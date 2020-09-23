package com.alliex.cvs.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("error")
    public String handleError(HttpServletRequest request) {
        Object errorStatusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        log.warn("Error Status Code: " + errorStatusCode);

        if (errorStatusCode != null) {
            return "/errors/" + errorStatusCode;
        } else {
            log.warn("Error Status Code is null. Something is wrong...");

            return "unknown";
        }
    }

    @Override
    public String getErrorPath() {
        return "error";
    }

}
