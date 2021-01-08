package com.alliex.cvs.service;

import com.alliex.cvs.domain.transaction.*;
import com.alliex.cvs.domain.type.PaymentType;
import com.alliex.cvs.domain.type.TransactionSearchType;
import com.alliex.cvs.domain.type.TransactionState;
import com.alliex.cvs.domain.type.TransactionType;
import com.alliex.cvs.domain.user.User;
import com.alliex.cvs.exception.NotEnoughPointException;
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

    @Transactional(readOnly = true)
    public Page<TransactionResponse> getTransactions(Pageable pageable, TransactionRequest transactionRequest) {
        return transactionRepository.findAll(searchWith(getPredicateData(transactionRequest)), pageable).map(TransactionResponse::new);
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));

        return new TransactionResponse(transaction);
    }

    @Transactional
    public Long save(TransactionSaveRequest transactionSaveRequest) {
        return transactionRepository.save(transactionSaveRequest.toEntity()).getId();
    }

    @Transactional
    public Long update(Long transId, Long userId, TransactionState transactionState) {
        Transaction transaction = transactionRepository.findById(transId).orElseThrow(()
                -> new TransactionNotFoundException(transId));
        transaction.update(userId, transactionState);

        return transaction.getId();
    }

    @Transactional
    public TransactionStateResponse getTransStateByRequestId(String requestId) {
        Transaction transaction = transactionRepository.findByRequestId(requestId)
                .orElseThrow(() -> new TransactionNotFoundException(requestId));
        return new TransactionStateResponse(transaction);
    }

    @Transactional
    public TransactionStateResponse QRPaymentStep1(TransactionSaveRequest transactionSaveRequest) {
        String requestId = RandomStringUtils.randomAlphanumeric(20);
        transactionSaveRequest.setRequestId(requestId);
        transactionSaveRequest.setPaymentType(PaymentType.QR);
        transactionSaveRequest.setType(TransactionType.PAYMENT);
        transactionSaveRequest.setState(TransactionState.WAIT);
        Long transId = save(transactionSaveRequest);


        for (Map<String, String> tMap : transactionSaveRequest.getTransProduct()) {
            TransactionDetailSaveRequest saveRequest = new TransactionDetailSaveRequest(Integer.parseInt(tMap.get("productAmount")),
                    Long.parseLong(tMap.get("productId")), TransactionState.WAIT, transId);
            transactionDetailService.save(saveRequest);
        }

        return new TransactionStateResponse(TransactionState.WAIT, requestId);
    }

    @Transactional
    public TransactionStateResponse QRPaymentStep2(String requestId, TransactionSaveRequest transactionSaveRequest) {
        Transaction transaction = transactionRepository.findByRequestId(requestId)
                .orElseThrow(() -> new TransactionNotFoundException(requestId));

        // user point 조회 후 거래금액보다 작을시 false
        PointResponse point = pointService.findByUserId(transactionSaveRequest.getUserId());
        if (point.getPoint() < transaction.getPoint()) {
            throw new NotEnoughPointException(point.getPoint(), transactionSaveRequest.getPoint());
        }

        List<TransactionDetailResponse> transactionDetail = transactionDetailService.getDetails(transaction.getId());

        if (transaction.getState() != TransactionState.WAIT) {
            throw new TransactionStateException("PAYMENT is possible only TransactionState=WAIT and TransactionType=PAYMENT");
        }

        // User -> Transaction 관계 임시 해제를 위한 주석
//        pointService.updatePointMinus(transaction.getUser().getId(), transaction.getPoint());
        pointService.updatePointMinus(transactionSaveRequest.getUserId(), transaction.getPoint());
        for (TransactionDetailResponse product : transactionDetail) {
            productService.decreaseQuantity(product.getProductId(), product.getProductQuantity());
        }
        update(transaction.getId(), transactionSaveRequest.getUserId(), TransactionState.SUCCESS);
        transactionDetailService.update(transaction.getId(), TransactionState.SUCCESS);

        return new TransactionStateResponse(transaction.getState(), transaction.getRequestId());
    }

    @Transactional
    public Long transactionRefund(Long transId) {
        List<Transaction> originTransactionIdCheck = transactionRepository.findByOriginId(transId);
        if (!originTransactionIdCheck.isEmpty()) {
            throw new TransactionNotFoundException(transId);
        }

        Transaction transaction = transactionRepository.findById(transId).orElseThrow(()
                -> new TransactionNotFoundException(transId));

        if (transaction.getState() != TransactionState.SUCCESS || transaction.getType() != TransactionType.PAYMENT) {
            // FIXME
            throw new TransactionStateException("REFUND is possible only TransactionState=SUCCESS and TransactionType=PAYMENT");
        }

        List<TransactionDetailResponse> transactionDetail = transactionDetailService.getDetails(transaction.getId());
        // User -> Transaction 관계 임시 해제를 위한 주석
//        pointService.updatePointPlus(transaction.getUser().getId(), transaction.getPoint());
        pointService.updatePointPlus(transaction.getUserId(), transaction.getPoint());
        for (TransactionDetailResponse product : transactionDetail) {
            productService.increaseQuantity(product.getProductId(), product.getProductQuantity());
        }
        transactionDetailService.update(transId, TransactionState.REFUND);
        // User -> Transaction 관계 임시 해제를 위한 주석
//        TransactionSaveRequest transactionRefundRequest = new TransactionSaveRequest(transaction.getUser().getId(), transaction.getMerchantId(), transaction.getId(),
        TransactionSaveRequest transactionRefundRequest = new TransactionSaveRequest(transaction.getUserId(), transaction.getMerchantId(), transaction.getId(),
                transaction.getPoint(), TransactionState.REFUND, TransactionType.REFUND, transaction.getRequestId(), transaction.getPaymentType());

        return transactionRepository.save(transactionRefundRequest.toEntity()).getId();
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
                    case MERCHANTID:
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

        if (StringUtils.isNotBlank(transactionRequest.getMerchantId())) {
            predicateData.put(TransactionSearchType.MERCHANTID, transactionRequest.getMerchantId());
        }

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
