package com.alliex.cvs.web;

import com.alliex.cvs.service.ProductService;
import com.alliex.cvs.web.dto.ProductListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ProductApiController {

    @Autowired
    private ProductService productService;

    @PostMapping("/product")
    public List<ProductListResponse> getProductList(Model model, Pageable pageable, @RequestParam Map<String, Object> param){
        int page= Integer.parseInt(param.get("page").toString());
        return productService.findAll(PageRequest.of(page-1,20));
    }


}
