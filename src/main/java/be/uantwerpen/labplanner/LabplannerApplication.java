package be.uantwerpen.labplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class LabplannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabplannerApplication.class, args);
    }


    @RequestMapping({"/","/home"})
    public String showHomepage(){
        return "StockPage";
    }

}
