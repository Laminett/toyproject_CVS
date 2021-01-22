package com.alliex.cvs.web;

import com.alliex.cvs.service.ProductCategoryService;
import com.alliex.cvs.web.dto.*;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ProductCategoryApiController {

    private final ProductCategoryService productCategoryService;

    @ApiOperation(value = "Create Category")
    @PostMapping({"/api/v1/products-categories", "/web-api/v1/products-categories"})
    @ResponseStatus(HttpStatus.CREATED)
    public Long save(@RequestBody ProductCategorySaveRequest productCategorySaveRequest) {
        return productCategoryService.save(productCategorySaveRequest);
    }

    @ApiOperation(value = "Update Category")
    @PostMapping({"/api/v1/products-categories/{id}", "/web-api/v1/products-categories/{id}"})
    public Long update(@PathVariable Long id, @RequestBody ProductCategoryUpdateRequest productCategoryUpdateRequest) {
        return productCategoryService.update(id, productCategoryUpdateRequest);
    }

    @ApiOperation(value = "Get Categories")
    @GetMapping("/web-api/v1/products-categories")
    public Page<ProductCategoryResponse> getCategories(@RequestParam(required = false, defaultValue = "1") int page, ProductCategoryRequest productCategoryRequest) {
        return productCategoryService.getCategories(PageRequest.of(page - 1, 20, Sort.Direction.DESC, "id"), productCategoryRequest);
    }

    @ApiOperation(value = "Get Category")
    @GetMapping({"/api/v1/products-categories/{id}", "/web-api/v1/products-categories/{id}"})
    public ProductCategoryResponse getCategoryById(@PathVariable Long id) {
        return productCategoryService.getCategoryById(id);
    }

    @ApiOperation(value = "Delete Category")
    @DeleteMapping({"/api/v1/products-categories/{id}", "/web-api/v1/products-categories/{id}"})
    public Long delete(@PathVariable Long id) {
        productCategoryService.delete(id);

        return id;
    }

}
