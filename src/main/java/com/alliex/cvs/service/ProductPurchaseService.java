package com.alliex.cvs.service;


import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.product.ProductRepository;
import com.alliex.cvs.domain.product.purchase.ProductPurchase;
import com.alliex.cvs.domain.product.purchase.ProductPurchaseRepository;
import com.alliex.cvs.domain.type.ProductPurchaseSearchType;

import com.alliex.cvs.exception.ProductPurchaseNotFoundException;
import com.alliex.cvs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

@RequiredArgsConstructor
@Service
public class ProductPurchaseService {

    private final ProductPurchaseRepository productPurchaseRepository;

    private final ProductService productService;

    public Page<ProductPurchaseResponse> getPurchases(Pageable pageable, ProductPurchaseRequest productPurchaseRequest) {
        return productPurchaseRepository.findAll(searchWith(getPredicateData(productPurchaseRequest)), pageable)
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

    private Specification<ProductPurchase> searchWith(Map<ProductPurchaseSearchType, Object> predicateData) {
        return (Specification<ProductPurchase>) ((root, query, builder) -> {
            List<Predicate> predicate = new ArrayList<>();
            for (Map.Entry<ProductPurchaseSearchType, Object> entry : predicateData.entrySet()) {
                switch (entry.getKey()) {
                    case PRODUCT_ID:
                    case CATEGORY_ID:
                        Join<ProductPurchase, Product> productJoin = root.join(entry.getKey().getField());
                        predicate.add(builder.like(productJoin.get("name"), "%" + entry.getValue() + "%"));
                        break;
                    case PURCHASE_DATE:
                        predicate.add(builder.equal(
                                root.get(entry.getKey().getField()), entry.getValue()));
                        break;
                    default:
                        break;
                }
            }

            return builder.and(predicate.toArray(new Predicate[0]));
        });
    }

    private Map<ProductPurchaseSearchType, Object> getPredicateData(ProductPurchaseRequest productPurchaseRequest) {
        Map<ProductPurchaseSearchType, Object> predicateData = new HashMap<>();

        if (StringUtils.isNotBlank(productPurchaseRequest.getProductName())) {
            predicateData.put(ProductPurchaseSearchType.PRODUCT_ID, productPurchaseRequest.getProductName());
        }

        if (StringUtils.isNotBlank(productPurchaseRequest.getCategoryName())) {
            predicateData.put(ProductPurchaseSearchType.CATEGORY_ID, productPurchaseRequest.getCategoryName());
        }

        if (productPurchaseRequest.getPurchaseDate() != null) {
            predicateData.put(ProductPurchaseSearchType.PURCHASE_DATE, productPurchaseRequest.getPurchaseDate());
        }

        return predicateData;
    }

}
