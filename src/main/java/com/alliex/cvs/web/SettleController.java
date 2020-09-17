package com.alliex.cvs.web;

import com.alliex.cvs.domain.user.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class SettleController {

    @GetMapping("/settle")
    public String getSettleList(Model model, @AuthenticationPrincipal LoginUser loginUser, @RequestParam(value = "searchDate", required = false) String aggregatedAt, @RequestParam(value = "searchUsername", required = false) String username) {
        model.addAttribute("loginUser", loginUser.getUsername());

        return "settle";
    }

}