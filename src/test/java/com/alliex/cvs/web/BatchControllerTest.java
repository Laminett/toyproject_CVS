package com.alliex.cvs.web;

import com.alliex.cvs.domain.settle.Settle;
import com.alliex.cvs.domain.settle.SettleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional()
@Rollback()
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-data.sql"),
})
public class BatchControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private SettleRepository settleRepository;

    private final String aggregatedAt = "20200901";

    @WithMockUser(roles = "ADMIN")
    @Test
    public void runSettleBatch() throws Exception {
        List<Settle> settleListBefore = settleRepository.findAll();
        int listCount = settleListBefore.size();

        MvcResult result = this.mvc.perform(post("/web-api/v1/system/batch/manual/settle/{aggregatedAt}", aggregatedAt)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        int insertedListCount = Integer.parseInt(content);

        List<Settle> settleListAfter = settleRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        assertThat(settleListAfter.size()).isEqualTo(listCount + insertedListCount);

        LocalDate aggregatedAt = LocalDate.of(2020, 9, 1);
        List<Settle> resultList400 = settleRepository.findByUserIdAndAggregatedAt(400L, aggregatedAt);
        assertThat(resultList400.get(0).getApprovalAmount()).isEqualTo(1000);
        assertThat(resultList400.get(0).getApprovalCount()).isEqualTo(10);
        assertThat(resultList400.get(0).getCancelAmount()).isEqualTo(600);
        assertThat(resultList400.get(0).getCancelCount()).isEqualTo(6);
        assertThat(resultList400.get(0).getTotalAmount()).isEqualTo(400);
        assertThat(resultList400.get(0).getTotalCount()).isEqualTo(16);

        List<Settle> resultList401 = settleRepository.findByUserIdAndAggregatedAt(401L, aggregatedAt);
        assertThat(resultList401.get(0).getApprovalAmount()).isEqualTo(1300);
        assertThat(resultList401.get(0).getApprovalCount()).isEqualTo(10);
        assertThat(resultList401.get(0).getCancelAmount()).isEqualTo(700);
        assertThat(resultList401.get(0).getCancelCount()).isEqualTo(6);
        assertThat(resultList401.get(0).getTotalAmount()).isEqualTo(600);
        assertThat(resultList401.get(0).getTotalCount()).isEqualTo(16);
    }

}