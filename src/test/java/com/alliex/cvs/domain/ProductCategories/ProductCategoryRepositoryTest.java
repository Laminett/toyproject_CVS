package com.alliex.cvs.domain.ProductCategories;

import com.alliex.cvs.domain.product.category.ProductCategory;
import com.alliex.cvs.domain.product.category.ProductCategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional()
@Rollback()
public class ProductCategoryRepositoryTest {

    @Autowired
    ProductCategoryRepository productCategoryRepository;

    @Test
    public void getProductCategories() {
        String name = "test category";
        Boolean isEnabled = true;
        String createdId = "ykk";

        productCategoryRepository.save(ProductCategory.builder()
                .name(name)
                .isEnabled(isEnabled)
                .adminId(createdId)
                .build());

        //when
        List<ProductCategory> categories = productCategoryRepository.findAll();

        //then
        ProductCategory productCategory = categories.get(0);
        assertThat(productCategory.getName()).isEqualTo(name);
        assertThat(productCategory.getEnabled()).isEqualTo(isEnabled);
        assertThat(productCategory.getAdminId()).isEqualTo(createdId);
    }
}
