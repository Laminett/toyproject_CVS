package com.alliex.cvs.web;

import com.alliex.cvs.domain.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal LoginUser loginUser) {
        if (loginUser != null) {
            model.addAttribute("loginUser", loginUser);
        }

        return "transactions";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "login-form";
    }

    @GetMapping("/logout/success")
    public String logoutSuccess() {
        return "redirect:/login-form";
    }

}