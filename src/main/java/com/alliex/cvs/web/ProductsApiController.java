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
public class ProductsApiController {

    @Autowired
    private ProductsService productsService;

    @PostMapping("/api/v1/products")
    public List<ProductsListResponse> getProductList(@RequestParam Map<String, Object> param) {
        int page = Integer.parseInt(param.get("page").toString());
        String searchSelector = param.get("findBy").toString();
        String searchText = param.get("findText").toString();

        return productsService.findSelect(PageRequest.of(page - 1, 20), searchSelector, searchText);
    }

    @GetMapping("/api/v1/barcodescan/{barcode}")
    public ProductsAppResponse scanProducts(@PathVariable String barcode) {
        return productsService.scanProducts(barcode);
    }

    @PostMapping("/api/v1/addproducts")
    public Long save(@RequestBody ProductsSaveRequest productsSaveRequest) {
        return productsService.save(productsSaveRequest);
    }

    @PutMapping("/api/v1/products/{id}")
    public Long update(@PathVariable Long id, @RequestBody ProductsUpdateRequest request) {
        return productsService.update(id, request);
    }

    @DeleteMapping("/api/v1/products/{id}")
    public Long delete(@PathVariable Long id) {
        productsService.delete(id);

        return id;
    }

}
