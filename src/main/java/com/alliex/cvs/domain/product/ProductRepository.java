package com.alliex.cvs.domain.product;

import com.alliex.cvs.domain.product.category.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findById(Long id);

    Optional<Product> findByBarcode(String barcode);

    Optional<Product> findByNameAndProductCategory(String name, ProductCategory category);

}
