package com.vicheak.mbankingapi.security;

import com.vicheak.mbankingapi.security.authorityconfig.UserAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authenticationProviderConfig() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChainConfig(HttpSecurity http) throws Exception {
        //customize security filter here
        http.authorizeHttpRequests(auth -> {
            //welcome page
            auth.requestMatchers("/").permitAll();

            //user security
            auth.requestMatchers(HttpMethod.GET, "/api/v1/users/**")
                    .hasAuthority(UserAuth.USER_READ.getName());

            auth.requestMatchers(HttpMethod.POST, "/api/v1/users/**")
                    .hasAuthority(UserAuth.USER_WRITE.getName());

            auth.requestMatchers(HttpMethod.PUT, "/api/v1/users/**")
                    .hasAuthority(UserAuth.USER_UPDATE.getName());

            auth.requestMatchers(HttpMethod.DELETE, "/api/v1/users/**")
                    .hasAuthority(UserAuth.USER_DELETE.getName());

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
