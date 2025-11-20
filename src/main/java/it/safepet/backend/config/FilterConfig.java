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
        registrationBean.addUrlPatterns("/gestioneAmministrativa/*");
        registrationBean.addUrlPatterns("/gestioneCartellaClinica/*");
        registrationBean.addUrlPatterns("/gestioneCondivisioneDati/*");
        registrationBean.addUrlPatterns("/gestioneListaPreferiti/*");
        registrationBean.addUrlPatterns("/gestionePaziente/*");
        registrationBean.addUrlPatterns("/gestionePet/*");
        registrationBean.addUrlPatterns("/gestioneRecensioni/*");
        registrationBean.addUrlPatterns("/gestioneUtente/*");
        registrationBean.addUrlPatterns("/reportVeterinariECliniche/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
