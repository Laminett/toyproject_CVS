package com.alliex.cvs.web;

import com.alliex.cvs.domain.user.LoginUser;
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
    public String getPointHistory(Model model, @AuthenticationPrincipal LoginUser loginUser, @RequestParam(value = "searchStatus", required = false) String status, @RequestParam(value = "searchUsername", required = false) String uesrname) {
        model.addAttribute("loginUser", loginUser.getUsername());

        PageRequest pageRequest = PageRequest.of(0, 20, Sort.Direction.DESC, "id");
        model.addAttribute("page", pointHistoryService.getPage(pageRequest, status, uesrname));
        model.addAttribute("pointHistory", pointHistoryService.getPointHistory(pageRequest, status, uesrname));

        return "point-history";
    }

}