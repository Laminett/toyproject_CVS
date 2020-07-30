package com.alliex.cvs.web;

import com.alliex.cvs.service.ProductsService;
import com.alliex.cvs.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ProductsApiController {

    @Autowired
    private ProductsService productsService;

    @GetMapping("/api/v1/products")
    public List<ProductResponse> getProductList(@RequestParam(value = "page") int page, @RequestParam(value = "findBy") String searchField
            , @RequestParam(value = "findText") String searchValue) {

        return productsService.getProducts(PageRequest.of(page - 1, 20), searchField, searchValue);
    }

    @GetMapping("/api/v1/barcodescan/{barcode}")
    public ProductAppResponse scanProducts(@PathVariable String barcode) {
        return productsService.scanProducts(barcode);
    }

    @PostMapping("/api/v1/addproducts")
    public Long save(@RequestBody ProductSaveRequest productSaveRequest) {
        return productsService.save(productSaveRequest);
    }

    @PutMapping("/api/v1/products/{id}")
    public Long update(@PathVariable Long id, @RequestBody ProductUpdateRequest request) {
        return productsService.update(id, request);
    }

    @DeleteMapping("/api/v1/products/{id}")
    public Long delete(@PathVariable Long id) {
        productsService.delete(id);

        return id;
    }

}
