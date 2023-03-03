package com.kk.springsecurity.controller;

import com.kk.springsecurity.model.Customer;
import com.kk.springsecurity.model.Loans;
import com.kk.springsecurity.repository.CustomerRepository;
import com.kk.springsecurity.repository.LoanRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoansController {
    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;

    public LoansController(LoanRepository loanRepository, CustomerRepository customerRepository) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping("loans")
    // the method will execute but won't return the response if the user doesn't have the role 'USER'
//    @PreAuthorize("hasRole('USER')")
//    @PostAuthorize("hasRole('USER')")
    public List<Loans> getLoanDetails(@RequestParam String email) {
        List<Customer> customers = customerRepository.findByEmail(email);
        if (customers != null && !customers.isEmpty()) {
            return loanRepository.findByCustomerIdOrderByStartDtDesc(customers.get(0).getId());
        }
        return null;
    }
}
