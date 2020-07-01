package com.alliex.cvs.web;

import com.alliex.cvs.config.security.LoginUser;
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
        System.out.println("loginUser:" + loginUser);

        if (loginUser != null) {
            model.addAttribute("loginUser", loginUser);
        }

        return "index";
    }

    @GetMapping("/login/form")
    public String loginPage(String error) {
        if (error != null && error.equals("e")) {
            System.out.println("인증실패");
        } else {
            System.out.println("로그인 페이지로 이동");
        }

        return "login";
    }

}
