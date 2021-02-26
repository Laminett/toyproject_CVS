package com.alliex.cvs.repository;

import com.alliex.cvs.entity.ProductPurchase;
import com.alliex.cvs.web.dto.ProductPurchaseRequest;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import static com.alliex.cvs.entity.QProductPurchase.productPurchase;

@Repository
public class ProductPurchaseRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public ProductPurchaseRepositorySupport(JPAQueryFactory queryFactory) {
        super(ProductPurchase.class);
        this.queryFactory = queryFactory;
    }

    public Page<ProductPurchase> findBySearchValue(Pageable pageable, ProductPurchaseRequest productPurchaseRequest) {
        QueryResults<ProductPurchase> results = getQuerydsl().applyPagination(pageable, queryFactory
                .selectFrom(productPurchase)
                .where(
                        productNameLike(productPurchaseRequest.getProductName()),
                        categoryNameLike(productPurchaseRequest.getCategoryName()),
                        purchaseDateEq(productPurchaseRequest.getPurchaseDate())
                )).fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanExpression productNameLike(String productName) {
        return StringUtils.isNotBlank(productName) ? productPurchase.product.name.like("%" + productName + "%") : null;
    }

    private BooleanExpression categoryNameLike(String categoryName) {
        return StringUtils.isNotBlank(categoryName) ? productPurchase.productCategory.name.like("%" + categoryName + "%") : null;
    }

    private BooleanExpression purchaseDateEq(LocalDate purchaseDate) {
        return purchaseDate != null ? productPurchase.purchaseDate.eq(purchaseDate) : null;
    }

}
