package com.alliex.cvs.web;

import com.alliex.cvs.config.security.LoginUser;
import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.product.ProductRepository;
import com.alliex.cvs.service.ProductsService;
import com.alliex.cvs.web.dto.ProductsAppResponse;
import com.alliex.cvs.web.dto.ProductsFindRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Controller
public class ProductsController {

    private final ProductsService productsService;
    private final ProductRepository productRepository;

    @GetMapping("/products")
    public String getProduct(Model model, Pageable pageable, @AuthenticationPrincipal LoginUser loginUser) {
        // paging default size = 20
        Page<Product> AllPage = productRepository.findAll(pageable);

        List<Integer> pages = new ArrayList<>();
        for (int i = 1; i <= AllPage.getTotalPages(); i++) {
            pages.add(i);
        }

        // 총 페이지 list에 담아서 전달
        model.addAttribute("loginUser", loginUser.getUsername());
        model.addAttribute("page", pages);
        model.addAttribute("findBy", "all");
        model.addAttribute("findText", "all");
        model.addAttribute("Product", productsService.findAll(pageable));

        return "product";
    }

    @PostMapping("/products/find")
    public String findProduct(Model model, Pageable pageable, @RequestBody ProductsFindRequest productsFindRequest, @AuthenticationPrincipal LoginUser loginUser) {
        Page<Product> findPage = null;

        String selector = productsFindRequest.getSearchSelect();
        String findText = productsFindRequest.getSearchText();
        if ("name".equals(selector)) {
            findPage = productRepository.findByName(pageable, findText);
        } else if ("category".equals(selector)) {
            findPage = productRepository.findByCategoryId(pageable, findText);
        } else {
            findPage = productRepository.findAll(pageable);
        }

        List<Integer> pages = new ArrayList<>();
        for (int i = 1; i <= findPage.getTotalPages(); i++) {
            pages.add(i);
        }

        //총 페이지 list에 담아서 전달
        model.addAttribute("loginUser", loginUser.getUsername());
        model.addAttribute("page", pages);
        model.addAttribute("findBy", selector);
        model.addAttribute("findText", findText);
        model.addAttribute("Product", productsService.findSelect(pageable, selector, findText));

        return "product";
    }

}
