package com.alliex.cvs.web;

import com.alliex.cvs.web.dto.ProductCategorySaveRequest;
import com.alliex.cvs.web.dto.ProductCategoryUpdateRequest;
import com.alliex.cvs.web.dto.ProductUpdateRequest;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional()
@Rollback()
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-data.sql"),
})
public class ProductCategoryApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    private final Long testId = 500L;
    private final String testCategoryName = "categorytest";
    private final Boolean testIsEnabled = true;
    private final String testAdminId = "testid";

    @WithMockUser(roles = "ADMIN")
    @Test
    public void createProductCategory() throws Exception {
        // Given
        ProductCategorySaveRequest productCategorySaveRequest = getProductCategoryRequest();

        mvc.perform(post("/web-api/v1/products-categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productCategorySaveRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void createProductCategory_ProductCategoryAlreadyExists() throws Exception {
        // Given
        ProductCategorySaveRequest productCategorySaveRequest = getProductCategoryRequest();

        // Create
        mvc.perform(post("/web-api/v1/products-categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productCategorySaveRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

        // Create One More
        mvc.perform(post("/web-api/v1/products-categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productCategorySaveRequest)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is("PRODUCT_CATEGORY_ALREADY_EXISTS")));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void updateProductCategory() throws Exception {
        // Given
        ProductCategoryUpdateRequest productCategoryUpdateRequest = new ProductCategoryUpdateRequest();
        String categoryName = "categoryName updated";
        productCategoryUpdateRequest.setCategoryName(categoryName);

        mvc.perform(put("/web-api/v1/products-categories/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productCategoryUpdateRequest)))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(get("/web-api/v1/products-categories/{id}", testId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(categoryName));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void updateProductCategory_ProductCategoryNotFound() throws Exception {
        // Given
        ProductCategoryUpdateRequest productCategoryUpdateRequest = new ProductCategoryUpdateRequest();
        String categoryName = "categoryName updated";
        productCategoryUpdateRequest.setCategoryName(categoryName);

        mvc.perform(put("/web-api/v1/products-categories/{id}", testId + 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productCategoryUpdateRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("PRODUCT_CATEGORY_NOT_FOUND")));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getProductCategories() throws Exception {
        mvc.perform(get("/web-api/v1/products-categories"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getProductCategoriesWithPage() throws Exception {
        mvc.perform(get("/web-api/v1/products-categories").param("page", "2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getProductCategoryById() throws Exception {
        mvc.perform(get("/web-api/v1/products-categories/{id}", testId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testCategoryName));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getProductCategoryById_ProductCategoryNotFound() throws Exception {
        mvc.perform(get("/web-api/v1/products-categories/{id}", 44))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("PRODUCT_CATEGORY_NOT_FOUND")));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getProductCategoryByIsEnabled() throws Exception {
        mvc.perform(get("/web-api/v1/products-categories").param("isEnabled", String.valueOf(testIsEnabled)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].isEnabled").value(testIsEnabled));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getProductCategoryByAdminId() throws Exception {
        mvc.perform(get("/web-api/v1/products-categories").param("adminId", testAdminId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].adminId").value(testAdminId));
    }

    private ProductCategorySaveRequest getProductCategoryRequest() {
        String categoryName = "categoryTest_test";
        Boolean isEnabled = false;
        String adminId = "testtest";

        // Create ProductCategory
        return ProductCategorySaveRequest.builder()
                .categoryName(categoryName)
                .isEnabled(isEnabled)
                .adminId(adminId)
                .build();
    }

}
