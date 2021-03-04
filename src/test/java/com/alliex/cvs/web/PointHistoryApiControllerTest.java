package com.alliex.cvs.web;

import com.alliex.cvs.domain.type.PointHistoryStatus;
import com.alliex.cvs.web.dto.PointHistoryUpdateRequest;
import com.alliex.cvs.web.dto.PointHistorySaveRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
public class PointHistoryApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    private final Long testId = 90L;
    private final Long testNotExistId = 9999L;
    private final Long testUserId = 400L;

    @WithMockUser(roles = "USER")
    @Test
    public void chargeRequest() throws Exception {
        PointHistorySaveRequest pointHistorySaveRequest = getPointHistorySaveRequest(10000L);

        mvc.perform(post("/api/v1/point/history")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pointHistorySaveRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(400))
                .andExpect(jsonPath("$.point").value(10000));
    }

    @WithMockUser(roles = "USER")
    @Test
    public void chargeRequest_PointTooMuch() throws Exception {
        PointHistorySaveRequest pointHistorySaveRequest = getPointHistorySaveRequest(999999999L);

        mvc.perform(post("/api/v1/point/history")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pointHistorySaveRequest)))
                .andDo(print())
                .andExpect(jsonPath("$.code", is("POINT_LIMIT_EXCESS")));
    }

    @WithMockUser(roles = "USER")
    @Test
    public void chargeRequest_AlreadyExists() throws Exception {
        PointHistorySaveRequest pointHistorySaveRequest = getPointHistorySaveRequest(2000L);

        mvc.perform(post("/api/v1/point/history")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pointHistorySaveRequest)));

        mvc.perform(post("/api/v1/point/history")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pointHistorySaveRequest)))
                .andDo(print())
                .andExpect(jsonPath("$.code", is("POINT_HISTORY_PROGRESS_ALREADY_EXISTS")));
    }

    private PointHistorySaveRequest getPointHistorySaveRequest(Long point) {
        return PointHistorySaveRequest.builder()
                .userId(testUserId)
                .point(point)
                .build();
    }

    @WithMockUser(roles = "USER")
    @Test
    public void searchRequest() throws Exception {
        mvc.perform(get("/api/v1/point/history/progress/users/{id}", testUserId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(400))
                .andExpect(jsonPath("$.point").value(0));
    }

    @WithMockUser(roles = "USER")
    @Test
    public void searchRequest_Exists() throws Exception {
        PointHistorySaveRequest pointHistorySaveRequest = getPointHistorySaveRequest(10000L);

        mvc.perform(post("/api/v1/point/history")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pointHistorySaveRequest)))
                .andDo(print());

        mvc.perform(get("/api/v1/point/history/progress/users/{id}", testUserId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(400))
                .andExpect(jsonPath("$.point").value(10000));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getPointHistoryWithPage() throws Exception {
        mvc.perform(get("/web-api/v1/point/history").param("page", "2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void updatePointHistory() throws Exception {
        // given
        PointHistoryUpdateRequest pointHistoryUpdateRequest = getPointHistoryUpdateRequest();

        mvc.perform(put("/web-api/v1/point/history/{id}", testId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pointHistoryUpdateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Long.toString(testId)));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void updatePointHistory_HistoryNotFound() throws Exception {
        // given
        PointHistoryUpdateRequest pointHistoryUpdateRequest = getPointHistoryUpdateRequest();

        mvc.perform(put("/web-api/v1/point/history/{id}", testNotExistId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pointHistoryUpdateRequest)))
                .andDo(print())
                .andExpect(jsonPath("$.code", is("POINT_HISTORY_NOTFOUND")));
    }

    private PointHistoryUpdateRequest getPointHistoryUpdateRequest() {
        return PointHistoryUpdateRequest.builder()
                .status(PointHistoryStatus.APPROVE)
                .adminId("testAdmin")
                .build();
    }

}