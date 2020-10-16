package com.alliex.cvs.service;

import com.alliex.cvs.domain.transaction.*;
import com.alliex.cvs.domain.type.PaymentType;
import com.alliex.cvs.domain.type.TransactionState;
import com.alliex.cvs.domain.type.TransactionType;
import com.alliex.cvs.exception.NotEnoughPointException;
import com.alliex.cvs.exception.TransactionNotFoundException;
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

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionDetailService transactionDetailService;

    @Autowired
    private PointService pointService;

    @Autowired
    private ProductService productService;

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
                -> new TransactionNotFoundException("Not found transaction : id" + transId));
        transaction.update(transactionState, PaymentType.valueOf(paymentType.toString()));

        return transaction.getId();
    }

    @Transactional
    public TransactionResponse getTransStateByBarcode(String barcode) {
        Transaction transaction = transactionRepository.findByRequestId(barcode)
                .orElseThrow(() -> new TransactionNotFoundException("Not found transaction : barcode" + barcode));
        return new TransactionResponse(transaction);
    }

    @Transactional
    public TransactionResponse transactionPaymentStep1(TransactionSaveRequest requestParam) {
        // user point 조회 후 거래금액보다 작을시 false
        PointResponse point = pointService.findByUserId(requestParam.getUserId());
        if (point.getPoint() < requestParam.getPoint()) {
            throw new NotEnoughPointException("Not Enough point : have point " + point.getPoint() + " necessary point " + requestParam.getPoint());
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
    public TransactionResponse transactionPaymentStep2(String barcode, PaymentType paymentType) {
        Transaction transaction = transactionRepository.findByRequestId(barcode)
                .orElseThrow(() -> new TransactionNotFoundException("Not found transaction : barcode" + barcode));

        List<TransactionDetailResponse> transactionDetail = transactionDetailService.getDetails(transaction.getId());

        if (!TransactionState.WAIT.equals(transaction.getState())) {
            throw new TransactionNotFoundException("trans_state error");
        }

        pointService.updatePointMinus(transaction.getUser().getId(), transaction.getPoint());
        for (TransactionDetailResponse product : transactionDetail) {
            productService.updateAmountMinus(product.getProductId(), product.getProductQuantity());
        }
        update(transaction.getId(), TransactionState.SUCCESS, paymentType);
        transactionDetailService.update(transaction.getId(), TransactionState.SUCCESS);

        return new TransactionResponse(transaction.getState(), transaction.getRequestId());
    }

    @Transactional
    public Long transactionRefund(Long transId) {
        List<Transaction> transactionchk = transactionRepository.findByOriginId(transId);
        if (!transactionchk.isEmpty()) {
            throw new TransactionNotFoundException("already REFUND");
        }

        Transaction transaction = transactionRepository.findById(transId).orElseThrow(()
                -> new TransactionNotFoundException("Not found transaction : id" + transId));

        List<TransactionDetailResponse> transactionDetail = transactionDetailService.getDetails(transaction.getId());
        if (!TransactionState.SUCCESS.equals(transaction.getState()) || !TransactionType.PAYMENT.equals(transaction.getType())) {
            throw new TransactionNotFoundException("REFUND Fail");
        }

        pointService.updatePointPlus(transaction.getUser().getId(), transaction.getPoint());
        for (TransactionDetailResponse product : transactionDetail) {
            productService.updateAmountPlus(product.getProductId(), product.getProductQuantity());
        }
        transactionDetailService.update(transId, TransactionState.REFUND);
        TransactionSaveRequest transactionRefundRequest = new TransactionSaveRequest(transaction.getUser().getId(), transaction.getMerchantId(), transaction.getId(),
                transaction.getPoint(), TransactionState.REFUND, TransactionType.REFUND, transaction.getRequestId(), transaction.getPaymentType());

        return transactionRepository.save(transactionRefundRequest.toEntity()).getId();
    }

}
