package com.kk.springsecurity.config;

import com.kk.springsecurity.constants.SecurityConstants;
import com.kk.springsecurity.filter.AuthoritiesLoggingAfterFilter;
import com.kk.springsecurity.filter.AuthoritiesLoggingAtFilter;
import com.kk.springsecurity.filter.JWTTokenGeneratorFilter;
import com.kk.springsecurity.filter.JWTTokenValidatorFilter;
import com.kk.springsecurity.filter.RequestValidationBeforeFilter;
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

        // we are disabling default JSESSIONID creation from Spring Security by commenting the lines at 45 and 46, we are saying
        // we are going to take care of our own session management or token management inside my web application
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .securityContext().requireExplicitSave(false)
//                .and().sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .cors().configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    // this way we can expose the headers that we are sending inside the response to the client application,
                    // otherwise the browser won't accept these headers because there are 2 different origins that are trying to communicate
                    config.setExposedHeaders(Collections.singletonList(SecurityConstants.JWT_HEADER));
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
        http.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class);
        http.addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class);
        // the order of AuthoritiesLoggingAtFilter execution may be sometimes either before or other times after BasicAuthenticationFilter
        http.addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class);
        http.addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class);
        http.addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class);
        http.authorizeHttpRequests((authorize) -> authorize
                /*.requestMatchers("/account").hasAuthority("VIEWACCOUNT")
                .requestMatchers("/balance").hasAnyAuthority("VIEWACCOUNT", "VIEWBALANCE")
                .requestMatchers("/loans").hasAuthority("VIEWLOANS")
                .requestMatchers("/cards").hasAuthority("VIEWCARDS")*/
                .requestMatchers("/account").hasRole("USER")
                .requestMatchers("/balance").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/loans").hasRole("USER")
                .requestMatchers("/cards").hasRole("USER")
                .requestMatchers("/user").authenticated()
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
