package com.alliex.cvs.web;

import com.alliex.cvs.domain.transaction.Transaction;
import com.alliex.cvs.service.TransactionDetailService;
import com.alliex.cvs.service.TransactionService;
import com.alliex.cvs.web.dto.TransactionDetailResponse;
import com.alliex.cvs.web.dto.TransactionRequest;
import com.alliex.cvs.web.dto.TransactionResponse;
import com.alliex.cvs.web.dto.TransactionSaveRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class TransactionApiController {

    private final TransactionService transactionService;

    private final TransactionDetailService transactionDetailService;

    public TransactionApiController(TransactionService transactionService, TransactionDetailService transactionDetailService) {
        this.transactionService = transactionService;
        this.transactionDetailService = transactionDetailService;
    }

    @ApiOperation(value = "get Transaction", notes = "거래 화면 데이터, 거래 화면 검색")
    @GetMapping({"/api/v1/transactions", "/web-api/v1/transactions"})
    public Page<Transaction> getTransactions(@RequestParam(value = "page", required = false) int page, TransactionRequest searchRequest) {
        return transactionService.getTransactions(PageRequest.of(page - 1, 10, Sort.Direction.DESC, "id"), searchRequest);
    }

    @ApiOperation(value = "Transaction Pay Step1", notes = "거래 바코드 전송")
    @PostMapping({"/api/v1/transactions/payment/step1", "/web-api/v1/transactions/payment/step1"})
    public TransactionResponse TransactionPaymentStep1(@RequestBody TransactionSaveRequest requestParam) {
        return transactionService.transactionPaymentStep1(requestParam);
    }

    @ApiOperation(value = "Transaction Pay Step2", notes = "바코드 조회 거래 승인/거절")
    @PutMapping({"/api/v1/transactions/payment/step2/{barcode}", "/web-api/v1/transactions/payment/step2/{barcode}"})
    public TransactionResponse TransactionPaymentStep2(@PathVariable String barcode, @RequestBody Transaction transaction) {
        return transactionService.transactionPaymentStep2(barcode, transaction.getPaymentType());
    }

    @ApiOperation(value = "Transaction refund", notes = "거래 취소")
    @PostMapping({"/api/v1/transactions/refund/{transId}", "/web-api/v1/transactions/refund/{transId}"})
    public Long TransactionRefund(@PathVariable Long transId) {
        return transactionService.transactionRefund(transId);
    }

    @ApiOperation(value = "get Transaction Status", notes = "거래상태 조회")
    @GetMapping({"/api/v1/transactions/state/{requestId}", "/web-api/v1/transactions/state/{requestId}"})
    public TransactionResponse getTransactionState(@PathVariable String requestId) {
        return transactionService.getTransStateByRequestId(requestId);
    }

    @ApiOperation(value = "get Transaction detail", notes = "거래 상세정보 조회")
    @GetMapping({"/api/v1/transactions/{transId}", "/web-api/v1/transactions/{transId}"})
    public List<TransactionDetailResponse> getTransactionDetailById(@PathVariable Long transId) {
        return transactionDetailService.getDetails(transId);
    }

}
