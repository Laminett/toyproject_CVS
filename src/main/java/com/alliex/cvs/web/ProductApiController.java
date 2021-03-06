package com.alliex.cvs.web;

import com.alliex.cvs.domain.LoginUser;
import com.alliex.cvs.service.ProductService;
import com.alliex.cvs.web.dto.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductApiController {

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "Get Products", notes = "상품 목록 조회")
    @GetMapping({"/api/v1/products", "/web-api/v1/products"})
    public Page<ProductResponse> getProducts(@RequestParam(value = "page", required = false, defaultValue = "1") int page, ProductRequest searchRequest) {
        return productService.getProducts(PageRequest.of(page - 1, 10), searchRequest);
    }

    @ApiOperation(value = "Get Product", notes = "상품 검색")
    @GetMapping({"/api/v1/products/{id}", "/web-api/v1/products/{id}"})
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @ApiOperation(value = "Get Product by Barcode", notes = "상품 바코드 스캔")
    @GetMapping({"/api/v1/barcodescan/{barcode}", "/web-api/v1/barcodescan/{barcode}"})
    public ProductResponse scanProducts(@PathVariable String barcode) {
        return productService.scanProducts(barcode);
    }

    @ApiOperation(value = "Insert Product", notes = "상품 등록")
    @PostMapping({"/api/v1/products", "/web-api/v1/products"})
    @ResponseStatus(HttpStatus.CREATED)
    public Long save(@RequestBody ProductSaveRequest productSaveRequest, @AuthenticationPrincipal LoginUser loginUser) {
        productSaveRequest.setAdminId(loginUser.getUsername());

        return productService.save(productSaveRequest);
    }

    @ApiOperation(value = "Update Product", notes = "상품 수정")
    @PostMapping({"/api/v1/products/{id}", "/web-api/v1/products/{id}"})
    public Long update(@PathVariable Long id, @RequestBody ProductUpdateRequest productUpdateRequest, @AuthenticationPrincipal LoginUser loginUser) {
        productUpdateRequest.setAdminId(loginUser.getUsername());

        return productService.update(id, productUpdateRequest);
    }

    @ApiOperation(value = "Delete Product", notes = "상품 삭제")
    @DeleteMapping({"/api/v1/products/{id}", "/web-api/v1/products/{id}"})
    public Long delete(@PathVariable Long id) {
        productService.delete(id);

        return id;
    }

}
