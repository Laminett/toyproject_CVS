package com.alliex.cvs.domain.product;

import com.alliex.cvs.web.dto.ProductsAppResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductsRepository extends JpaRepository<Products, Long> {

    Page<Products> findAll(Pageable pageable);

    Optional<Products> findById(Long id);

    Page<Products> findByCategoryId(Pageable pageable, String categoryId);

    Page<Products> findByName(Pageable pageable, String name);

    Optional<Products> findByBarcode(String barcode);
}
