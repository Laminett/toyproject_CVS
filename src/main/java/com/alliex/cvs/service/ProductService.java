package com.alliex.cvs.service;

import com.alliex.cvs.domain.product.ProductRepository;
import com.alliex.cvs.web.dto.ProductListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductListResponse> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).stream()
                .map(ProductListResponse::new).collect(Collectors.toList());
    }

}
