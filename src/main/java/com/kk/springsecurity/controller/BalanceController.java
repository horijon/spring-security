package com.kk.springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {
    @GetMapping("balance")
    public String getBalanceDetails() {
        return "Here are the balance details from DB";
    }
}
