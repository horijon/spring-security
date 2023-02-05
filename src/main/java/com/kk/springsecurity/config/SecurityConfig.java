package com.kk.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // AbstractUserDetailsAuthenticationProvider - base class for authentication providers that implements common functionality for authentication.
    // Step 1 - user enters credentials
    // Step 2 - Spring Security Filters - default is org.springframework.boot.autoconfigure.security.servlet.SpringBootWebSecurityConfiguration
    // Step 3 - AuthenticationManager - default is org.springframework.security.authentication.ProviderManager
    // Step 4 - AuthenticationProvider - default is org.springframework.security.authentication.dao.DaoAuthenticationProvider
    // Step 5.1 - UserDetailsService/UserDetailsManager - default is org.springframework.security.provisioning.InMemoryUserDetailsManager
    // Step 5.2 - PasswordEncoder

    // Step - 2
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/account", "/balance", "/loans", "/cards").authenticated()
                // no authentication required or is public faced apis that doesn't need to be authenticated
                .requestMatchers("/contact", "/notices").permitAll()
                // denyAll is only used to restrict some urls temporarily, may be to fix something in that api and/or is under development
                .requestMatchers("/welcome").denyAll());
        http.formLogin();
        http.httpBasic();
        return http.build();
    }

    // Step - 3 AuthenticationManager uses AuthenticationProvider
    // Step - 4 AuthenticationProvider uses UserDetailsManager
    // Step - 5.1 - UserDetailsManager
    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails admin = User.withUsername("admin")
                .password("123")
                .authorities("admin")
                .build();
        UserDetails user = User.withUsername("user")
                .password("123")
                .authorities("read")
                .build();
        return new InMemoryUserDetailsManager(admin, user);
    }

    // Step - 4 AuthenticationProvider uses PasswordEncoder
    // Step - 5.2 - PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
