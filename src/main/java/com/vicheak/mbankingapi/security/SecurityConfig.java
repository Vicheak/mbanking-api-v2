package com.vicheak.mbankingapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public UserDetailsManager userDetailsManagerConfig() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        UserDetails adminUser = User.withUsername("admin")
                .password(passwordEncoder.encode("admin@123"))
                .build();

        UserDetails managerUser = User.withUsername("manager")
                .password(passwordEncoder.encode("manager@123"))
                .build();

        UserDetails customerUser = User.withUsername("customer")
                .password(passwordEncoder.encode("customer@123"))
                .build();

        manager.createUser(adminUser);
        manager.createUser(managerUser);
        manager.createUser(customerUser);

        return manager;
    }

    @Bean
    public SecurityFilterChain securityFilterChainConfig(HttpSecurity http) throws Exception {
        //customize security filter here
        http.authorizeHttpRequests(auth -> {

            auth.anyRequest().authenticated();
        });

        //enable default form-based security and authentication
        //http.formLogin(Customizer.withDefaults());

        //enable basic authentication with http basic
        //allow client application to be authenticated
        http.httpBasic(Customizer.withDefaults());

        //disable csrf
        http.csrf(AbstractHttpConfigurer::disable);

        //update API policy to stateless
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
