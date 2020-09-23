package com.alliex.cvs.web;

import com.alliex.cvs.domain.type.Role;
import com.alliex.cvs.web.dto.UserSaveRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional()
@Rollback()
public class UserApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @WithMockUser(roles = "USER")
    @Test
    public void createUser() throws Exception {
        String username = "test " + RandomStringUtils.randomAlphanumeric(5);
        String password = "password";
        String department = "department";
        String fullName = "fullName " + RandomStringUtils.randomAlphanumeric(5);
        String email = RandomStringUtils.randomAlphanumeric(5) + "@email.com";
        String phoneNumber = "010-1111-2222";
        Role role = Role.ADMIN;

        // Create user.
        UserSaveRequest userSaveRequest = UserSaveRequest.builder()
                .username(username)
                .password(password)
                .department(department)
                .fullName(fullName)
                .email(email)
                .phoneNumber(phoneNumber)
                .role(role)
                .build();

        mvc.perform(post("/web-api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSaveRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @WithMockUser(roles = "USER")
    @Test
    public void getUsersByDepartment() throws Exception {
        mvc.perform(get("/web-api/v1/users").param("department", "Mobile Div"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].department").value("Mobile Div"));
    }

    @WithMockUser(roles = "USER")
    @Test
    public void getUsersByFullName() throws Exception {
        mvc.perform(get("/web-api/v1/users").param("fullName", "admin"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].fullName").value("admin"));
    }

    @WithMockUser(roles = "USER")
    @Test
    public void getUsers() throws Exception {
        mvc.perform(get("/web-api/v1/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @Test
    public void getUsersWithPage() throws Exception {
        mvc.perform(get("/web-api/v1/users").param("page", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].fullName").value("admin"));
    }

}