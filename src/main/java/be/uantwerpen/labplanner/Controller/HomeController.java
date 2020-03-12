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
        logger.info("showHomepage");
        return "homepage";
    }
    @RequestMapping({"/usermanagement","/usermanagement"})
    public String showUsermanagementPage(){
        logger.info("showUserManagementPage");
        return "usermanagement";
    }
    @RequestMapping({"/stockmanagement","/stockmanagement"})
    public String showStockmanagementPage(){
        logger.info("showStochmanagementPage");
        return "stockmanagement";
    }
    @RequestMapping({"/planingtool","/planningtool"})
    public String showPlanningtoolPage(){
        logger.info("showPlanningtoolPage");
        return "planningtool";
    }
    @RequestMapping({"/devicemanagement","/devicemanagement"})
    public String showDevicemanagementPage(){
        logger.info("showDevicemanagementPage");
        return "devicemanagement";
    }
}
