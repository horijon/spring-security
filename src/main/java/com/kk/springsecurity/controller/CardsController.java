package com.kk.springsecurity.controller;

import com.kk.springsecurity.model.Cards;
import com.kk.springsecurity.model.Customer;
import com.kk.springsecurity.repository.CardsRepository;
import com.kk.springsecurity.repository.CustomerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardsController {
    private final CardsRepository cardsRepository;
    private final CustomerRepository customerRepository;

    public CardsController(CardsRepository cardsRepository, CustomerRepository customerRepository) {
        this.cardsRepository = cardsRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping("cards")
    public List<Cards> getCardDetails(@RequestParam String email) {
        List<Customer> customers = customerRepository.findByEmail(email);
        if (customers != null && !customers.isEmpty()) {
            return cardsRepository.findByCustomerId(customers.get(0).getId());
        }
        return null;
    }
}
