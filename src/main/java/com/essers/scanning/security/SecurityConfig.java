package com.essers.scanning.security;

import com.essers.scanning.data.service.UserService;
import com.essers.scanning.views.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;


@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurityConfigurerAdapter {
    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        //override default login view
        setLoginView(http, LoginView.class);
        //override default login success url
        http.formLogin().defaultSuccessUrl("/portal", true);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //allow images to be loaded by not logged in users
        web.ignoring().antMatchers("/images/**");
        super.configure(web);
    }

    //Bcrypt password encoder (Algorithm: BCrypt(2a), strength: 10)
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Component
    public class CustomAuthenticationProvider implements AuthenticationProvider {
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            //get username and password from login form
            String username = authentication.getName();
            String password = authentication.getCredentials().toString();
            UserDetails user = userService.loadUserByUsername(username);
            //if user exists and hashed password matches password from login form
            if (user.getUsername().equals(username) && passwordEncoder().matches(password, user.getPassword())) {
                //return user details (without any ROLES)
                return new UsernamePasswordAuthenticationToken(username,
                        passwordEncoder().encode(password),
                        Collections.emptyList());
            } else {
                //throw exception if user does not exist or password does not match
                throw new BadCredentialsException("Authentication failed");
            }
        }

        //AuthenticationProvider must implement this method
        //returns true if the class can support the authentication object
        @Override
        public boolean supports(Class<?> aClass) {
            return aClass.equals(UsernamePasswordAuthenticationToken.class);
        }
    }

}
