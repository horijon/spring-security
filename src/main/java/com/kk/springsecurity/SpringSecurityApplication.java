package com.kk.springsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
//@EnableWebSecurity(debug = true) // not recommended in prod
// securedEnabled and jsr250Enabled are less powerful than prePostEnabled,
// so we will use prePostEnabled (@PreAuthorize and @PostAuthorize)
// and we might implement PermissionEvaluator for custom permission check.
// securedEnabled (@Secured) and jsr250Enabled annotations are here to support them as well,
// but we won't use them as they are less powerful than prePostEnabled.
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SpringSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args);
    }

}
