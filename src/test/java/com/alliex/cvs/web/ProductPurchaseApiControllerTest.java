package com.alliex.cvs.web;


import com.alliex.cvs.testsupport.WithMockCustomUser;
import com.alliex.cvs.web.dto.ProductPurchaseSaveRequest;
import com.alliex.cvs.web.dto.ProductPurchaseUpdateRequest;
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

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
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
public class ProductPurchaseApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @WithMockCustomUser
    @Test
    public void createProductPurchase() throws Exception {
        // Given
        ProductPurchaseSaveRequest productPurchaseSaveRequest = getProductPurchaseRequest();

        mvc.perform(post("/web-api/v1/products-purchases")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productPurchaseSaveRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @WithMockCustomUser(username = "adminId Updated")
    @Test
    public void updateProductPurchase() throws Exception {
        final Long testId = 500L;
        // Given
        ProductPurchaseUpdateRequest productPurchaseUpdateRequest = new ProductPurchaseUpdateRequest();
        final Long purchaseAmount = 100L;
        final Integer purchaseQuantity = 10;
        final String adminId = "adminId Updated";
        final LocalDate purchaseDate = LocalDate.now();

        productPurchaseUpdateRequest.setPurchaseAmount(purchaseAmount);
        productPurchaseUpdateRequest.setPurchaseQuantity(purchaseQuantity);
        productPurchaseUpdateRequest.setPurchaseDate(purchaseDate);

        mvc.perform(post("/web-api/v1/products-purchases/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productPurchaseUpdateRequest)))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(get("/web-api/v1/products-purchases/{id}", testId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purchaseAmount").value(purchaseAmount))
                .andExpect(jsonPath("$.purchaseQuantity").value(purchaseQuantity))
                .andExpect(jsonPath("$.adminId").value(adminId))
                .andExpect(jsonPath("$.purchaseDate").value(purchaseDate.toString()));
    }

    @WithMockCustomUser
    @Test
    public void updateProductPurchase_ProductCategoryNotFound() throws Exception {
        final Long testId = 500L;
        // Given
        ProductPurchaseUpdateRequest productPurchaseUpdateRequest = new ProductPurchaseUpdateRequest();
        Long purchaseAmount = 100L;
        Integer purchaseQuantity = 10;
        String adminId = "adminId Updated";
        LocalDate purchaseDate = LocalDate.of(2000, 10, 10);

        productPurchaseUpdateRequest.setAdminId(adminId);
        productPurchaseUpdateRequest.setPurchaseAmount(purchaseAmount);
        productPurchaseUpdateRequest.setPurchaseQuantity(purchaseQuantity);
        productPurchaseUpdateRequest.setPurchaseDate(purchaseDate);

        mvc.perform(post("/web-api/v1/products-purchases/{id}", testId + 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productPurchaseUpdateRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("PRODUCT_PURCHASE_NOT_FOUND")));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getProductPurchases() throws Exception {
        mvc.perform(get("/web-api/v1/products-purchases"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getProductPurchasesWithPage() throws Exception {
        mvc.perform(get("/web-api/v1/products-purchases").param("page", "2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getProductPurchaseById() throws Exception {
        final Long testId = 500L;
        final Long purchaseAmount = 500L;
        final Integer purchaseQuantity = 50;

        mvc.perform(get("/web-api/v1/products-purchases/{id}", testId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purchaseAmount").value(purchaseAmount))
                .andExpect(jsonPath("$.purchaseQuantity").value(purchaseQuantity));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getProductPurchaseById_ProductPurchaseNotFound() throws Exception {
        mvc.perform(get("/web-api/v1/products-purchases/{id}", 44))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("PRODUCT_PURCHASE_NOT_FOUND")));
    }

    @WithMockCustomUser
    @Test
    public void getProductPurchaseByProductName() throws Exception {
        String testProductName = "testProduct";    //  productId:1 = testProduct
        String testSearchByProductNameLike = "tp";

        mvc.perform(get("/web-api/v1/products-purchases").param("productName", testSearchByProductNameLike))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].product.name").value(testProductName));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getProductPurchaseByCategoryName() throws Exception {
        String testCategoryName = "categorytest";   //  categoryId:1 = categorytest
        String testSearchByCategoryNameLike = "es";

        mvc.perform(get("/web-api/v1/products-purchases").param("categoryName", testSearchByCategoryNameLike))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].product.productCategory.name").value(testCategoryName));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getProductPurchaseByPurchaseDate() throws Exception {
        String testPurchaseDate = "2020-12-03";

        mvc.perform(get("/web-api/v1/products-purchases").param("purchaseDate", testPurchaseDate))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].purchaseDate").value(testPurchaseDate));
    }

    private ProductPurchaseSaveRequest getProductPurchaseRequest() {
        Long categoryId = 500L;
        Long productId = 500L;
        Long purchaseAmount = 100L;
        Integer purchaseQuantity = 10;
        String adminId = "testtest";
        LocalDate eight_format = LocalDate.now();
        LocalDateTime fourteen_format = LocalDateTime.now();

        // Create ProductCategory
        return ProductPurchaseSaveRequest.builder()
                .categoryId(categoryId)
                .productId(productId)
                .purchaseAmount(purchaseAmount)
                .purchaseQuantity(purchaseQuantity)
                .adminId(adminId)
                .purchaseDate(eight_format)
                .createdDate(fourteen_format).build();
    }

}
