package com.alliex.cvs.service;

import com.alliex.cvs.domain.posts.Posts;
import com.alliex.cvs.domain.product.Products;
import com.alliex.cvs.domain.product.ProductsRepository;
import com.alliex.cvs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductsService {

    private final ProductsRepository productsRepository;

    @Transactional(readOnly = true)
    public List<ProductsListResponse> findAll(Pageable pageable) {
        return productsRepository.findAll(pageable).stream()
                .map(ProductsListResponse::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductsListResponse> findSelect(Pageable pageable, String selector, String findText) {
        if ("name".equals(selector)) {
            return productsRepository.findByName(pageable, findText).stream()
                    .map(ProductsListResponse::new).collect(Collectors.toList());
        } else if ("category".equals(selector)) {
            return productsRepository.findByCategoryId(pageable, findText).stream()
                    .map(ProductsListResponse::new).collect(Collectors.toList());
        } else {
            return productsRepository.findAll(pageable).stream()
                    .map(ProductsListResponse::new).collect(Collectors.toList());
        }
    }

    @Transactional
    public ProductsAppResponse scanProducts(String barcode){
        Products products= productsRepository.findByBarcode(barcode)
                .orElseThrow(() -> new IllegalArgumentException("not found Products. barcode: " + barcode));
        return new ProductsAppResponse(products);
    }


    @Transactional
    public Long save(ProductsSaveRequest productsSaveRequest) {
        return productsRepository.save(productsSaveRequest.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, ProductsUpdateRequest request) {
        Products products = productsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found posts. id: " + id));

        products.update(request.getId(), request.getCategoryId(), request.getName(), request.getPoint(), request.isGetisEnabled(), request.getModifiedId());

        return id;
    }

    @Transactional
    public void delete(Long id) {
        Products products = productsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found posts. id:" + id));

        productsRepository.delete(products);
    }

}
