package com.alliex.cvs.web;

import com.alliex.cvs.domain.user.LoginUser;
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

        return "index";
    }

    @GetMapping("/login/form")
    public String loginPage(String error) {
        if (error != null && error.equals("e")) {
            System.out.println("login failure.");
        }

        return "login";
    }

    @GetMapping("/logout/success")
    public String logoutSuccess() {
        System.out.println("logout success.");

        return "redirect:/";
    }

}
