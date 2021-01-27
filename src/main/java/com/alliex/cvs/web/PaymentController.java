package com.alliex.cvs.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class PaymentController {

    @GetMapping("/payment")
    public String posPayment() {
        return "payment";
    }

}