package com.alliex.cvs.repository;

import com.alliex.cvs.entity.Product;
import com.alliex.cvs.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findById(Long id);

    Optional<Product> findByBarcodeAndIsEnabled(String barcode, Boolean isEnabled);

    Optional<Product> findByNameAndProductCategory(String name, ProductCategory category);

}
