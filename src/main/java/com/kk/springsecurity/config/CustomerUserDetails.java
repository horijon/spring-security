package com.kk.springsecurity.config;

import com.kk.springsecurity.model.Customer;
import com.kk.springsecurity.repository.CustomerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerUserDetails implements UserDetailsService {
    private final CustomerRepository customerRepository;

    public CustomerUserDetails(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameAsEmail) throws UsernameNotFoundException {
        String password;
        List<GrantedAuthority> authorities;
        List<Customer> customer = customerRepository.findByEmail(usernameAsEmail);
        if (customer.size() == 0) {
            throw new UsernameNotFoundException("User details not found for the user : " + usernameAsEmail);
        } else {
            password = customer.get(0).getPwd();
            authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(customer.get(0).getRole()));
        }
        return new User(usernameAsEmail, password, authorities);
    }
}
