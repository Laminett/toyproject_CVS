package com.alliex.cvs.service;

import com.alliex.cvs.entity.Transaction;
import com.alliex.cvs.repository.TransactionRepository;
import com.alliex.cvs.domain.type.TransactionState;
import com.alliex.cvs.domain.type.TransactionType;
import com.alliex.cvs.domain.LoginUser;
import com.alliex.cvs.entity.User;
import com.alliex.cvs.exception.NotEnoughPointException;
import com.alliex.cvs.exception.TransactionAlreadyRefundedException;
import com.alliex.cvs.exception.TransactionNotFoundException;
import com.alliex.cvs.exception.TransactionStateException;
import com.alliex.cvs.repository.TransactionRepositorySupport;
import com.alliex.cvs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final TransactionDetailService transactionDetailService;

    private final TransactionRepositorySupport transactionRepositorySupport;

    private final PointService pointService;

    private final ProductService productService;

    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<TransactionResponse> getTransactions(Pageable pageable, TransactionRequest transactionRequest) {
        Page<Transaction> transactions = transactionRepositorySupport.findBySearchValueWithDate(pageable, transactionRequest);

        return transactions.map(TransactionResponse::new);
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransactionByRequestId(String requestId) {
        Transaction transaction = transactionRepository.findByRequestId(requestId)
                .orElseThrow(() -> new TransactionNotFoundException(requestId));

        TransactionResponse transactionResponse = new TransactionResponse(transaction);

        List<TransactionDetailResponse> transactionItems;

        if (transaction.getType() == TransactionType.REFUND) {
            transactionItems = transactionDetailService.getDetailByRequestId(transaction.getOriginRequestId());
        } else {
            transactionItems = transactionDetailService.getDetailByRequestId(transaction.getRequestId());
        }

        transactionRepository.findByOriginRequestId(requestId).ifPresent(originTransaction -> {
            transactionResponse.setIsRefunded(true);
        });

        transactionResponse.setTransactionItems(transactionItems);

        return transactionResponse;
    }

    @Transactional
    public Long save(TransactionSave transactionSave) {
        User setUserId = new User();
        setUserId.setId(transactionSave.getUserId());

        Transaction transaction = Transaction.builder()
                .user(setUserId)
                .originRequestId(transactionSave.getOriginRequestId())
                .point(transactionSave.getPoint())
                .transactionState(transactionSave.getState())
                .transactionType(transactionSave.getType())
                .paymentType(transactionSave.getPaymentType())
                .requestId(transactionSave.getRequestId())
                .build();

        return transactionRepository.save(transaction).getId();
    }

    @Transactional
    public TransactionStateResponse getTransStateByRequestId(String requestId) {
        Transaction transaction = transactionRepository.findByRequestId(requestId)
                .orElseThrow(() -> new TransactionNotFoundException(requestId));
        return new TransactionStateResponse(transaction);
    }

    @Transactional
    public TransactionStateResponse paymentFromQrStep1(List<TransactionDetailSaveRequest> transactionDetailSaveRequests) {
        String requestId = RandomStringUtils.randomAlphanumeric(20);

        for (TransactionDetailSaveRequest item : transactionDetailSaveRequests) {
            TransactionDetailSaveRequest saveRequest = new TransactionDetailSaveRequest(item.getQuantity(),
                    item.getProductId(), requestId);
            transactionDetailService.save(saveRequest.toEntity());
        }

        return new TransactionStateResponse(TransactionState.WAIT, requestId);
    }

    /**
     * APP에서 QR코드를 읽을 때 호출된다.
     */
    @Transactional
    public TransactionStateResponse paymentFromQrStep2(TransactionSaveRequest transactionSaveRequest, LoginUser loginUser) {
        UserResponse userResponse = userService.getUserByUsername(loginUser.getUsername());
        PointResponse point = pointService.findByUserId(userResponse.getId());
        Long _point = 0L;

        // 차감할 포인트 계산
        List<TransactionDetailResponse> transactionDetailResponses = transactionDetailService.getDetailByRequestId(transactionSaveRequest.getRequestId());

        for (TransactionDetailResponse item : transactionDetailResponses) {
            int _productQuantity = item.getProductQuantity();
            ProductResponse _product = productService.getProductById(item.getProductId());
            _point += _productQuantity * _product.getPoint();

            // 상품 수량 차감 처리
            productService.decreaseQuantity(_product.getId(), _productQuantity);
        }

        if (point.getPoint() < _point) {
            throw new NotEnoughPointException(point.getPoint(), _point);
        }

        // 포인트 차감 처리
        pointService.updatePointMinus(userResponse.getId(), _point);

        TransactionSave transactionSave = new TransactionSave();
        transactionSave.setRequestId(transactionSaveRequest.getRequestId());
        transactionSave.setPaymentType(transactionSaveRequest.getPaymentType());
        transactionSave.setType(TransactionType.PAYMENT);
        transactionSave.setState(TransactionState.SUCCESS);
        transactionSave.setUserId(userResponse.getId());
        transactionSave.setPoint(_point);
        Long transId = save(transactionSave);

        Transaction transaction = transactionRepository.findById(transId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionSaveRequest.getRequestId()));

        return new TransactionStateResponse(transaction);
    }

    /**
     * APP에서 직접 결제할 때 호출 된다.
     */
    @Transactional
    public TransactionStateResponse appPayment(TransactionSaveRequest transactionSaveRequest, LoginUser loginUser) {
        UserResponse userResponse = userService.getUserByUsername(loginUser.getUsername());
        PointResponse point = pointService.findByUserId(userResponse.getId());
        Long _point = 0L;

        // 차감할 포인트 계산 및 상품 수량 차감
        for (TransactionDetailSaveRequest item : transactionSaveRequest.getTransProduct()) {
            TransactionDetailSaveRequest saveRequest = new TransactionDetailSaveRequest(item.getQuantity(),
                    item.getProductId(), transactionSaveRequest.getRequestId());
            transactionDetailService.save(saveRequest.toEntity());

            Long _productId = item.getProductId();
            int _productQuantity = item.getQuantity();
            ProductResponse _product = productService.getProductById(_productId);

            // 상품 수량 차감
            productService.decreaseQuantity(_product.getId(), _productQuantity);

            _point += _productQuantity * _product.getPoint();
        }

        if (point.getPoint() < _point) {
            throw new NotEnoughPointException(point.getPoint(), _point);
        }

        // 포인트 차감 처리
        pointService.updatePointMinus(userResponse.getId(), _point);

        TransactionSave transactionSave = new TransactionSave();
        transactionSave.setPaymentType(transactionSaveRequest.getPaymentType());
        transactionSave.setPoint(_point);
        transactionSave.setState(TransactionState.SUCCESS);
        transactionSave.setUserId(userResponse.getId());
        transactionSave.setType(TransactionType.PAYMENT);
        transactionSave.setRequestId(transactionSaveRequest.getRequestId());
        Long transId = save(transactionSave);

        Transaction transaction = transactionRepository.findById(transId)
                .orElseThrow(() -> new TransactionNotFoundException(transId));

        return new TransactionStateResponse(transaction);
    }

    /**
     * 거래를 반품할때 호출 된다.
     */
    @Transactional
    public TransactionRefundResponse transactionRefund(String requestId) {
        transactionRepository.findByOriginRequestId(requestId).ifPresent(transaction -> {
            throw new TransactionAlreadyRefundedException(requestId);
        });

        Transaction transaction = transactionRepository.findByRequestId(requestId).orElseThrow(()
                -> new TransactionNotFoundException(requestId));

        if (transaction.getState() != TransactionState.SUCCESS || transaction.getType() != TransactionType.PAYMENT) {
            throw new TransactionStateException("REFUND is possible only TransactionState=SUCCESS and TransactionType=PAYMENT");
        }

        // 차감된 상품 수량 원복
        List<TransactionDetailResponse> transactionDetail = transactionDetailService.getDetailByRequestId(transaction.getRequestId());
        for (TransactionDetailResponse productDetail : transactionDetail) {
            productService.increaseQuantity(productDetail.getProductId(), productDetail.getProductQuantity());
        }

        // 차감된 포인트 원복
        pointService.updatePointPlus(transaction.getUser().getId(), transaction.getPoint());

        TransactionSave transactionSave = new TransactionSave();
        String refundRequestId = RandomStringUtils.randomAlphanumeric(20);
        transactionSave.setUserId(transaction.getUser().getId());
        transactionSave.setOriginRequestId(transaction.getRequestId());
        transactionSave.setRequestId(refundRequestId);
        transactionSave.setPoint(transaction.getPoint());
        transactionSave.setState(TransactionState.REFUND);
        transactionSave.setType(TransactionType.REFUND);
        transactionSave.setPaymentType(transaction.getPaymentType());
        Long transId = save(transactionSave);

        Transaction refundTransaction = transactionRepository.findById(transId).orElseThrow(()
                -> new TransactionNotFoundException(refundRequestId));

        return new TransactionRefundResponse(refundTransaction);
    }

}
