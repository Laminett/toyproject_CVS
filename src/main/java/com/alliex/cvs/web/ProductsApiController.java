package com.alliex.cvs.web;

import com.alliex.cvs.service.ProductsService;
import com.alliex.cvs.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ProductsApiController {

    @Autowired
    private ProductsService productsService;

    @PostMapping("/product")
    public List<ProductsListResponse> getProductList(@RequestParam Map<String, Object> param) {
        int page = Integer.parseInt(param.get("page").toString());
        String searchSelector = param.get("findBy").toString();
        String searchText = param.get("findText").toString();

        if ("all".equals(searchSelector)) {
            return productsService.findAll(PageRequest.of(page - 1, 20));
        } else {
            return productsService.findSelect(PageRequest.of(page - 1, 20), searchSelector, searchText);
        }
    }

    @GetMapping("/barcodescan/{barcode}")
    public ProductsAppResponse scanProducts(@PathVariable String barcode){
        return productsService.scanProducts(barcode);
    }

    @PostMapping("/addproducts")
    public Long save(@RequestBody ProductsSaveRequest productsSaveRequest) {
        return productsService.save(productsSaveRequest);
    }

    @PutMapping("/products/{id}")
    public Long update(@PathVariable Long id, @RequestBody ProductsUpdateRequest request) {
        return productsService.update(id, request);
    }

    @DeleteMapping("/products/{id}")
    public Long delete(@PathVariable Long id) {
        productsService.delete(id);

        return id;
    }

}
