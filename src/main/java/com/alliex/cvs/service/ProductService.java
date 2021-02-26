package com.alliex.cvs.service;

import com.alliex.cvs.entity.Product;
import com.alliex.cvs.repository.ProductRepository;
import com.alliex.cvs.entity.ProductCategory;
import com.alliex.cvs.exception.ProductAlreadyExistsException;
import com.alliex.cvs.exception.ProductQuantityLimitExcessException;
import com.alliex.cvs.exception.ProductNotFoundException;
import com.alliex.cvs.repository.ProductRepositorySupport;
import com.alliex.cvs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductRepositorySupport productRepositorySupport;

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProducts(Pageable pageable, ProductRequest productRequest) {
        return productRepositorySupport.findBySearchValue(pageable, productRequest).map(ProductResponse::new);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return new ProductResponse(product);
    }

    @Transactional
    public ProductResponse scanProducts(String barcode) {
        // 상품정보조회 name, point
        Product product = productRepository.findByBarcodeAndIsEnabled(barcode, true)
                .orElseThrow(() -> new ProductNotFoundException(barcode));

        return new ProductResponse(product);
    }

    @Transactional
    public Long save(ProductSaveRequest productSaveRequest) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(productSaveRequest.getCategoryId());

        productRepository.findByNameAndProductCategory(productSaveRequest.getName(), productCategory).ifPresent(product -> {
            throw new ProductAlreadyExistsException(product.getName());
        });

        return productRepository.save(productSaveRequest.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, ProductUpdateRequest productUpdateRequest) {
        Product _product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        _product.update(productUpdateRequest);

        return id;
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(product.getProductCategory().getId(),
                product.getName(), product.getPoint(), product.getQuantity(), false, product.getAdminId());

        product.update(productUpdateRequest);
    }

    @Transactional
    public Long increaseQuantity(Long productId, int quantity) {
        Product productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        productEntity.updateQuantity(productEntity.getQuantity() + quantity);

        return productId;
    }

    @Transactional
    public Long decreaseQuantity(Long productId, int quantity) {
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
