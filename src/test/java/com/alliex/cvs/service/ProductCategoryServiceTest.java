package com.alliex.cvs.service;

import com.alliex.cvs.web.dto.ProductCategoryRequest;
import com.alliex.cvs.web.dto.ProductCategoryResponse;
import com.alliex.cvs.web.dto.ProductCategoryUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional()
@Rollback()
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-data.sql"),
})
public class ProductCategoryServiceTest {

    @Autowired
    ProductCategoryService productCategoryService;

    @Test
    public void getProductCategories() {
        ProductCategoryRequest productCategoryRequest = null;
        List<ProductCategoryResponse> category = productCategoryService.getCategories(PageRequest.of(0, 10));
        assertThat(category.size()).isGreaterThanOrEqualTo(1);

    }

    @Test
    public void updateCategory() {
        // Update category.
        ProductCategoryUpdateRequest productCategoryUpdateRequest = new ProductCategoryUpdateRequest();
        productCategoryUpdateRequest.setCategoryName("categoryName updated");
        productCategoryUpdateRequest.setIsEnabled(true);
        productCategoryUpdateRequest.setAdminId("modifiedId updated");
        Long updatedId = productCategoryService.update(500L, productCategoryUpdateRequest);

        // Get category
        ProductCategoryResponse productCategoryResponse = productCategoryService.getCategoryById(updatedId);
        assertThat(productCategoryResponse.getName()).isEqualTo("categoryName updated");
        assertThat(productCategoryResponse.getIsEnabled()).isEqualTo(true);
        assertThat(productCategoryResponse.getAdminId()).isEqualTo("modifiedId updated");
    }

}
