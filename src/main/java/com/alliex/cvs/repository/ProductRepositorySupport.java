package com.alliex.cvs.repository;

import com.alliex.cvs.entity.Product;
import com.alliex.cvs.web.dto.ProductRequest;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.alliex.cvs.entity.QProduct.product;

@Repository
public class ProductRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public ProductRepositorySupport(JPAQueryFactory queryFactory) {
        super(Product.class);
        this.queryFactory = queryFactory;
    }

    public Page<Product> findBySearchValue(Pageable pageable, ProductRequest productRequest) {
        QueryResults<Product> results = getQuerydsl().applyPagination(pageable, queryFactory
                .selectFrom(product)
                .where(
                        nameLike(productRequest.getName()),
                        categoryNameLike(productRequest.getCategoryName()),
                        isEnabledEq(productRequest.getIsEnabled())
                )).fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanExpression nameLike(String name) {
        return StringUtils.isNotBlank(name) ? product.name.like("%" + name + "%") : null;
    }

    private BooleanExpression categoryNameLike(String categoryName) {
        return StringUtils.isNotBlank(categoryName) ? product.productCategory.name.like("%" + categoryName + "%") : null;
    }

    private BooleanExpression isEnabledEq(Boolean isEnabled) {
        return isEnabled != null ? product.isEnabled.eq(isEnabled) : null;
    }

}
