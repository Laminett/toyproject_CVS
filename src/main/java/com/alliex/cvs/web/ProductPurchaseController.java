package com.alliex.cvs.web;

import com.alliex.cvs.domain.user.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class ProductPurchaseController {

    @GetMapping("products-purchases")
    public String getProductPurchases(Model model, @AuthenticationPrincipal LoginUser loginUser) {
        model.addAttribute("loginUser", loginUser.getUsername());

        return "product-purchase";
    }

}
