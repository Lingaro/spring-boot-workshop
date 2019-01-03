package com.lingaro.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
class DevAuth {

    @Autowired
    public void configureDev(AuthenticationManagerBuilder auth, PasswordEncoder encoder) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("test").password(encoder.encode("test")).roles("ADMIN");
    }
}
