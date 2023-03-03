package com.kk.springsecurity.controller;

import com.kk.springsecurity.model.Accounts;
import com.kk.springsecurity.model.Customer;
import com.kk.springsecurity.repository.AccountsRepository;
import com.kk.springsecurity.repository.CustomerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountController {

    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;

    public AccountController(AccountsRepository accountsRepository, CustomerRepository customerRepository) {
        this.accountsRepository = accountsRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping("account")
    public Accounts getAccountDetails(@RequestParam String email) {
        List<Customer> customers = customerRepository.findByEmail(email);
        if (customers != null && !customers.isEmpty()) {
            return accountsRepository.findByCustomerId(customers.get(0).getId());
        }
        return null;
    }
}
