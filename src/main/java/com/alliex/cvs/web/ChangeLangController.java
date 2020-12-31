package com.alliex.cvs.web;

import com.alliex.cvs.web.dto.ApiResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;

@Controller
public class ChangeLangController {

    @GetMapping("/web-api/v1/language/{lang}")
    @ResponseBody
    public ApiResult ChangeLanguage(@PathVariable String lang, HttpServletRequest request) {
        Locale locale = null;
        if (lang.equalsIgnoreCase("en")) {
            locale = Locale.ENGLISH;
        }else if (lang.equalsIgnoreCase("vi")) {
            locale = new Locale("vi", "VN");
        } else if (lang.equalsIgnoreCase("ko")) {
            locale = Locale.KOREAN;
        }

        HttpSession session =  request.getSession();
        session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);

        return new ApiResult(null);
    }
}