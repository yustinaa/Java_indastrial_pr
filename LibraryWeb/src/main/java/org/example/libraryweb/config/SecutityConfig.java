package org.example.libraryweb.config;

import org.example.libraryweb.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecutityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;//спринг сам найдет сервис (благодаря @Service)

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/process_register", "/error").permitAll()
                        .requestMatchers("/librarian/**").hasRole("LIBRARIAN")
                        .requestMatchers("/reader/**").hasRole("READER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")           //URL для страницы входа
                        .loginProcessingUrl("/login")  // URL, куда форма отправляет данные (POST)
                        .defaultSuccessUrl("/success", true)
                        .permitAll()
                )
                .logout(logout -> logout.logoutSuccessUrl("/login"));

        return http.build();
    }
}