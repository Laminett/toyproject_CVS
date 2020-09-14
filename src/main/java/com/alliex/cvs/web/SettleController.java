package com.alliex.cvs.web;

import com.alliex.cvs.domain.user.LoginUser;
import com.alliex.cvs.service.SettleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class SettleController {

    private final SettleService settleService;

    @GetMapping("/settle")
    public String getSettleList(Model model, @AuthenticationPrincipal LoginUser loginUser, @RequestParam(value = "searchDate", required = false) String date) {
        model.addAttribute("loginUser", loginUser.getUsername());

        PageRequest pageRequest = PageRequest.of(0, 20, Sort.Direction.DESC, "id");
        model.addAttribute("page", settleService.getPage(pageRequest, date));
        model.addAttribute("settle", settleService.getSettleList(pageRequest, date));

        return "settle";
    }
}