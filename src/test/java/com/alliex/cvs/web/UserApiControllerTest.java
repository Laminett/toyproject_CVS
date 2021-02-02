package com.alliex.cvs.web;

import com.alliex.cvs.web.dto.UserSaveRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional()
@Rollback()
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-data.sql"),
})
public class UserApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    private final Long testId = 400L;
    private final String testFullName = "400_fullName";

    @WithMockUser(roles = "ADMIN")
    @Test
    public void createUser_WEB() throws Exception {
        // given
        UserSaveRequest userSaveRequest = getUserSaveRequest(RandomStringUtils.randomAlphanumeric(5), RandomStringUtils.randomAlphanumeric(5));

        mvc.perform(post("/web-api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSaveRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void createUser_APP() throws Exception {
        // given
        UserSaveRequest userSaveRequest = getUserSaveRequest(RandomStringUtils.randomAlphanumeric(5), RandomStringUtils.randomAlphanumeric(5));

        mvc.perform(post("/api/v1/create-users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSaveRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void createUser_APP_InvalidRequest() throws Exception {
        mvc.perform(post("/api/v1/create-users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUserSaveRequest("aabb", "password"))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));

        mvc.perform(post("/api/v1/create-users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUserSaveRequest("aabbc", "1234"))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));

        mvc.perform(post("/api/v1/create-users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUserSaveRequest("aabbc*", "1234"))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void createUser_UserAlreadyExists() throws Exception {
        // given
        UserSaveRequest userSaveRequest = getUserSaveRequest(RandomStringUtils.randomAlphanumeric(5), RandomStringUtils.randomAlphanumeric(5));

        // create
        mvc.perform(post("/web-api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSaveRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

        // create one more
        mvc.perform(post("/web-api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSaveRequest)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is("USER_ALREADY_EXISTS")));

    }

    private UserSaveRequest getUserSaveRequest(String username, String password) {
        String department = "department";
        String fullName = "fullName " + RandomStringUtils.randomAlphanumeric(5);
        String email = RandomStringUtils.randomAlphanumeric(5) + "@email.com";
        String phoneNumber = "010-1111-2222";

        // Create user.
        return UserSaveRequest.builder()
                .username(username)
                .password(password)
                .department(department)
                .fullName(fullName)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getUsersByDepartment() throws Exception {
        mvc.perform(get("/web-api/v1/users").param("department", "Mobile Div"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].department").value("Mobile Div"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getUsersByFullName() throws Exception {
        mvc.perform(get("/web-api/v1/users").param("fullName", testFullName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].fullName").value(testFullName));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getUsers() throws Exception {
        mvc.perform(get("/web-api/v1/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getUsersWithPage() throws Exception {
        mvc.perform(get("/web-api/v1/users").param("page", "2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getUserById() throws Exception {
        mvc.perform(get("/web-api/v1/users/{id}", testId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value(testFullName))
                .andExpect(jsonPath("$.point", is(notNullValue())));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getUserById_UserNotFound() throws Exception {
        long notExistsId = 9999L;

        mvc.perform(get("/web-api/v1/users/{id}", notExistsId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("USER_NOT_FOUND")));
    }

    @Test
    public void verifyPassword() throws Exception {
        String validPassword = "12345";
        mvc.perform(get("/api/v1/users/verify/pw/{password}", validPassword))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid", is(true)));

        String invalidPassword = "1234";
        mvc.perform(get("/api/v1/users/verify/pw/{password}", invalidPassword))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid", is(false)));
    }

    @Test
    public void verifyLoginId() throws Exception {
        String existLoginId = "forUnitTest";
        mvc.perform(get("/api/v1/users/verify/id/{username}", existLoginId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(existLoginId)));

        String notExistLoginId = "oops";
        mvc.perform(get("/api/v1/users/verify/id/{username}", notExistLoginId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("USER_NOT_FOUND")));
    }

}