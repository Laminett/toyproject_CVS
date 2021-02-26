package com.alliex.cvs.service;

import com.alliex.cvs.entity.ProductCategory;
import com.alliex.cvs.repository.ProductCategoryRepository;
import com.alliex.cvs.domain.type.ProductCategorySearchType;
import com.alliex.cvs.exception.*;
import com.alliex.cvs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public Long save(ProductCategorySaveRequest productCategorySaveRequest) {
        // Check existence of a category.
        productCategoryRepository.findByName(productCategorySaveRequest.getCategoryName()).ifPresent(productCategory -> {
            throw new ProductCategoryAlreadyExistsException(productCategory.getName());
        });

        // Create category.
        return productCategoryRepository.save(productCategorySaveRequest.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, ProductCategoryUpdateRequest productCategoryUpdateRequest) {
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new ProductCategoryNotFoundException(id));

        productCategory.update(productCategoryUpdateRequest);

        return productCategory.getId();
    }

    @Transactional
    public void delete(Long id) {
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new ProductCategoryNotFoundException(id));

        ProductCategoryUpdateRequest productCategoryUpdateRequest =
                new ProductCategoryUpdateRequest(productCategory.getName(), productCategory.getAdminId(), false);
        productCategory.update(productCategoryUpdateRequest);
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


    private Specification<ProductCategory> searchWith(Map<ProductCategorySearchType, Object> predicateData) {
        return (Specification<ProductCategory>) ((root, query, builder) -> {
            List<Predicate> predicate = new ArrayList<>();
            for (Map.Entry<ProductCategorySearchType, Object> entry : predicateData.entrySet()) {
                switch (entry.getKey()) {
                    case NAME:
                        predicate.add(builder.like(
                                root.get(entry.getKey().getField()), "%" + entry.getValue() + "%"));
                        break;
                    case IS_ENABLED:
                        predicate.add(builder.equal(
                                root.get(entry.getKey().getField()), entry.getValue()));
                        break;
                    default:
                        break;
                }
            }

            return builder.and(predicate.toArray(new Predicate[0]));
        });
    }

    private Map<ProductCategorySearchType, Object> getPredicateData(ProductCategoryRequest productCategoryRequest) {
        Map<ProductCategorySearchType, Object> predicateData = new HashMap<>();

        if (StringUtils.isNotBlank(productCategoryRequest.getCategoryName())) {
            predicateData.put(ProductCategorySearchType.NAME, productCategoryRequest.getCategoryName());
        }

        if (productCategoryRequest.getIsEnabled() != null) {
            predicateData.put(ProductCategorySearchType.IS_ENABLED, productCategoryRequest.getIsEnabled());
        }

        return predicateData;
    }
}
