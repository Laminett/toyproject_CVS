package com.alliex.cvs.web;

import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.product.ProductRepository;
import com.alliex.cvs.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @GetMapping("/product")
    public String getProduct(Model model, Pageable pageable){

        //paging default size = 20
        Page<Product> ptest = productRepository.findAll(pageable);

        List<Integer> pages = new ArrayList<>();
        for (int i=1;i<=ptest.getTotalPages();i++){
            pages.add(i);
        }

        //총 페이지 list에 담아서 전달
        model.addAttribute("page",pages);
        model.addAttribute("Product", productService.findAll(pageable));
        return "product";
    }


}
