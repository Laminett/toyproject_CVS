package com.alliex.cvs.web;

import com.alliex.cvs.service.ProductsService;
import com.alliex.cvs.web.dto.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ProductsApiController {

    @Autowired
    private ProductsService productsService;

    @ApiOperation(value = "Get Products", notes = "상품 목록 조회")
    @GetMapping("/api/v1/products")
    public List<ProductResponse> getProductList(@RequestParam(value = "page") int page, @RequestParam(value = "findBy") String searchField
            , @RequestParam(value = "findText") String searchValue) {

        return productsService.getProducts(PageRequest.of(page - 1, 20), searchField, searchValue);
    }

    @ApiOperation(value = "Get Product by Barcode", notes = "상품 바코드 스캔")
    @GetMapping("/api/v1/barcodescan/{barcode}")
    public ProductAppResponse scanProducts(@PathVariable String barcode) {
        return productsService.scanProducts(barcode);
    }

    @ApiOperation(value = "Insert Product", notes = "상품 등록")
    @PostMapping("/api/v1/addproducts")
    public Long save(@RequestBody ProductSaveRequest productSaveRequest) {
        return productsService.save(productSaveRequest);
    }

    @ApiOperation(value = "Update Product", notes = "상품 수정")
    @PutMapping("/api/v1/products/{id}")
    public Long update(@PathVariable Long id, @RequestBody ProductUpdateRequest request) {
        return productsService.update(id, request);
    }

    @ApiOperation(value = "Delete Product", notes = "상품 삭제")
    @DeleteMapping("/api/v1/products/{id}")
    public Long delete(@PathVariable Long id) {
        productsService.delete(id);

        return id;
    }

}
