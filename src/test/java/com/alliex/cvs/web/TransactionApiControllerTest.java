package com.alliex.cvs.web;

import com.alliex.cvs.domain.type.PaymentType;
import com.alliex.cvs.domain.type.TransactionState;
import com.alliex.cvs.domain.type.TransactionType;
import com.alliex.cvs.testsupport.WithMockCustomUser;
import com.alliex.cvs.web.dto.TransactionDetailSaveRequest;
import com.alliex.cvs.web.dto.TransactionSaveRequest;
import com.alliex.cvs.web.dto.TransactionStateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional()
@Rollback()
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-data.sql"),
})
public class TransactionApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getTransactions() throws Exception {
        mvc.perform(get("/web-api/v1/transactions"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getTransactionById() throws Exception {
        final long testId = 500L;
        String requestId = "AAAAAAAAAAaaaaaaaaaa";
        mvc.perform(get("/web-api/v1/transactions/{requestId}", requestId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value(requestId));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getTransactionById_TransactionNotFound() throws Exception {
        mvc.perform(get("/web-api/v1/transactions/{id}", 601))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("TRANSACTION_NOT_FOUND"));
    }

    @WithMockCustomUser
    @Test
    public void paymentFromPos() throws Exception {
        List<TransactionDetailSaveRequest> detailList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            TransactionDetailSaveRequest transactionDetail =
                    new TransactionDetailSaveRequest(10 + i, 500L + i, null);

            detailList.add(transactionDetail);
        }

        MvcResult result =
                mvc.perform(post("/web-api/v1/transactions/payment/pos/step1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(detailList)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();

        String content = result.getResponse().getContentAsString();
        TransactionStateResponse transactionStateResponse = objectMapper.readValue(content, TransactionStateResponse.class);
        String requestIdTest = transactionStateResponse.getRequestId();

        mvc.perform(get("/web-api/v1/transactions/items/{requestIdTest}", requestIdTest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].requestId").value(requestIdTest))
                .andExpect(jsonPath("$[1].requestId").value(requestIdTest))
                .andExpect(jsonPath("$[2].requestId").value(requestIdTest));

        TransactionSaveRequest transactionSaveRequest = new TransactionSaveRequest();
        transactionSaveRequest.setPaymentType(PaymentType.POS_QR);
        transactionSaveRequest.setRequestId(requestIdTest);

        mvc.perform(put("/web-api/v1/transactions/payment/pos/step2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionSaveRequest)))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(get("/web-api/v1/transactions/state/{requestId}", requestIdTest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value(requestIdTest));
    }

    @WithMockCustomUser(username = "testtest100")
    @Test
    public void paymentFromPos_NotEnoughPoint() throws Exception {
        List<TransactionDetailSaveRequest> detailList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            TransactionDetailSaveRequest transactionDetail =
                    new TransactionDetailSaveRequest(10 + i, 500L + i, null);

            detailList.add(transactionDetail);
        }

        MvcResult result =
                mvc.perform(post("/web-api/v1/transactions/payment/pos/step1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(detailList)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();

        String content = result.getResponse().getContentAsString();
        TransactionStateResponse transactionStateResponse = objectMapper.readValue(content, TransactionStateResponse.class);
        String requestIdTest = transactionStateResponse.getRequestId();

        TransactionSaveRequest transactionSaveRequest = new TransactionSaveRequest();
        transactionSaveRequest.setPaymentType(PaymentType.POS_QR);
        transactionSaveRequest.setRequestId(requestIdTest);

        mvc.perform(put("/web-api/v1/transactions/payment/pos/step2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionSaveRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("NOT_ENOUGH_POINT"));
    }

    @WithMockCustomUser(username = "cvstest")
    @Test
    public void paymentFromPos_AccessDenied() throws Exception {
        List<TransactionDetailSaveRequest> detailList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            TransactionDetailSaveRequest transactionDetail =
                    new TransactionDetailSaveRequest(10 + i, 500L + i, null);

            detailList.add(transactionDetail);
        }

        MvcResult result =
                mvc.perform(post("/web-api/v1/transactions/payment/pos/step1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(detailList)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();

        String content = result.getResponse().getContentAsString();
        TransactionStateResponse transactionStateResponse = objectMapper.readValue(content, TransactionStateResponse.class);
        String requestIdTest = transactionStateResponse.getRequestId();

        TransactionSaveRequest transactionSaveRequest = new TransactionSaveRequest();
        transactionSaveRequest.setPaymentType(PaymentType.POS_QR);
        transactionSaveRequest.setRequestId(requestIdTest);

        mvc.perform(put("/web-api/v1/transactions/payment/pos/step2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionSaveRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("ACCESS_DENIED"));
    }

    @WithMockCustomUser
    @Test
    public void paymentFromApp() throws Exception {
        String requestId = RandomStringUtils.randomAlphanumeric(20);
        TransactionSaveRequest transactionSaveRequest = new TransactionSaveRequest();
        transactionSaveRequest.setPaymentType(PaymentType.MOBILE);
        transactionSaveRequest.setRequestId(requestId);
        List<TransactionDetailSaveRequest> detailList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            TransactionDetailSaveRequest transactionDetail =
                    new TransactionDetailSaveRequest(10 + i, 500L + i, null);

            detailList.add(transactionDetail);
        }

        transactionSaveRequest.setTransProduct(detailList);

        mvc.perform(post("/web-api/v1/transactions/payment/app")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionSaveRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value(requestId))
                .andExpect(jsonPath("$.transactionState").value(String.valueOf(TransactionState.SUCCESS)));
    }

    @WithMockCustomUser(username = "testtest100")
    @Test
    public void paymentFromApp_NotEnoughPoint() throws Exception {
        String requestId = RandomStringUtils.randomAlphanumeric(20);
        TransactionSaveRequest transactionSaveRequest = new TransactionSaveRequest();
        transactionSaveRequest.setPaymentType(PaymentType.MOBILE);
        transactionSaveRequest.setRequestId(requestId);
        List<TransactionDetailSaveRequest> detailList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            TransactionDetailSaveRequest transactionDetail =
                    new TransactionDetailSaveRequest(10 + i, 500L + i, null);

            detailList.add(transactionDetail);
        }

        transactionSaveRequest.setTransProduct(detailList);

        mvc.perform(post("/web-api/v1/transactions/payment/app")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionSaveRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("NOT_ENOUGH_POINT"));
    }

    @WithMockCustomUser(username = "cvstest")
    @Test
    public void paymentFromApp_AccessDenied() throws Exception {
        String requestId = RandomStringUtils.randomAlphanumeric(20);
        TransactionSaveRequest transactionSaveRequest = new TransactionSaveRequest();
        transactionSaveRequest.setPaymentType(PaymentType.MOBILE);
        transactionSaveRequest.setRequestId(requestId);
        List<TransactionDetailSaveRequest> detailList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            TransactionDetailSaveRequest transactionDetail =
                    new TransactionDetailSaveRequest(10 + i, 500L + i, null);

            detailList.add(transactionDetail);
        }

        transactionSaveRequest.setTransProduct(detailList);

        mvc.perform(post("/web-api/v1/transactions/payment/app")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionSaveRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("ACCESS_DENIED"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getTransactionState() throws Exception {
        String requestId = "AAAAAAAAAAaaaaaaaaaa";
        mvc.perform(get("/web-api/v1/transactions/state/{requestId}", requestId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionState").value(String.valueOf(TransactionState.SUCCESS)))
                .andExpect(jsonPath("$.requestId").value(requestId));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void TransactionRefund() throws Exception {
        String requestId = "AAAAAAAAAAaaaaaaaaaa";

        MvcResult result =
                mvc.perform(post("/web-api/v1/transactions/refund/{requestId}", requestId))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();

        String content = result.getResponse().getContentAsString();
        TransactionStateResponse resultTransactionId = objectMapper.readValue(content, TransactionStateResponse.class);

        mvc.perform(get("/web-api/v1/transactions/{requestId}", resultTransactionId.getRequestId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(String.valueOf(TransactionState.REFUND)))
                .andExpect(jsonPath("$.type").value(String.valueOf(TransactionType.REFUND)))
                .andExpect(jsonPath("$.originRequestId").value(requestId))
                .andExpect(jsonPath("$.requestId").value(resultTransactionId.getRequestId()));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void TransactionRefund_AlreadyRefund() throws Exception {
        String requestId = "AAAAAAAAAAaaaaaaaaaa";

        mvc.perform(post("/web-api/v1/transactions/refund/{requestId}", requestId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        mvc.perform(post("/web-api/v1/transactions/refund/{requestId}", requestId))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("TRANSACTION_ALREADY_REFUNDED"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getTransactionDetailById() throws Exception {
        final String requestId = "AAAAAAAAAAaaaaaaaaaa";
        mvc.perform(get("/web-api/v1/transactions/items/{requestId}", requestId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(500))
                .andExpect(jsonPath("$[1].productId").value(501))
                .andExpect(jsonPath("$[2].productId").value(502));
    }

}
