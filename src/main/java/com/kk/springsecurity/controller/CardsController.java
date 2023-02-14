package com.kk.springsecurity.controller;

import com.kk.springsecurity.model.Cards;
import com.kk.springsecurity.repository.CardsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardsController {
    private final CardsRepository cardsRepository;

    public CardsController(CardsRepository cardsRepository) {
        this.cardsRepository = cardsRepository;
    }

    @GetMapping("cards")
    public List<Cards> getCardDetails(@RequestParam int id) {
        return cardsRepository.findByCustomerId(id);
    }
}
