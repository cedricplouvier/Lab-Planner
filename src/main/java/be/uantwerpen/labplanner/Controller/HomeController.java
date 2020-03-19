package be.uantwerpen.labplanner.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {

    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping({"/","/home"})
    public String showHomepage(){
        return "homepage";
    }

    //@PreAuthorize("hasAuthority('User Management')")
    @RequestMapping("/usermanagement")
    public String showUsermanagementPage(){
        return "redirect:/usermanagement/users";
    }
    @RequestMapping("/stockmanagement")
    public String showStockmanagementPage(){
        return "redirect:/products";
    }
    @RequestMapping("/planningtool")
    public String showPlanningtoolPage(){
        return "planningtool";
    }
    @RequestMapping("/devicemanagement")
    public String showDevicemanagementPage(){
        return "redirect:/devices";
    }
}
