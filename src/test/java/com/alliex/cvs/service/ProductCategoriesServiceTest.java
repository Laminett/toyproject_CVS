package com.alliex.cvs.service;

import com.alliex.cvs.web.dto.ProductCategoryRequest;
import com.alliex.cvs.web.dto.ProductCategoryResponse;
import com.alliex.cvs.web.dto.ProductCategoryUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional()
@Rollback()
public class ProductCategoriesServiceTest {

    @Autowired
    ProductCategoriesService productCategoriesService;

    @Test
    public void getProductCategories(){
        ProductCategoryRequest productCategoryRequest = null;
        List<ProductCategoryResponse> category = productCategoriesService.getCategories(PageRequest.of(0,10));
        assertThat(category.size()).isGreaterThanOrEqualTo(1);

    }

    @Test
    public void updateCategory(){
        // Update category.
        ProductCategoryUpdateRequest productCategoryUpdateRequest = new ProductCategoryUpdateRequest();
        productCategoryUpdateRequest.setCategoryName("categoryName updated");
        productCategoryUpdateRequest.setIsEnabled(true);
        productCategoryUpdateRequest.setAdminId("modifiedId updated");
        Long updatedId = productCategoriesService.update(2L, productCategoryUpdateRequest);

        // Get category
        ProductCategoryResponse productCategoryResponse = productCategoriesService.getCategoryById(updatedId);
        assertThat(productCategoryResponse.getName()).isEqualTo("categoryName updated");
        assertThat(productCategoryResponse.getIsEnabled()).isEqualTo(true);
        assertThat(productCategoryResponse.getAdminId()).isEqualTo("modifiedId updated");
    }


}
