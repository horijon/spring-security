package com.kk.springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardsController {
    @GetMapping("cards")
    public String getCardDetails() {
        return "Here are the card details from DB";
    }
}
