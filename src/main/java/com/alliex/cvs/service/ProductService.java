package com.alliex.cvs.service;

import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.product.ProductRepository;
import com.alliex.cvs.domain.product.ProductSpecs;
import com.alliex.cvs.domain.product.category.ProductCategoryRepository;
import com.alliex.cvs.exception.ProductAlreadyExistsException;
import com.alliex.cvs.exception.ProductQuantityLimitExcessException;
import com.alliex.cvs.exception.ProductNotFoundException;
import com.alliex.cvs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProducts(Pageable pageable, ProductRequest searchRequest) {
        Map<ProductSpecs.SearchKey, Object> searchKeys = new HashMap<>();

        if (StringUtils.isBlank(searchRequest.getSearchValue())) {
            return productRepository.findAll(pageable).map(ProductResponse::new);
        }

        if (searchRequest.getSearchField().equals("category")) {
            Long categoryId = productCategoryRepository.findByName(searchRequest.getSearchValue()).get().getId();
            searchKeys.put(ProductSpecs.SearchKey.valueOf(searchRequest.getSearchField().toUpperCase()), categoryId);
        } else {
            searchKeys.put(ProductSpecs.SearchKey.valueOf(searchRequest.getSearchField().toUpperCase()), searchRequest.getSearchValue());
        }

        return productRepository.findAll(ProductSpecs.searchWith(searchKeys), pageable).map(ProductResponse::new);
    }

    @Transactional
    public ProductResponse scanProducts(String barcode) {
        // 상품정보조회 name, point
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new ProductNotFoundException(barcode));

        return new ProductResponse(product.getBarcode(), product.getName(), product.getPoint());
    }

    @Transactional
    public Long save(ProductSaveRequest productSaveRequest) {
        productRepository.findByName(productSaveRequest.getName()).ifPresent(product -> {
            throw new ProductAlreadyExistsException(product.getName());
        });

        return productRepository.save(productSaveRequest.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, ProductUpdateRequest productUpdateRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.update(productUpdateRequest);

        return id;
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productRepository.delete(product);
    }

    @Transactional
    public Long updateQuantityPlus(Long productId, int quantity) {
        Product productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        productEntity.updateQuantity(productEntity.getQuantity() + quantity);

        return productId;
    }

    @Transactional
    public Long updateQuantityMinus(Long productId, int quantity) {
        Product productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        int _quantity = productEntity.getQuantity() - quantity;
        if (_quantity < 0) {
            throw new ProductQuantityLimitExcessException(productEntity.getQuantity(), quantity);
        }

        productEntity.updateQuantity(_quantity);

        return productId;
    }

}
