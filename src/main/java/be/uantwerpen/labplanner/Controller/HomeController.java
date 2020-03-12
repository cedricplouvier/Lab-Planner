package be.uantwerpen.labplanner.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
public class HomeController {
    @RequestMapping({"/","/home"})
    public String showHomepage(){
        return "homepage";
    }
    @RequestMapping("/usermanagement")
    public String showUsermanagementPage(){
        return "usermanagement";
    }
    @RequestMapping("/stockmanagement")
    public String showStockmanagementPage(){
        return "stockmanagement";
    }
    @RequestMapping("/planingtool")
    public String showPlanningtoolPage(){
        return "planningtool";
    }
    @RequestMapping("/devicemanagement")
    public String showDevicemanagementPage(){
        return "redirect:/devices";
    }
}