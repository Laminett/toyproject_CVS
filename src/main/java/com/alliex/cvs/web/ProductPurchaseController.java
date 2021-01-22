package com.alliex.cvs.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class ProductPurchaseController {

    @GetMapping("/products-purchases")
    public String getProductPurchases() {
        return "product-purchase";
    }

}
