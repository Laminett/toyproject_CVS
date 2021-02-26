package com.alliex.cvs.service;

import com.alliex.cvs.entity.ProductCategory;
import com.alliex.cvs.repository.ProductCategoryRepository;
import com.alliex.cvs.exception.*;
import com.alliex.cvs.repository.ProductCategoryRepositorySupport;
import com.alliex.cvs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    private final ProductCategoryRepositorySupport productCategoryRepositorySupport;

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
        return productCategoryRepositorySupport.findBySearchValue(pageable, productCategoryRequest)
                .map(ProductCategoryResponse::new);
    }

    public ProductCategoryResponse getCategoryById(Long id) {
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new ProductCategoryNotFoundException(id));

        return new ProductCategoryResponse(productCategory);
    }

}
