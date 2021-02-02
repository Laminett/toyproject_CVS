package com.alliex.cvs.service;

import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.product.ProductRepository;
import com.alliex.cvs.domain.product.category.ProductCategory;
import com.alliex.cvs.domain.type.ProductSearchType;
import com.alliex.cvs.exception.ProductAlreadyExistsException;
import com.alliex.cvs.exception.ProductQuantityLimitExcessException;
import com.alliex.cvs.exception.ProductNotFoundException;
import com.alliex.cvs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProducts(Pageable pageable, ProductRequest productRequest) {
        return productRepository.findAll(searchWith(getPredicateData(productRequest)), pageable).map(ProductResponse::new);
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
        Product product = productRepository.findByBarcode(barcode)
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

        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(productUpdateRequest.getCategoryId());

        productRepository.findByNameAndProductCategory(productUpdateRequest.getName(), productCategory).ifPresent(product -> {
            throw new ProductAlreadyExistsException(product.getName());
        });

        _product.update(productUpdateRequest);

        return id;
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productRepository.delete(product);
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

    private Specification<Product> searchWith(Map<ProductSearchType, Object> predicateData) {
        return (Specification<Product>) ((root, query, builder) -> {
            List<Predicate> predicate = new ArrayList<>();
            for (Map.Entry<ProductSearchType, Object> entry : predicateData.entrySet()) {
                switch (entry.getKey()) {
                    case NAME:
                        predicate.add(builder.like(
                                root.get(entry.getKey().getValue()), "%" + entry.getValue() + "%"));
                        break;
                    case IS_ENABLED:
                        predicate.add(builder.equal(
                                root.get(entry.getKey().getValue()), entry.getValue()));
                        break;
                    case CATEGORY_NAME:
                        Join<Product, ProductCategory> categoryJoin = root.join(entry.getKey().getValue());
                        predicate.add(builder.like(categoryJoin.get("name"), "%" + entry.getValue() + "%"));
                        break;
                    default:
                        break;
                }
            }

            return builder.and(predicate.toArray(new Predicate[0]));
        });
    }

    private Map<ProductSearchType, Object> getPredicateData(ProductRequest productRequest) {
        Map<ProductSearchType, Object> predicateData = new HashMap<>();

        if (StringUtils.isNotBlank(productRequest.getName())) {
            predicateData.put(ProductSearchType.NAME, productRequest.getName());
        }

        if (StringUtils.isNotBlank(productRequest.getCategoryName())) {
            predicateData.put(ProductSearchType.CATEGORY_NAME, productRequest.getCategoryName());
        }

        if (productRequest.getIsEnabled() != null) {
            predicateData.put(ProductSearchType.IS_ENABLED, productRequest.getIsEnabled());
        }

        return predicateData;
    }

}
