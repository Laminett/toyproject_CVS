package com.alliex.cvs.service;

import com.alliex.cvs.domain.transaction.*;
import com.alliex.cvs.domain.type.TransactionSearchType;
import com.alliex.cvs.domain.type.TransactionState;
import com.alliex.cvs.domain.type.TransactionType;
import com.alliex.cvs.domain.user.LoginUser;
import com.alliex.cvs.domain.user.User;
import com.alliex.cvs.exception.NotEnoughPointException;
import com.alliex.cvs.exception.TransactionAlreadyRefundException;
import com.alliex.cvs.exception.TransactionNotFoundException;
import com.alliex.cvs.exception.TransactionStateException;
import com.alliex.cvs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final TransactionDetailService transactionDetailService;

    private final PointService pointService;

    private final ProductService productService;

    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<TransactionResponse> getTransactions(Pageable pageable, TransactionRequest transactionRequest) {
        return transactionRepository.findAll(searchWith(getPredicateData(transactionRequest)), pageable).map(TransactionResponse::new);
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransactionByRequestId(String requestId) {
        Transaction transaction = transactionRepository.findByRequestId(requestId)
                .orElseThrow(() -> new TransactionNotFoundException(requestId));

        return new TransactionResponse(transaction);
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
    public TransactionStateResponse paymentFromPosStep1(List<TransactionDetailSaveRequest> transactionDetailSaveRequests) {
        String requestId = RandomStringUtils.randomAlphanumeric(20);

        for (TransactionDetailSaveRequest detailMap : transactionDetailSaveRequests) {
            TransactionDetailSaveRequest saveRequest = new TransactionDetailSaveRequest(detailMap.getQuantity(),
                    detailMap.getProductId(), TransactionState.WAIT, requestId);
            transactionDetailService.save(saveRequest);
        }

        return new TransactionStateResponse(TransactionState.WAIT, requestId);
    }

    @Transactional
    public TransactionStateResponse paymentFromPosStep2(TransactionSaveRequest transactionSaveRequest, LoginUser loginUser) {
        UserResponse userResponse = userService.getUserByUsername(loginUser.getUsername());
        PointResponse point = pointService.findByUserId(userResponse.getId());
        Long _point = 0L;

        List<TransactionDetailResponse> transactionDetailResponses = transactionDetailService.getDetailByRequestId(transactionSaveRequest.getRequestId());

        for (TransactionDetailResponse detailMap : transactionDetailResponses) {
            if (detailMap.getTransactionState() != TransactionState.WAIT) {
                throw new TransactionStateException("PAYMENT is possible only TransactionState=WAIT and TransactionType=PAYMENT");
            }

            productService.getProductById(detailMap.getProductId());
            int _productQuantity = detailMap.getProductQuantity();
            ProductResponse _product = productService.getProductById(detailMap.getProductId());

            productService.decreaseQuantity(_product.getId(), _productQuantity);

            _point += _productQuantity * _product.getPoint();
        }

        if (point.getPoint() < _point) {
            throw new NotEnoughPointException(point.getPoint(), _point);
        }

        pointService.updatePointMinus(userResponse.getId(), _point);

        TransactionSave transactionSave = new TransactionSave();
        transactionSave.setRequestId(transactionSaveRequest.getRequestId());
        transactionSave.setPaymentType(transactionSaveRequest.getPaymentType());
        transactionSave.setType(TransactionType.PAYMENT);
        transactionSave.setState(TransactionState.SUCCESS);
        transactionSave.setUserId(userResponse.getId());
        transactionSave.setPoint(_point);
        Long transId = save(transactionSave);

        transactionDetailService.update(transactionSaveRequest.getRequestId(), TransactionState.SUCCESS);

        Transaction transaction = transactionRepository.findById(transId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionSaveRequest.getRequestId()));

        return new TransactionStateResponse(transaction);
    }

    @Transactional
    public TransactionStateResponse appPayment(TransactionSaveRequest transactionSaveRequest, LoginUser loginUser) {
        UserResponse userResponse = userService.getUserByUsername(loginUser.getUsername());
        PointResponse point = pointService.findByUserId(userResponse.getId());
        Long _point = 0L;

        for (TransactionDetailSaveRequest DetailMap : transactionSaveRequest.getTransProduct()) {
            TransactionDetailSaveRequest saveRequest = new TransactionDetailSaveRequest(DetailMap.getQuantity(),
                    DetailMap.getProductId(), TransactionState.SUCCESS, transactionSaveRequest.getRequestId());
            transactionDetailService.save(saveRequest);

            Long _productId = DetailMap.getProductId();
            int _productQuantity = DetailMap.getQuantity();
            ProductResponse _product = productService.getProductById(_productId);

            productService.decreaseQuantity(_product.getId(), _productQuantity);

            _point += _productQuantity * _product.getPoint();
        }

        if (point.getPoint() < _point) {
            throw new NotEnoughPointException(point.getPoint(), _point);
        }

        TransactionSave transactionSave = new TransactionSave();
        transactionSave.setPaymentType(transactionSaveRequest.getPaymentType());
        transactionSave.setPoint(_point);
        transactionSave.setState(TransactionState.SUCCESS);
        transactionSave.setUserId(userResponse.getId());
        transactionSave.setType(TransactionType.PAYMENT);
        transactionSave.setRequestId(transactionSaveRequest.getRequestId());
        Long transId = save(transactionSave);

        pointService.updatePointMinus(userResponse.getId(), _point);

        Transaction transaction = transactionRepository.findById(transId)
                .orElseThrow(() -> new TransactionNotFoundException(transId));

        return new TransactionStateResponse(transaction);
    }

    @Transactional
    public TransactionRefundResponse transactionRefund(String requestId) {
        transactionRepository.findByOriginRequestId(requestId).ifPresent(transaction -> {
            throw new TransactionAlreadyRefundException(requestId);
        });

        Transaction transaction = transactionRepository.findByRequestId(requestId).orElseThrow(()
                -> new TransactionNotFoundException(requestId));

        if (transaction.getState() != TransactionState.SUCCESS || transaction.getType() != TransactionType.PAYMENT) {
            throw new TransactionStateException("REFUND is possible only TransactionState=SUCCESS and TransactionType=PAYMENT");
        }

        List<TransactionDetailResponse> transactionDetail = transactionDetailService.getDetailByRequestId(transaction.getRequestId());
        pointService.updatePointPlus(transaction.getUser().getId(), transaction.getPoint());
        for (TransactionDetailResponse productDetail : transactionDetail) {
            productService.increaseQuantity(productDetail.getProductId(), productDetail.getProductQuantity());
        }

        transactionDetailService.update(transaction.getRequestId(), TransactionState.REFUND);

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

    private Specification<Transaction> searchWith(Map<TransactionSearchType, Object> predicateData) {
        return (Specification<Transaction>) ((root, query, builder) -> {
            List<Predicate> predicate = new ArrayList<>();
            for (Map.Entry<TransactionSearchType, Object> entry : predicateData.entrySet()) {
                switch (entry.getKey()) {
                    case USERID:
                        Join<Transaction, User> userJoin = root.join(entry.getKey().getValue());
                        predicate.add(builder.like(userJoin.get("username"), "%" + entry.getValue() + "%"));
                        break;
                    case TYPE:
                    case POINT:
                    case STATE:
                    case PAYMENTTYPE:
                        predicate.add(builder.like(root.get(entry.getKey().getValue()).as(String.class), "%" + entry.getValue() + "%"));
                        break;
                    default:
                        break;
                }
            }

            return builder.and(predicate.toArray(new Predicate[0]));
        });
    }

    private Map<TransactionSearchType, Object> getPredicateData(TransactionRequest transactionRequest) {
        Map<TransactionSearchType, Object> predicateData = new HashMap<>();

        if (StringUtils.isNotBlank(transactionRequest.getUserId())) {
            predicateData.put(TransactionSearchType.USERID, transactionRequest.getUserId());
        }

        if (StringUtils.isNotBlank(transactionRequest.getPaymentType())) {
            predicateData.put(TransactionSearchType.PAYMENTTYPE, transactionRequest.getPaymentType());
        }

        if (transactionRequest.getPoint() != null) {
            predicateData.put(TransactionSearchType.POINT, transactionRequest.getPoint());
        }

        if (StringUtils.isNotBlank(transactionRequest.getState())) {
            predicateData.put(TransactionSearchType.STATE, transactionRequest.getState());
        }

        if (StringUtils.isNotBlank(transactionRequest.getType())) {
            predicateData.put(TransactionSearchType.TYPE, transactionRequest.getType());
        }

        return predicateData;
    }
}
