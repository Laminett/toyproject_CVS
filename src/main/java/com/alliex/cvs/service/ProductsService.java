package com.alliex.cvs.service;

import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.product.ProductRepository;
import com.alliex.cvs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductsService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductsListResponse> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).stream()
                .map(ProductsListResponse::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductsListResponse> findSelect(Pageable pageable, String selector, String findText) {
        if ("name".equals(selector)) {
            return productRepository.findByName(pageable, findText).stream()
                    .map(ProductsListResponse::new).collect(Collectors.toList());
        } else if ("category".equals(selector)) {
            return productRepository.findByCategoryId(pageable, findText).stream()
                    .map(ProductsListResponse::new).collect(Collectors.toList());
        } else {
            return productRepository.findAll(pageable).stream()
                    .map(ProductsListResponse::new).collect(Collectors.toList());
        }
    }

    @Transactional
    public ProductsAppResponse scanProducts(String barcode) {
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new IllegalArgumentException("not found Products. barcode: " + barcode));
        return new ProductsAppResponse(product);
    }


    @Transactional
    public Long save(ProductsSaveRequest productsSaveRequest) {
        return productRepository.save(productsSaveRequest.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, ProductsUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found posts. id: " + id));

        product.update(request.getId(), request.getCategoryId(), request.getName(), request.getPoint(), request.isGetisEnabled(), request.getModifiedId());

        return id;
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found posts. id:" + id));

        productRepository.delete(product);
    }

}
