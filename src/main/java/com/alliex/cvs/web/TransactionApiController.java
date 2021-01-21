package com.alliex.cvs.web;

import com.alliex.cvs.domain.transaction.Transaction;
import com.alliex.cvs.domain.user.LoginUser;
import com.alliex.cvs.service.TransactionDetailService;
import com.alliex.cvs.service.TransactionService;
import com.alliex.cvs.web.dto.*;
import com.auth0.jwt.internal.com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.bind.v2.model.core.TypeRef;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;


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
    @GetMapping({"/api/v1/transactions/{requestId}", "/web-api/v1/transactions/{requestId}"})
    public TransactionResponse getTransaction(@PathVariable String requestId) {
        return transactionService.getTransactionByRequestId(requestId);
    }

    @ApiOperation(value = "Transaction QRPay Step1", notes = "거래 QR스트링 전송")
    @PostMapping({"/api/v1/transactions/payment/pos/step1", "/web-api/v1/transactions/payment/pos/step1"})
    public TransactionStateResponse paymentFromPosStep1(@RequestBody List<TransactionDetailSaveRequest> transactionDetailSaveRequests) {
        return transactionService.paymentFromPosStep1(transactionDetailSaveRequests);
    }

    @ApiOperation(value = "Transaction QRPay Step2", notes = "QR 조회 거래 승인/거절")
    @PutMapping({"/api/v1/transactions/payment/pos/step2", "/web-api/v1/transactions/payment/pos/step2"})
    public TransactionStateResponse paymentFromPosStep2(@RequestBody TransactionSaveRequest transactionSaveRequest, @AuthenticationPrincipal LoginUser loginUser) {
        return transactionService.paymentFromPosStep2(transactionSaveRequest, loginUser);
    }

    @ApiOperation(value = "Transaction APP Payment", notes = "APP 을 사용하여 거래")
    @PostMapping({"/api/v1/transactions/payment/app", "/web-api/v1/transactions/payment/app"})
    public TransactionStateResponse paymentFromApp(@RequestBody TransactionSaveRequest transactionSaveRequest, @AuthenticationPrincipal LoginUser loginUser) {
        return transactionService.appPayment(transactionSaveRequest, loginUser);
    }

    @ApiOperation(value = "Transaction refund", notes = "거래 취소")
    @PostMapping({"/api/v1/transactions/refund/{requestId}", "/web-api/v1/transactions/refund/{requestId}"})
    public TransactionRefundResponse TransactionRefund(@PathVariable String requestId) {
        return transactionService.transactionRefund(requestId);
    }

    @ApiOperation(value = "get Transaction Status", notes = "거래상태 조회")
    @GetMapping({"/api/v1/transactions/state/{requestId}", "/web-api/v1/transactions/state/{requestId}"})
    public TransactionStateResponse getTransactionState(@PathVariable String requestId) {
        return transactionService.getTransStateByRequestId(requestId);
    }

    @ApiOperation(value = "get Transaction detail", notes = "거래 상세정보 조회")
    @GetMapping({"/api/v1/transactions/items/{requestId}", "/web-api/v1/transactions/items/{requestId}"})
    public List<TransactionDetailResponse> getTransactionDetailById(@PathVariable String requestId) {
        return transactionDetailService.getDetailByRequestId(requestId);
    }

}
