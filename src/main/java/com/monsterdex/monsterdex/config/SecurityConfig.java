package com.monsterdex.monsterdex.config;

import com.monsterdex.monsterdex.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity 
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService customUserDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
            
                .requestMatchers("/", "/login", "/usuarios/cadastro", "/css/**", "/js/**", "/images/**").permitAll()
                
                
                .requestMatchers("/h2-console/**").permitAll()
                
               
                .requestMatchers("/api/**").permitAll()
                
                
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") // 
                .defaultSuccessUrl("/criaturas", true) 
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout") 
                .permitAll()
            )
            
            .headers(headers -> headers.frameOptions().sameOrigin())
            .csrf(csrf -> csrf.disable()); 

        return http.build();
    }
    
}