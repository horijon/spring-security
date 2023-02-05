package com.kk.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * custom config - copied from org.springframework.boot.autoconfigure.security.servlet.SpringBootWebSecurityConfiguration.class and modified
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/account", "/balance", "/loans", "/cards").authenticated()
                .requestMatchers("/contact", "/notices").permitAll()
                .requestMatchers("/welcome").denyAll());
        http.formLogin();
        http.httpBasic();
        return http.build();
    }
}
