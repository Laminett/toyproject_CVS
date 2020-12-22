package com.alliex.cvs.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findById(Long id);

    Optional<Product> findByName(String name);

    Optional<Product> findByBarcode(String barcode);

}
