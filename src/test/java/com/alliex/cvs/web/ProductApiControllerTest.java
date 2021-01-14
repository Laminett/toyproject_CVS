package com.alliex.cvs.web;

import com.alliex.cvs.testsupport.WithMockCustomUser;
import com.alliex.cvs.web.dto.ProductSaveRequest;
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
import org.springframework.transaction.annotation.Transactional;

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
public class ProductApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @WithMockCustomUser
    @Test
    public void createProduct() throws Exception {
        // Given
        ProductSaveRequest productSaveRequest = getProductRequest();

        mvc.perform(post("/web-api/v1/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productSaveRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @WithMockCustomUser
    @Test
    public void createProduct_AlreadyExist() throws Exception {
        // Given
        ProductSaveRequest productSaveRequest = getProductRequest();

        // Create
        mvc.perform(post("/web-api/v1/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productSaveRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

        // Create One More
        mvc.perform(post("/web-api/v1/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productSaveRequest)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is("PRODUCT_ALREADY_EXISTS")));
    }

    @WithMockCustomUser(username = "updatedAdminId")
    @Test
    public void updateProduct() throws Exception {
        final Long testId = 500L;

        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest();
        Long categoryId = 500L;
        String name = "name updated";
        Long point = 100000L;
        Integer quantity = 100000;
        String adminId = "updatedAdminId";
        Boolean isEnabled = false;

        productUpdateRequest.setCategoryId(categoryId);
        productUpdateRequest.setName(name);
        productUpdateRequest.setPoint(point);
        productUpdateRequest.setQuantity(quantity);
        productUpdateRequest.setAdminId(adminId);
        productUpdateRequest.setIsEnabled(isEnabled);

        mvc.perform(post("/web-api/v1/products/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productUpdateRequest)))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(get("/web-api/v1/products/{id}", testId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId.id").value(categoryId))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.point").value(point))
                .andExpect(jsonPath("$.quantity").value(quantity))
                .andExpect(jsonPath("$.adminId").value(adminId))
                .andExpect(jsonPath("$.isEnabled").value(isEnabled));
    }

    @WithMockCustomUser
    @Test
    public void updateProduct_ProductNotFound() throws Exception {
        final Long testId = 500L;

        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest();
        Long categoryId = 500L;
        String name = "name updated";
        Long point = 100000L;
        Integer quantity = 100000;
        String adminId = "updatedAdminId";
        Boolean isEnabled = false;

        productUpdateRequest.setCategoryId(categoryId);
        productUpdateRequest.setName(name);
        productUpdateRequest.setPoint(point);
        productUpdateRequest.setQuantity(quantity);
        productUpdateRequest.setAdminId(adminId);
        productUpdateRequest.setIsEnabled(isEnabled);

        mvc.perform(post("/web-api/v1/products/{id}", testId + 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productUpdateRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("PRODUCT_NOT_FOUND")));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getProducts() throws Exception {
        mvc.perform(get("/web-api/v1/products"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getProductsWithPage() throws Exception {
        mvc.perform(get("/web-api/v1/products").param("page", "2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getProductById() throws Exception {
        final long testId = 500L;
        String barcode = "123123123123";

        mvc.perform(get("/web-api/v1/products/{id}", testId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.barcode").value(barcode));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getProductById_ProductNotFound() throws Exception {
        mvc.perform(get("/web-api/v1/products/{id}", 44))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("PRODUCT_NOT_FOUND")));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void ScanProduct() throws Exception {
        final Long testId = 500L;
        final String name = "testProduct";
        final String barcode = "123123123123";
        final Boolean isEnabled= true;
        final Long point = 100L;
        final Integer quantity = 200;
        final Long categoryId = 500L;
        final String adminId = "testAdmin";

        mvc.perform(get("/web-api/v1/barcodescan/{barcode}", barcode))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.isEnabled").value(isEnabled))
                .andExpect(jsonPath("$.point").value(point))
                .andExpect(jsonPath("$.quantity").value(quantity))
                .andExpect(jsonPath("$.categoryId.id").value(categoryId))
                .andExpect(jsonPath("$.adminId").value(adminId))
                .andExpect(jsonPath("$.barcode").value(barcode));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void ScanProduct_ProductNotFound() throws Exception {
        final String barcode = "987654321123";

        mvc.perform(get("/web-api/v1/barcodescan/{barcode}", barcode))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("PRODUCT_NOT_FOUND")));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getProductByName() throws Exception {
        String testName = "testProduct";
        String testSearchByNameLike = "ro";

        mvc.perform(get("/web-api/v1/products").param("name", testSearchByNameLike))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(testName));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getProductByCategoryName() throws Exception {
        String testCategoyName1 = "categorytest";
        String testName = "testProduct";
        String testSearchByCategoryNameLike = "te";

        mvc.perform(get("/web-api/v1/products").param("categoryName", testSearchByCategoryNameLike))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].categoryId.name").value(testCategoyName1))
                .andExpect(jsonPath("$.content[0].name").value(testName));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getProductByIsEnabled() throws Exception {
        Boolean testIsEnabled = true;
        String testName = "testProduct";

        mvc.perform(get("/web-api/v1/products").param("isEnabled", String.valueOf(true)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(testName))
                .andExpect(jsonPath("$.content[0].isEnabled").value(true));
    }

    private ProductSaveRequest getProductRequest() {
        Long categoryId = 500L;
        String name = "productTestName";
        String barcode = "147258369123";
        Boolean isEnabled = true;
        Long point = 5000L;
        Integer quantity = 500;

        return ProductSaveRequest.builder()
                .categoryId(categoryId)
                .name(name)
                .barcode(barcode)
                .isEnabled(isEnabled)
                .point(point)
                .quantity(quantity).build();
    }

}
