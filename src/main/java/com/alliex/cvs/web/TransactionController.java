package com.alliex.cvs.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TransactionController {

    @GetMapping("/transactions")
    public String getTransaction() {
        return "transactions";
    }

}
