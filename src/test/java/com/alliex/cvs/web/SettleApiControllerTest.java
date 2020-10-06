package com.alliex.cvs.web;

import com.alliex.cvs.web.dto.SettleUpdateRequest;
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

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional()
@Rollback()
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-data.sql"),
})
public class SettleApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    private final Long testId = 90L;
    private final Long testNotExistId = 9999L;

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getSettleWithPage() throws Exception {

        mvc.perform(get("/web-api/v1/settle").param("page", "2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void updateSettle() throws Exception {
        // given
        SettleUpdateRequest settleUpdateRequest = getSettleUpdateRequest();

        mvc.perform(put("/web-api/v1/settle/{id}", testId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(settleUpdateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Long.toString(testId)));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void updateSettle_SettleNotFound() throws Exception {
        // given
        SettleUpdateRequest settleUpdateRequest = getSettleUpdateRequest();

        mvc.perform(put("/web-api/v1/settle/{id}", testNotExistId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(settleUpdateRequest)))
                .andDo(print())
                .andExpect(jsonPath("$.code", is("SETTLE_NOT_FOUND")));
    }

    private SettleUpdateRequest getSettleUpdateRequest() {
        return SettleUpdateRequest.builder()
                .status("Y")
                .adminId("testAdmin")
                .build();
    }

}