package be.uantwerpen.labplanner;

import be.uantwerpen.labplanner.Exception.CustomAuthenticationFailureHandler;
import org.h2.server.web.WebServlet;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
class WebConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }

    /* You can choose to use overridde httpsecurity
    or implement method-level security using @preAuthorize in the controller classes */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/console/*").hasAuthority("Console Access")  //allow h2 console access to admins only
                .antMatchers("/usermanagement/**").hasAuthority("User Management")
                .anyRequest().authenticated()   //all other urls can be access by any authenticated role
                .and().formLogin().failureHandler(customAuthenticationFailureHandler());
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
}
