package com.alliex.cvs.domain.product.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ProductPurchaseRepository extends JpaRepository<ProductPurchase, Long>, JpaSpecificationExecutor<ProductPurchase> {

}
