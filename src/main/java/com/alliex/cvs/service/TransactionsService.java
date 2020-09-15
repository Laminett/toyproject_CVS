package com.alliex.cvs.service;

import com.alliex.cvs.domain.transaction.*;
import com.alliex.cvs.domain.type.TransState;
import com.alliex.cvs.domain.type.TransType;
import com.alliex.cvs.exception.TransactionNotFoundException;
import com.alliex.cvs.util.RandomMathUtils;
import com.alliex.cvs.web.dto.*;
import lombok.RequiredArgsConstructor;
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
public class TransactionsService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionsDetailService transactionsDetailService;

    @Autowired
    private PointService pointService;

    @Autowired
    private ProductsService productsService;

    @Transactional(readOnly = true)
    public Page<Transaction> getTransactions(Pageable pageable, TransactionRequest searchRequest) {
        Map<TransactionSpecs.SearchKey, Object> searchKeys = new HashMap<>();

        if (!"".equals(searchRequest.getSearchField()) && !"".equals(searchRequest.getSearchValue())) {
            if ("transState".equals(searchRequest.getSearchField())) {
                searchKeys.put(TransactionSpecs.SearchKey.valueOf(searchRequest.getSearchField().toUpperCase()), TransState.valueOf(searchRequest.getSearchValue().toUpperCase()));
            } else if ("transType".equals(searchRequest.getSearchField())) {
                searchKeys.put(TransactionSpecs.SearchKey.valueOf(searchRequest.getSearchField().toUpperCase()), TransType.valueOf(searchRequest.getSearchValue().toUpperCase()));
            } else {
                searchKeys.put(TransactionSpecs.SearchKey.valueOf(searchRequest.getSearchField().toUpperCase()), searchRequest.getSearchValue());
            }
        }

        return searchKeys.isEmpty() ? transactionRepository.findAll(pageable) : transactionRepository.findAll(TransactionSpecs.searchWith(searchKeys), pageable);
    }

    @Transactional
    public Long save(TransactionSaveRequest transactionSaveRequest) {
        return transactionRepository.save(transactionSaveRequest.toEntity()).getId();
    }

    @Transactional
    public Long update(Long transId, TransState transState) {
        Transaction transaction = transactionRepository.findById(transId).orElseThrow(()
                -> new TransactionNotFoundException("Not found transaction : id" + transId));
        transaction.update(transState);

        return transaction.getId();
    }

    @Transactional
    public Transaction getTransStateByBarcode(String barcode) {
        Transaction transaction = transactionRepository.findByTransNumber(barcode);

        return transaction;
    }

    @Transactional
    public String transactionPaymentStep1(TransactionSaveRequest requestParam) {
        // user point 조회 후 거래금액보다 작을시 false
        PointResponse point = pointService.findByUserId(requestParam.getBuyerId());
        String transNumber = "NOT ENOUGH POINT";
        if (point.getPoint() >= requestParam.getPoint()) {
            transNumber = RandomMathUtils.createTransNumber();
            requestParam.setTransNumber(transNumber);
            Long transId = save(requestParam);

            for (Map<String, String> tMap : requestParam.getTransProduct()) {
                TransactionDetailSaveRequest saveRequest = new TransactionDetailSaveRequest(Integer.parseInt(tMap.get("productAmount")),
                        Long.parseLong(tMap.get("productId")), TransState.WAIT, transId);
                transactionsDetailService.save(saveRequest);
            }
        }

        return transNumber;
    }

    @Transactional
    public String transactionPaymentStep2(String barcode) {
        Transaction transaction = transactionRepository.findByTransNumber(barcode);
        List<TransactionDetailResponse> transactionDetail = transactionsDetailService.getDetails(transaction.getId());
        if (transaction.getTransNumber().isEmpty()) {
            update(transaction.getId(), TransState.FAIL);
            transactionsDetailService.update(transaction.getId(), TransState.FAIL);
            throw new TransactionNotFoundException("Invalid BARCODE");
        }

        if (!TransState.WAIT.equals(transaction.getTransState())) {
            throw new TransactionNotFoundException("trans_state error");
        }

        pointService.updatePointMinus(transaction.getUser().getId(), transaction.getPoint());
        for (TransactionDetailResponse product : transactionDetail) {
            productsService.updateAmountMinus(product.getProductId(), product.getProductAmount());
        }
        update(transaction.getId(), TransState.SUCCESS);
        transactionsDetailService.update(transaction.getId(), TransState.SUCCESS);

        return "SUCCESS";
    }

    @Transactional
    public Long transactionRefund(Long transId) {
        List<Transaction> transactionchk = transactionRepository.findByOriginId(transId);
        if (!transactionchk.isEmpty()) {
            throw new TransactionNotFoundException("already REFUND");
        }

        Transaction transaction = transactionRepository.findById(transId).orElseThrow(()
                -> new TransactionNotFoundException("Not found transaction : id" + transId));

        List<TransactionDetailResponse> transactionDetail = transactionsDetailService.getDetails(transaction.getId());
        if (!TransState.SUCCESS.equals(transaction.getTransState()) || !TransType.PAYMENT.equals(transaction.getTransType())) {
            throw new TransactionNotFoundException("REFUND Fail");
        }

        pointService.updatePointPlus(transaction.getUser().getId(), transaction.getPoint());
        for (TransactionDetailResponse product : transactionDetail) {
            productsService.updateAmountPlus(product.getProductId(), product.getProductAmount());
        }
        transactionsDetailService.update(transId, TransState.REFUND);
        transaction.setOriginId(transId);
        TransactionSaveRequest transactionRefundRequest = new TransactionSaveRequest(transaction.getUser().getId(), transaction.getMerchantId(), transaction.getId(),
                transaction.getPoint(), TransState.REFUND, TransType.REFUND, transaction.getTransNumber());

        return transactionRepository.save(transactionRefundRequest.toEntity()).getId();
    }

}
