package be.uantwerpen.labplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@SpringBootApplication
public class LabplannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabplannerApplication.class, args);
    }

    @Configuration
    @EnableWebSecurity
    public class SecurityConfiguration  extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception{
            http.authorizeRequests().antMatchers("/").permitAll();
        }
    }



    @RequestMapping({"/","/products"})
    public String showHomepage(){
        return "products-list";
    }



}
