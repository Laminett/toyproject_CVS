package com.alliex.cvs.service;

import com.alliex.cvs.domain.transaction.*;
import com.alliex.cvs.domain.type.PaymentType;
import com.alliex.cvs.domain.type.TransactionState;
import com.alliex.cvs.domain.type.TransactionType;
import com.alliex.cvs.exception.NotEnoughPointException;
import com.alliex.cvs.exception.TransactionNotFoundException;
import com.alliex.cvs.exception.TransactionStateException;
import com.alliex.cvs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<Transaction> getTransactions(Pageable pageable, TransactionRequest searchRequest) {
        Map<TransactionSpecs.SearchKey, Object> searchKeys = new HashMap<>();
        Object searchValue;

        // FIXME
        if (StringUtils.isBlank(searchRequest.getSearchValue())) {
            return transactionRepository.findAll(pageable);
        }

        switch (searchRequest.getSearchField()) {
            case "state":
                searchValue = TransactionState.valueOf(searchRequest.getSearchValue().toUpperCase());
                break;
            case "type":
                searchValue = TransactionType.valueOf(searchRequest.getSearchValue().toUpperCase());
                break;
            case "paymentType":
                searchValue = PaymentType.valueOf(searchRequest.getSearchValue().toUpperCase());
                break;
            default:
                searchValue = searchRequest.getSearchValue();
                break;
        }
        searchKeys.put(TransactionSpecs.SearchKey.valueOf(searchRequest.getSearchField().toUpperCase()), searchValue);

        return transactionRepository.findAll(TransactionSpecs.searchWith(searchKeys), pageable);
    }

    @Transactional
    public Long save(TransactionSaveRequest transactionSaveRequest) {
        return transactionRepository.save(transactionSaveRequest.toEntity()).getId();
    }

    @Transactional
    public Long update(Long transId, TransactionState transactionState, PaymentType paymentType) {
        Transaction transaction = transactionRepository.findById(transId).orElseThrow(()
                -> new TransactionNotFoundException(transId));
        transaction.update(transactionState, PaymentType.valueOf(paymentType.toString()));

        return transaction.getId();
    }

    @Transactional
    public TransactionResponse getTransStateByRequestId(String requestId) {
        Transaction transaction = transactionRepository.findByRequestId(requestId)
                .orElseThrow(() -> new TransactionNotFoundException(requestId));
        return new TransactionResponse(transaction);
    }

    @Transactional
    public TransactionResponse transactionPaymentStep1(TransactionSaveRequest requestParam) {
        // user point 조회 후 거래금액보다 작을시 false
        PointResponse point = pointService.findByUserId(requestParam.getUserId());
        if (point.getPoint() < requestParam.getPoint()) {
            // HACK point Integer? Long?
            throw new NotEnoughPointException(point.getPoint(), requestParam.getPoint());
        }

        String requestId = RandomStringUtils.randomAlphanumeric(20);
        requestParam.setRequestId(requestId);
        Long transId = save(requestParam);

        for (Map<String, String> tMap : requestParam.getTransProduct()) {
            TransactionDetailSaveRequest saveRequest = new TransactionDetailSaveRequest(Integer.parseInt(tMap.get("productAmount")),
                    Long.parseLong(tMap.get("productId")), TransactionState.WAIT, transId);
            transactionDetailService.save(saveRequest);
        }

        return new TransactionResponse(TransactionState.WAIT, requestId);
    }

    @Transactional
    public TransactionResponse transactionPaymentStep2(String requestId, PaymentType paymentType) {
        Transaction transaction = transactionRepository.findByRequestId(requestId)
                .orElseThrow(() -> new TransactionNotFoundException(requestId));

        List<TransactionDetailResponse> transactionDetail = transactionDetailService.getDetails(transaction.getId());

        if (transaction.getState() != TransactionState.WAIT) {
            throw new TransactionStateException("PAYMENT is possible only TransactionState=WAIT and TransactionType=PAYMENT");
        }

        pointService.updatePointMinus(transaction.getUser().getId(), transaction.getPoint());
        for (TransactionDetailResponse product : transactionDetail) {
            productService.decreaseQuantity(product.getProductId(), product.getProductQuantity());
        }
        update(transaction.getId(), TransactionState.SUCCESS, paymentType);
        transactionDetailService.update(transaction.getId(), TransactionState.SUCCESS);

        return new TransactionResponse(transaction.getState(), transaction.getRequestId());
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
        pointService.updatePointPlus(transaction.getUser().getId(), transaction.getPoint());
        for (TransactionDetailResponse product : transactionDetail) {
            productService.increaseQuantity(product.getProductId(), product.getProductQuantity());
        }
        transactionDetailService.update(transId, TransactionState.REFUND);
        TransactionSaveRequest transactionRefundRequest = new TransactionSaveRequest(transaction.getUser().getId(), transaction.getMerchantId(), transaction.getId(),
                transaction.getPoint(), TransactionState.REFUND, TransactionType.REFUND, transaction.getRequestId(), transaction.getPaymentType());

        return transactionRepository.save(transactionRefundRequest.toEntity()).getId();
    }

}
