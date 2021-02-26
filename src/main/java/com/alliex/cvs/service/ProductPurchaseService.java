package com.alliex.cvs.service;


import com.alliex.cvs.entity.ProductPurchase;
import com.alliex.cvs.repository.ProductPurchaseRepository;

import com.alliex.cvs.exception.ProductPurchaseNotFoundException;
import com.alliex.cvs.repository.ProductPurchaseRepositorySupport;
import com.alliex.cvs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductPurchaseService {

    private final ProductPurchaseRepository productPurchaseRepository;

    private final ProductService productService;

    private final ProductPurchaseRepositorySupport productPurchaseRepositorySupport;

    public Page<ProductPurchaseResponse> getPurchases(Pageable pageable, ProductPurchaseRequest productPurchaseRequest) {
        return productPurchaseRepositorySupport.findBySearchValue(pageable, productPurchaseRequest)
                .map(ProductPurchaseResponse::new);
    }

    public ProductPurchaseResponse getPurchaseById(Long id) {
        ProductPurchase productPurchase = productPurchaseRepository.findById(id)
                .orElseThrow(() -> new ProductPurchaseNotFoundException(id));

        return new ProductPurchaseResponse(productPurchase);
    }

    @Transactional
    public Long save(ProductPurchaseSaveRequest productPurchaseSaveRequest) {
        productService.increaseQuantity(productPurchaseSaveRequest.getProductId(), productPurchaseSaveRequest.getPurchaseQuantity());

        return productPurchaseRepository.save(productPurchaseSaveRequest.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, ProductPurchaseUpdateRequest productPurchaseUpdateRequest) {
        ProductPurchase productPurchase = productPurchaseRepository.findById(id)
                .orElseThrow(() -> new ProductPurchaseNotFoundException(id));

        int _qunatity = productPurchaseUpdateRequest.getPurchaseQuantity() - productPurchase.getPurchaseQuantity();

        productService.increaseQuantity(productPurchase.getProduct().getId(), _qunatity);

        productPurchase.update(productPurchaseUpdateRequest);

        return productPurchase.getId();
    }

    @Transactional
    public void delete(Long id) {
        ProductPurchase productPurchase = productPurchaseRepository.findById(id)
                .orElseThrow(() -> new ProductPurchaseNotFoundException(id));
        productService.decreaseQuantity(productPurchase.getProduct().getId(), productPurchase.getPurchaseQuantity());

        productPurchaseRepository.delete(productPurchase);
    }

}
