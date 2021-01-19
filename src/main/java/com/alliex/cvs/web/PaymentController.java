package com.alliex.cvs.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class PaymentController {

    @GetMapping("/payment")
    public String posPayment(Model model) {
        return "payment";
    }

    @GetMapping("/payment/end/{transactionNumber}")
    public String posPaymentEnd(@PathVariable("transactionNumber") String transNumber, Model model) {
        System.out.println("transNumber: " + transNumber);
        return "payment-end";
    }

}