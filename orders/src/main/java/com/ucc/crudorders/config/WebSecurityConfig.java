package com.ucc.crudorders.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.ucc.crudorders.filters.JwtRequestFilter;
import com.ucc.crudorders.service.UserDetailsServiceImpl;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfig {


  @Autowired
  private JwtRequestFilter jwtRequestFilter;
  @Bean
  SecurityFilterChain web(HttpSecurity http) throws Exception {
    http
            .csrf(csrf -> csrf.disable())// desabilita csrf uasando el nuevo customizer
        .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers("/publico/**").permitAll()
            .requestMatchers("/api/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/swagger-ui/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/v3/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/swagger-ui/index.html").permitAll()

            .anyRequest().authenticated()
        )
        .cors(withDefaults())
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement((session) -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
    ;

    return http.build();
  }
/*
  @Bean
  @Primary
  UserDetailsServiceImpl userDetailsService() {
    return new UserDetailsServiceImpl();
  }*/


  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration
      authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

}

