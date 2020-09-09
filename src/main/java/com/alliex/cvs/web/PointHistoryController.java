package com.alliex.cvs.web;

import com.alliex.cvs.config.security.LoginUser;
import com.alliex.cvs.service.PointHistoryService;
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
public class PointHistoryController {

    private final PointHistoryService pointHistoryService;

    @GetMapping("/point/history")
    public String getPointHistory(Model model, @AuthenticationPrincipal LoginUser loginUser, @RequestParam(value = "searchStatus", required = false) String status, @RequestParam(value = "searchUserName", required = false) String uesrName) {
        model.addAttribute("loginUser", loginUser.getUsername());

        PageRequest pageRequest = PageRequest.of(0, 20, Sort.Direction.DESC, "id");
        model.addAttribute("page", pointHistoryService.getPage(pageRequest, status, uesrName));
        model.addAttribute("pointHistory", pointHistoryService.getPointHistory(pageRequest, status, uesrName));

        return "point-history";
    }

}