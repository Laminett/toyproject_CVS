package com.alliex.cvs.web;

import com.alliex.cvs.service.ProductCategoriesService;
import com.alliex.cvs.service.UserService;
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
public class ProductCategoriesApiController {

    private final ProductCategoriesService productCategoriesService;

    @ApiOperation(value = "Create Category")
    @PostMapping({"api/v1/products-categories", "web-api/v1/products-categories"})
    @ResponseStatus(HttpStatus.CREATED)
    public Long save(@RequestBody ProductCategorySaveRequest productCategorySaveRequest) {
        return productCategoriesService.save(productCategorySaveRequest);
    }

    @ApiOperation(value = "Update Category")
    @PostMapping({"api/v1/products-categories/{id}", "web-api/v1/products-categories/{id}"})
    public Long update(@PathVariable Long id, @RequestBody ProductCategoryUpdateRequest productCategoryUpdateRequest) {
        return productCategoriesService.update(id, productCategoryUpdateRequest);
    }

    @ApiOperation(value = "Get Categories")
    @GetMapping("web-api/v1/products-categories")
    public Page<ProductCategoryResponse> getCategories(@RequestParam(required = false, defaultValue = "1") int page, ProductCategoryRequest productCategoryRequest) {
        return productCategoriesService.getCategories(PageRequest.of(page - 1, 20, Sort.Direction.DESC, "id"), productCategoryRequest);
    }

    @ApiOperation(value = "Get Category")
    @GetMapping({"api/v1/products-categories/{id}", "web-api/v1/products-categories/{id}"})
    public ProductCategoryResponse getCategoryById(@PathVariable Long id) {
        return productCategoriesService.getCategoryById(id);
    }

    @ApiOperation(value = "Delete Category")
    @DeleteMapping({"api/v1/products-categories/{id}", "web-api/v1/products-categories/{id}"})
    public Long delete(@PathVariable Long id) {
        productCategoriesService.delete(id);

        return id;
    }

}
