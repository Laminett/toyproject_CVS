package com.alliex.cvs.web;

import com.alliex.cvs.domain.user.LoginUser;
import com.alliex.cvs.service.ProductPurchaseService;
import com.alliex.cvs.web.dto.*;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ProductPurchaseApiController {

    private final ProductPurchaseService productPurchaseService;

    @ApiOperation(value = "Get Purchases")
    @GetMapping("web-api/v1/products-purchases")
    public Page<ProductPurchaseResponse> getPurchases(@RequestParam(required = false, defaultValue = "1") int page, ProductPurchaseRequest productPurchaseRequest) {
        return productPurchaseService.getPurchases(PageRequest.of(page - 1, 20, Sort.Direction.DESC, "id"), productPurchaseRequest);
    }

    @ApiOperation(value = "Get Purchase")
    @GetMapping({"api/v1/products-purchases/{id}", "web-api/v1/products-purchases/{id}"})
    public ProductPurchaseResponse getPurchaseById(@PathVariable Long id) {
        return productPurchaseService.getPurchaseById(id);
    }

    @ApiOperation(value = "Create Purchase")
    @PostMapping({"api/v1/products-purchases", "web-api/v1/products-purchases"})
    @ResponseStatus(HttpStatus.CREATED)
    public Long save(@RequestBody ProductPurchaseSaveRequest productPurchaseSaveRequest, @AuthenticationPrincipal LoginUser loginUser) {
        productPurchaseSaveRequest.setAdminId(loginUser.getUsername());

        return productPurchaseService.save(productPurchaseSaveRequest);
    }

    @ApiOperation(value = "Update Purchase")
    @PostMapping({"api/v1/products-purchases/{id}", "web-api/v1/products-purchases/{id}"})
    public Long update(@PathVariable Long id, @RequestBody ProductPurchaseUpdateRequest productPurchaseUpdateRequest, @AuthenticationPrincipal LoginUser loginUser) {
        productPurchaseUpdateRequest.setAdminId(loginUser.getUsername());

        return productPurchaseService.update(id, productPurchaseUpdateRequest);
    }

    @ApiOperation(value = "Delete Purchase")
    @DeleteMapping({"api/v1/proudcts-purchases/{id}", "web-api/v1/products-purchases/{id}"})
    public Long delete(@PathVariable Long id) {
        productPurchaseService.delete(id);

        return id;
    }

}
