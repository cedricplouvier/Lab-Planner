package be.uantwerpen.labplanner.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @RequestMapping({"/","/home"})
    public String showHomepage(){
        return "homepage";
    }
    @RequestMapping({"/usermanagement","/usermanagement"})
    public String showUsermanagementPage(){
        return "usermanagement";
    }
    @RequestMapping({"/stockmanagement","/stockmanagement"})
    public String showStockmanagementPage(){
        return "stockmanagement";
    }
    @RequestMapping({"/planingtool","/planningtool"})
    public String showPlanningtoolPage(){
        return "planningtool";
    }
    @RequestMapping({"/devicemanagement","/devicemanagement"})
    public String showDevicemanagementPage(){
        return "devicemanagement";
    }
}
