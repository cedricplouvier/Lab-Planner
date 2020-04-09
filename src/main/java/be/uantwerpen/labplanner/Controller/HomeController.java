package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Service.DeviceService;
import be.uantwerpen.labplanner.Service.DeviceTypeService;
import be.uantwerpen.labplanner.Service.StepService;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import be.uantwerpen.labplanner.common.service.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Controller
public class HomeController {

    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private StepService stepService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DeviceService deviceService;

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
    @RequestMapping("/calendar")
    public String showCalendarPage(){
        logger.info("showCalendar");
        return "redirect:/calendar/weekly";

    }

    @RequestMapping("/planningtool")
    public String showPlanningtoolPage(){
        logger.info("showPlanning");
        return "redirect:/planning/";

    }
    @RequestMapping("/devicemanagement")
    public String showDevicemanagementPage(){
        return "redirect:/devices";
    }

    @RequestMapping(value={"/","/home"},method= RequestMethod.GET)
    public String showStepsHomePage(final ModelMap model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<Step> userSteps = new ArrayList<>();
        List<Step> allsteps = stepService.findAll();

            Iterator<Step> it = allsteps.iterator();
            while (it.hasNext()) {
                Step tempStep = it.next();
                if(tempStep.getUser().equals(user)) {
                    userSteps.add(tempStep);
                }
            }
            model.addAttribute("userSteps", userSteps);
            model.addAttribute("Step", new Step());
            model.addAttribute("currentUser",user.getUsername());

        return "homepage";
    }
}
