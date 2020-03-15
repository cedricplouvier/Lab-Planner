package be.uantwerpen.labplanner;

import be.uantwerpen.labplanner.Service.StorageService;
import be.uantwerpen.labplanner.common.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class LabplannerApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(LabplannerApplication.class, args);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        if (!registry.hasMappingForPattern("/webjars/**")) {
            registry.addResourceHandler("/webjars/**").addResourceLocations
                    ( "classpath:/META-INF/resources/webjars/");
        }
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale((Locale.US));
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Configuration
    protected static class AuthenticationSecurity extends
            GlobalAuthenticationConfigurerAdapter {
        @Autowired
        private SecurityService securityService;
        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception
        { auth.userDetailsService(securityService);
        }
    }

    @Bean
    public NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder)
                NoOpPasswordEncoder.getInstance();
    }

}
