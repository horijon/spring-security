package com.kk.springsecurity.config;

import com.kk.springsecurity.model.Customer;
import com.kk.springsecurity.repository.CustomerRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * CustomerUserDetails is removed as we no more use default {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider} that uses UserDetails
 * but instead use our own CustomUsernamePwdAuthenticationProvider
 */
@Component
public class CustomUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUsernamePwdAuthenticationProvider(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        List<Customer> customer = customerRepository.findByEmail(username);
        if (customer.size() > 0) {
            if (passwordEncoder.matches(pwd, customer.get(0).getPwd())) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(customer.get(0).getRole()));
                return new UsernamePasswordAuthenticationToken(username, pwd, authorities);
            } else {
                throw new BadCredentialsException("Invalid password!");
            }
        } else {
            throw new BadCredentialsException("No user registered with this details!");
        }
    }

    /**
     * copied from AbstractUserDetailsAuthenticationProvider which is extended by DaoAuthenticationProvider
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
