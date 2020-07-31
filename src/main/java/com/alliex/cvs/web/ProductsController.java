package com.alliex.cvs.web;

import com.alliex.cvs.config.security.LoginUser;
import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.product.ProductRepository;
import com.alliex.cvs.service.ProductsService;
import com.alliex.cvs.web.dto.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Controller
public class ProductsController {

    private final ProductsService productsService;

    @GetMapping("/products")
    public String getProduct(Model model, Pageable pageable, @RequestParam(value = "searchField", required = false) String searchField,
                             @RequestParam(value = "searchValue", required = false) String searchValue, @AuthenticationPrincipal LoginUser loginUser) {
        if (searchField == null) {
            model.addAttribute("findBy", "all");
            model.addAttribute("findText", "all");
        } else {
            model.addAttribute("findBy", searchField);
            model.addAttribute("findText", searchValue);
        }
        model.addAttribute("loginUser", loginUser.getUsername());
        model.addAttribute("page", productsService.getPages(pageable, searchField, searchValue));
        model.addAttribute("product", productsService.getProducts(pageable, searchField, searchValue));

        return "product";
    }

}
