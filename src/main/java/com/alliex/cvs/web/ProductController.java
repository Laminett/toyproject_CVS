package com.alliex.cvs.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@Controller
public class ProductController {

    @GetMapping("/products")
    public String getProducts() {
        return "product";
    }

}
