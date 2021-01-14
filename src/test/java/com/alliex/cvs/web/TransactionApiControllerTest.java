package com.alliex.cvs.web;

import com.alliex.cvs.domain.type.TransactionState;
import com.alliex.cvs.domain.type.TransactionType;
import com.alliex.cvs.web.dto.TransactionSaveRequest;
import com.alliex.cvs.web.dto.TransactionStateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String requestIdTest;

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
        mvc.perform(get("/web-api/v1/transactions/{id}", testId))
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

    @WithMockUser(roles = "ADMIN")
    @Test
    public void QRPaymentStep1() throws Exception {
        TransactionSaveRequest transactionSaveRequest = new TransactionSaveRequest();
        Long merchantId = 123L;
        Long point = 333L;

        List<Map<String, String>> detailList = new ArrayList<>();

        for(int i = 0; i < 3; i++) {
            Map<String, String> transactionDetail = new HashMap<>();
            transactionDetail.put("productId", "50"+i);
            transactionDetail.put("productAmount", "10"+i);
            transactionDetail.put("productPoint", "20"+i);

            detailList.add(transactionDetail);
        }

        transactionSaveRequest.setMerchantId(merchantId);
        transactionSaveRequest.setPoint(point);
        transactionSaveRequest.setTransProduct(detailList);

        MvcResult result =
        mvc.perform(post("/web-api/v1/transactions/payment/QRstep1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionSaveRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        TransactionStateResponse transactionStateResponse = objectMapper.readValue(content, TransactionStateResponse.class);
        requestIdTest = transactionStateResponse.getRequestId();

        mvc.perform(get("/web-api/v1/transactions/state/{requestId}", requestIdTest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionState").value(String.valueOf(TransactionState.WAIT)))
                .andExpect(jsonPath("$.requestId").value(requestIdTest));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void QRPaymentStep2() throws Exception {
        TransactionSaveRequest transactionSaveRequest = new TransactionSaveRequest();
        Long userId = 400L;
        String requestId = "BBBBBBBBBBbbbbbbbbbb";

        transactionSaveRequest.setUserId(userId);

        mvc.perform(put("/web-api/v1/transactions/payment/QRstep2/{requestId}", requestId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionSaveRequest)))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(get("/web-api/v1/transactions/state/{requestId}", requestId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionState").value(String.valueOf(TransactionState.SUCCESS)))
                .andExpect(jsonPath("$.requestId").value(requestId));
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
        final long testId = 500L;
        String requestId = "AAAAAAAAAAaaaaaaaaaa";

        MvcResult result =
        mvc.perform(post("/web-api/v1/transactions/refund/{transId}", testId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Long resultTransactionId = objectMapper.readValue(content, Long.class);

        mvc.perform(get("/web-api/v1/transactions/{id}", resultTransactionId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(String.valueOf(TransactionState.REFUND)))
                .andExpect(jsonPath("$.type").value(String.valueOf(TransactionType.REFUND)))
                .andExpect(jsonPath("$.originId").value(testId))
                .andExpect(jsonPath("$.requestId").value(requestId));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getTransactionDetailById() throws Exception{
        final long testId = 500L;
        mvc.perform(get("/web-api/v1/transactions/items/{transId}", testId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(500))
                .andExpect(jsonPath("$[1].productId").value(501))
                .andExpect(jsonPath("$[2].productId").value(502));
    }

}
