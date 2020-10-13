package com.alliex.cvs.service;

import com.alliex.cvs.domain.product.category.ProductCategory;
import com.alliex.cvs.domain.product.category.ProductCategoryRepository;
import com.alliex.cvs.domain.type.ProductCategorySearchType;
import com.alliex.cvs.exception.*;
import com.alliex.cvs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductCategoriesService {

    private final ProductCategoryRepository productCategoryRepository;

    public Long save(ProductCategorySaveRequest productCategorySaveRequest) {
        // Check existence of a category.
        productCategoryRepository.findByName(productCategorySaveRequest.getCategoryName()).ifPresent(productCategory -> {
            throw new ProductCategoryAlreadyExistsException(productCategorySaveRequest.getCategoryName());
        });

        // Create category.
        return productCategoryRepository.save(productCategorySaveRequest.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, ProductCategoryUpdateRequest productCategoryUpdateRequest) {
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new ProductCategoryNotFoundException(id));

        productCategory.update(id, productCategoryUpdateRequest);

        return productCategory.getId();
    }

    @Transactional
    public void delete(Long id) {
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new ProductCategoryNotFoundException(id));

        productCategoryRepository.delete(productCategory);
    }

    public List<ProductCategoryResponse> getCategories(Pageable pageable) {
        return productCategoryRepository.findAll(pageable).stream()
                .map(ProductCategoryResponse::new)
                .collect(Collectors.toList());
    }

    public Page<ProductCategoryResponse> getCategories(Pageable pageable, ProductCategoryRequest productCategoryRequest) {
        return productCategoryRepository.findAll(searchWith(getPredicateData(productCategoryRequest)), pageable)
                .map(ProductCategoryResponse::new);
    }

    public ProductCategoryResponse getCategoryById(Long id) {
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new ProductCategoryNotFoundException(id));

        return new ProductCategoryResponse(productCategory);
    }


    private Specification<ProductCategory> searchWith(Map<ProductCategorySearchType, String> predicateData) {
        return (Specification<ProductCategory>) ((root, query, builder) -> {
            List<Predicate> predicate = new ArrayList<>();
            for (Map.Entry<ProductCategorySearchType, String> entry : predicateData.entrySet()) {
                predicate.add(builder.like(
                        root.get(entry.getKey().name().toLowerCase()), "%" + entry.getValue() + "%"
                ));
            }

            return builder.and(predicate.toArray(new Predicate[0]));
        });
    }

    private Map<ProductCategorySearchType, String> getPredicateData(ProductCategoryRequest productCategoryRequest) {
        Map<ProductCategorySearchType, String> predicateData = new HashMap<>();

        if (StringUtils.isNotBlank(productCategoryRequest.getCategoryName())) {
            predicateData.put(ProductCategorySearchType.NAME, productCategoryRequest.getCategoryName());
        }

        return predicateData;
    }
}
