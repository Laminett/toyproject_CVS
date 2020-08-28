package com.alliex.cvs.web;

import com.alliex.cvs.config.security.LoginUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TransactionsController {

    @GetMapping("/transactions")
    public String getTransaction(Model model, @AuthenticationPrincipal LoginUser loginUser) {
        model.addAttribute("loginUser", loginUser.getUsername());

        return "transactions";
    }

}
