package com.alliex.cvs.service;

import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.product.ProductRepository;
import com.alliex.cvs.domain.product.ProductSpecs;
import com.alliex.cvs.domain.transaction.TransactionSpecs;
import com.alliex.cvs.domain.type.TransState;
import com.alliex.cvs.domain.type.TransType;
import com.alliex.cvs.exception.ProductAmountLimitExcessException;
import com.alliex.cvs.exception.ProductNotFoundException;
import com.alliex.cvs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductsService {

    private final ProductRepository productRepository;


    @Transactional(readOnly = true)
    public Page<Product> getProducts(Pageable pageable, ProductRequest searchRequest) {
        Map<ProductSpecs.SearchKey, Object> searchKeys = new HashMap<>();
        if (!"".equals(searchRequest.getSearchField()) && !"".equals(searchRequest.getSearchValue())) {
            searchKeys.put(ProductSpecs.SearchKey.valueOf(searchRequest.getSearchField().toUpperCase()), searchRequest.getSearchValue());
        }

        return searchKeys.isEmpty() ? productRepository.findAll(pageable) : productRepository.findAll(ProductSpecs.searchWith(searchKeys), pageable);
    }

    @Transactional
    public ProductAppResponse scanProducts(String barcode) {
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new ProductNotFoundException("not found Products. barcode: " + barcode));

        return new ProductAppResponse(product);
    }

    @Transactional
    public Long save(ProductSaveRequest productSaveRequest) {
        return productRepository.save(productSaveRequest.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("not found product. id: " + id));

        product.update(request.getId(), request.getCategoryId(), request.getName(), request.getPoint(), request.getQuantity(), request.getIsEnabled(), request.getModifiedId());

        return id;
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("not found product. id:" + id));

        productRepository.delete(product);
    }

    @Transactional
    public Long updateAmountPlus(Long productId, int quantity) {
        Product productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Not Found product. productId : " + productId));

        productEntity.updateQuantity(productEntity.getQuantity() + quantity);

        return productId;
    }

    @Transactional
    public Long updateAmountMinus(Long productId, int quantity) {
        Product productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Not Found product. productId : " + productId));

        int _quantity = productEntity.getQuantity() - quantity;
        if (_quantity < 0) {
            throw new ProductAmountLimitExcessException("Not enough Product amount. amount :" + _quantity);
        }

        productEntity.updateQuantity(_quantity);

        return productId;
    }

}
