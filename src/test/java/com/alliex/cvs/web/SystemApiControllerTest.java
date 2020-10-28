package com.alliex.cvs.web;

import com.alliex.cvs.domain.settle.Settle;
import com.alliex.cvs.domain.settle.SettleRepository;
import com.alliex.cvs.web.dto.SystemBatchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
public class SystemApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private SettleRepository settleRepository;

    @WithMockUser(roles = "ADMIN")
    @Test
    public void runSettleBatch() throws Exception {
        List<Settle> settleListBefore = settleRepository.findAll();
        System.out.println("settleListBefore : ");
        for (Settle obj : settleListBefore) {
            System.out.println(obj.getId());
        }

        mvc.perform(put("/web-api/v1/system/batch/manual/settle")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getSystemBatchRequest())))
                .andDo(print())
                .andExpect(status().isOk());

        List<Settle> settleListAfter = settleRepository.findAll();
        System.out.println("settleListAfter : ");
        for (Settle obj : settleListAfter) {
            System.out.println(obj.getId());
        }
    }

    private SystemBatchRequest getSystemBatchRequest() {
        return SystemBatchRequest.builder()
                .aggregatedAt("20200912")
                .build();
    }

}