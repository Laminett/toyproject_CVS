package com.alliex.cvs.repository;

import com.alliex.cvs.entity.ProductCategory;
import com.alliex.cvs.web.dto.ProductCategoryRequest;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.alliex.cvs.entity.QProductCategory.productCategory;

@Repository
public class ProductCategoryRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public ProductCategoryRepositorySupport(JPAQueryFactory queryFactory) {
        super(ProductCategory.class);
        this.queryFactory = queryFactory;
    }

    public Page<ProductCategory> findBySearchValue(Pageable pageable, ProductCategoryRequest productCategoryRequest) {
        QueryResults<ProductCategory> results = getQuerydsl().applyPagination(pageable, queryFactory
                .selectFrom(productCategory)
                .where(
                        categoryNameLike(productCategoryRequest.getCategoryName()),
                        isEnabledEq(productCategoryRequest.getIsEnabled())
                )).fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }


    private BooleanExpression categoryNameLike(String categoryName) {
        return StringUtils.isNotBlank(categoryName) ? productCategory.name.like("%" + categoryName + "%") : null;
    }

    private BooleanExpression isEnabledEq(Boolean isEnabled) {
        return isEnabled != null ? productCategory.isEnabled.eq(isEnabled) : null;
    }
}
