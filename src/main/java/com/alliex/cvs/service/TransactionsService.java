package com.alliex.cvs.service;

import com.alliex.cvs.domain.product.ProductRepository;
import com.alliex.cvs.domain.transaction.*;
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
    public String getTransStateByBarcode(String barcode) {
        Transaction transaction = transactionRepository.findByTransNumber(barcode);

        return transaction.getTransState().toString();
    }

    @Transactional
    public String transactionPaymentStep1(TransactionSaveRequest requestParam) {
        // user point 조회 후 거래금액보다 작을시 false
        PointResponse point = pointService.findByUserId(requestParam.getBuyerId());
        String transNumber = "NOT ENOUGH POINT";
        if (point.getPoint() >= requestParam.getTransPoint()) {
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
        // 데이터 추출 by barcode
        if (transaction.getTransNumber() != null) {
            // 거래상태가 WAIT 상태일때 진행
            // paymnet
            if (TransState.WAIT.equals(transaction.getTransState())) {
                // 거래 진행
                // 포인트 차감 point
                pointService.updatePointMinus(transaction.getUser().getId(), transaction.getTransPoint());
                // 재고 차감 product productService productAmountMinus 작업 후 적용예정

                // 거래상태 변경 "SUCCESS" transaction
                update(transaction.getId(), TransState.SUCCESS);
                // 해당 상세거래 상태 변경 "SUCCESS" transactionDetail
                transactionsDetailService.update(transaction.getId(), TransState.SUCCESS);
            } else {
                throw new TransactionNotFoundException("trans_state error");
            }
        } else {
            update(transaction.getId(), TransState.FAIL);
            transactionsDetailService.update(transaction.getId(), TransState.FAIL);
            throw new TransactionNotFoundException("Invalid BARCODE");
        }

        return "SUCCESS";
    }

    @Transactional
    public Long transactionRefund(Long transId) {
        List<Transaction> transactionchk = transactionRepository.findByOriginId(transId);
        if (transactionchk.size() == 0) {
            Transaction transaction = transactionRepository.findById(transId).orElseThrow(()
                    -> new TransactionNotFoundException("Not found transaction : id" + transId));
            // transState='SUCCESS' and transType = 'PAYMENT' 일때만 가능
            if (TransState.SUCCESS.equals(transaction.getTransState()) && TransType.PAYMENT.equals(transaction.getTransType())) {
                // 포인트 복구 point
                pointService.updatePointPlus(transaction.getUser().getId(), transaction.getTransPoint());
                // 재고 복구 product productService productAmountPlus 작업 후 적용예정

                // 상세거래 상태 변경 "REFUND" transactionDetail
                transactionsDetailService.update(transId, TransState.REFUND);
                // 취소거래 save transaction
                transaction.setOriginId(transId);
                TransactionSaveRequest transactionRefundRequest = new TransactionSaveRequest(transaction.getUser().getId(), transaction.getMerchantId(), transaction.getId(),
                        transaction.getTransPoint(), TransState.REFUND, TransType.REFUND, transaction.getTransNumber());
                return transactionRepository.save(transactionRefundRequest.toEntity()).getId();
            } else {
                throw new TransactionNotFoundException("REFUND Fail");
            }
        } else {
            throw new TransactionNotFoundException("already REFUND");
        }
    }

}
