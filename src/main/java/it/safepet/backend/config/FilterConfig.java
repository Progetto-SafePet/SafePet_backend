package it.safepet.backend.config;

import it.safepet.backend.autenticazione.jwt.JwtAuthFilter;
import it.safepet.backend.autenticazione.jwt.JwtUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtAuthFilter(JwtUtil jwtUtil) {
        FilterRegistrationBean<JwtAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthFilter(jwtUtil));
        registrationBean.addUrlPatterns(
                "/gestionePet/*"); //Aggiungere gli endpoint REST se necessario
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
