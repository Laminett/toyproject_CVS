package com.alliex.cvs.example;


import com.alliex.cvs.entity.Product;
import com.alliex.cvs.entity.ProductCategory;
import com.alliex.cvs.entity.QProduct;
import com.alliex.cvs.entity.QProductCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.alliex.cvs.entity.QProduct.product;
import static com.alliex.cvs.entity.QProductCategory.productCategory;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class QuerydslJoinTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);

        ProductCategory icecream = ProductCategory.builder().name("icecream").adminId("test").build();
        ProductCategory drink = ProductCategory.builder().name("drink").adminId("test").build();
        ProductCategory biscuit = ProductCategory.builder().name("biscuit").adminId("test").build();

        // add category
        em.persist(icecream);
        em.persist(drink);
        em.persist(biscuit);

        // add product
        em.persist(Product.builder()
                .productCategory(icecream)
                .name("메로나")
                .barcode("barcode1")
                .point(500L)
                .quantity(10)
                .isEnabled(true)
                .adminId("test")
                .build());

        em.persist(Product.builder()
                .productCategory(icecream)
                .name("누가바")
                .barcode("barcode2")
                .point(700L)
                .quantity(10)
                .isEnabled(true)
                .adminId("test")
                .build());

        em.persist(Product.builder()
                .productCategory(biscuit)
                .barcode("barcode3")
                .name("틴틴")
                .point(1000L)
                .quantity(10)
                .isEnabled(true)
                .adminId("test")
                .build());
    }

    @Test
    public void join() {
        List<Product> result = queryFactory
                .selectFrom(product)
                .join(product.productCategory, productCategory)
                .where(productCategory.name.eq("icecream"))
                .fetch();

        assertThat(result)
                .extracting("name")
                .containsExactly("메로나", "누가바");
    }

}
