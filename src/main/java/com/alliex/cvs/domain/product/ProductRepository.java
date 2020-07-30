package com.alliex.cvs.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAll(Pageable pageable);

    Optional<Product> findById(Long id);

    Page<Product> findByCategoryId(Pageable pageable, String categoryId);

    Page<Product> findByName(Pageable pageable, String name);

    Optional<Product> findByBarcode(String barcode);

}
