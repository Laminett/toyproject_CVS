package com.alliex.cvs.web;

import com.alliex.cvs.domain.transaction.Transaction;
import com.alliex.cvs.service.TransactionDetailService;
import com.alliex.cvs.service.TransactionService;
import com.alliex.cvs.web.dto.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class TransactionApiController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionDetailService transactionDetailService;

    @ApiOperation(value = "get Transactions", notes = "거래 화면 데이터, 거래 화면 검색")
    @GetMapping({"/api/v1/transactions", "/web-api/v1/transactions"})
    public Page<TransactionResponse> getTransactions(@RequestParam(value = "page", required = false, defaultValue = "1") int page, TransactionRequest searchRequest) {
        return transactionService.getTransactions(PageRequest.of(page - 1, 10, Sort.Direction.DESC, "id"), searchRequest);
    }

    @ApiOperation(value = "get Transaction", notes = "거래 화면 데이터, 거래 화면 검색")
    @GetMapping({"/api/v1/transactions/{id}", "/web-api/v1/transactions/{id}"})
    public TransactionResponse getTransaction(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @ApiOperation(value = "Transaction QRPay Step1", notes = "거래 QR스트링 전송")
    @PostMapping({"/api/v1/transactions/payment/QRstep1", "/web-api/v1/transactions/payment/QRstep1"})
    public TransactionStateResponse QRPaymentStep1(@RequestBody TransactionSaveRequest transactionSaveRequest) {
        return transactionService.QRPaymentStep1(transactionSaveRequest);
    }

    @ApiOperation(value = "Transaction QRPay Step2", notes = "QR 조회 거래 승인/거절")
    @PutMapping({"/api/v1/transactions/payment/QRstep2/{requestId}", "/web-api/v1/transactions/payment/QRstep2/{requestId}"})
    public TransactionStateResponse QRPaymentStep2(@PathVariable String requestId, @RequestBody TransactionSaveRequest transactionSaveRequest) {
        return transactionService.QRPaymentStep2(requestId, transactionSaveRequest);
    }

    @ApiOperation(value = "Transaction refund", notes = "거래 취소")
    @PostMapping({"/api/v1/transactions/refund/{transId}", "/web-api/v1/transactions/refund/{transId}"})
    public Long TransactionRefund(@PathVariable Long transId) {
        return transactionService.transactionRefund(transId);
    }

    @ApiOperation(value = "get Transaction Status", notes = "거래상태 조회")
    @GetMapping({"/api/v1/transactions/state/{requestId}", "/web-api/v1/transactions/state/{requestId}"})
    public TransactionStateResponse getTransactionState(@PathVariable String requestId) {
        return transactionService.getTransStateByRequestId(requestId);
    }

    @ApiOperation(value = "get Transaction detail", notes = "거래 상세정보 조회")
    @GetMapping({"/api/v1/transactions/items/{transId}", "/web-api/v1/transactions/items/{transId}"})
    public List<TransactionDetailResponse> getTransactionDetailById(@PathVariable Long transId) {
        return transactionDetailService.getDetails(transId);
    }

}
