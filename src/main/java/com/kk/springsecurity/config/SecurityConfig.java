package com.kk.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        // csrf protection applies to POST request only as it is not required to protect for get requests.

        /**
         *  From Spring Security 6, below actions will not happen by default,
         *  1) The Authentication details will not be saved automatically into SecurityContextHolder. To change this behaviour either we need to save
         *      these details explicitly into SecurityContextHolder or we can configure securityContext().requireExplicitSave(false) like shown below.
         *  2) The Session & JSessionID will not be created by default. Inorder to create a session after intial login, we need to configure
         *      sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)) like shown below.
         */
        http.securityContext().requireExplicitSave(false)
                .and().sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .cors().configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    // it can allow browser to send cross-origin request upto 1 hr after first preflight request
                    // after maxAge, the browser again sends preflight requests to check if cross-origin is allowed.
                    config.setMaxAge(3600L);
                    return config;
                })
                // previously we disabled csrf, then the register api worked fine without csrf token, worked from postman as well
                // if we didn't disable the csrf then we would have got 401 unauthozied error as spring security by default protects csrf attacks
                // this line of ignoring csrf will bypass csrf protection for these apis only.
                // .and().csrf().ignoringRequestMatchers("/contact", "/register");

                /**
                 *  In Spring Security 5, the default behavior is that the CsrfToken will be loaded on every request. Where as with
                 *  Spring Security 6, the default is that the lookup of the CsrfToken will be deferred until it is needed. The developer
                 *  has to write logic to read the CSRF token and send it as part of the response. When framework sees the CSRF token
                 *  in the response header, it takes care of sending the same as Cookie as well. For the same, we need to use CsrfTokenRequestAttributeHandler
                 *  and create a filter with the name CsrfCookieFilter which runs every time after the Spring Security in built filter BasicAuthenticationFilter
                 *  like shown below. More details about Filters, are discussed inside the Section 8 of the course.
                 */
                .and().csrf((csrf) -> csrf.csrfTokenRequestHandler(requestHandler).ignoringRequestMatchers("/contact", "/register")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
        http.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class); // this way the client application can also see the csrf token otherwise by default only the server will be able to see the csrf token
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/account", "/balance", "/loans", "/cards", "/user").authenticated()
                .requestMatchers("/notices", "/contact", "/register").permitAll()
                .requestMatchers("/welcome").denyAll());
        http.formLogin();
        http.httpBasic();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
