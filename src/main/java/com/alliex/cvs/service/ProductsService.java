package com.alliex.cvs.service;

import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.product.ProductRepository;
import com.alliex.cvs.exception.ProductNotFoundException;
import com.alliex.cvs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductsService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Integer> getPages(Pageable pageable, String searchField, String searchValue) {
        // paging default size = 20
        Page<Product> getPage = null;

        if ("name".equals(searchField)) {
            getPage = productRepository.findByName(pageable, searchValue);
        } else if ("category".equals(searchField)) {
            getPage = productRepository.findByCategoryId(pageable, searchValue);
        } else {
            getPage = productRepository.findAll(pageable);
        }

        List<Integer> pages = new ArrayList<>();
        for (int i = 1; i <= getPage.getTotalPages(); i++) {
            pages.add(i);
        }
        return pages;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts(Pageable pageable, String selector, String findText) {
        if ("name".equals(selector)) {
            return productRepository.findByName(pageable, findText).stream()
                    .map(ProductResponse::new).collect(Collectors.toList());
        } else if ("category".equals(selector)) {
            return productRepository.findByCategoryId(pageable, findText).stream()
                    .map(ProductResponse::new).collect(Collectors.toList());
        } else {
            return productRepository.findAll(pageable).stream()
                    .map(ProductResponse::new).collect(Collectors.toList());
        }
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

        product.update(request.getId(), request.getCategoryId(), request.getName(), request.getPoint(), request.getIsEnabled(), request.getModifiedId());

        return id;
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("not found product. id:" + id));

        productRepository.delete(product);
    }

}
