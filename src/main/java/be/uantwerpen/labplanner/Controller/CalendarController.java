package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Service.DeviceService;
import be.uantwerpen.labplanner.Service.DeviceTypeService;
import be.uantwerpen.labplanner.Service.StepService;
import be.uantwerpen.labplanner.common.model.users.User;
import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class CalendarController {
    @Autowired
    StepService stepService;
    @Autowired
    DeviceTypeService deviceTypeService;
    @Autowired
    DeviceService deviceService;
    //Populate
    @ModelAttribute("allDevices")
    public Iterable<Device> populateDevices() {
        return this.deviceService.findAll();
    }
    @ModelAttribute("allDeviceTypes")
    public Iterable<DeviceType> populateDeviceTypes() { return this.deviceTypeService.findAll(); }
    @ModelAttribute("allSteps")
    public Iterable<Step> populateSteps() { return this.stepService.findAll();}
    //Mappings
    @RequestMapping(value = "/calendar/user", method = RequestMethod.GET)
    public String showUserCalendar(final ModelMap model) {
        User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        List<Step> userSteps = new ArrayList<Step>();
        for (Step step: stepService.findAll()) {
            if (step.getUser().getId() ==user.getId()){
                userSteps.add(step);
            }
        }
        model.addAttribute("allSteps", userSteps);
        model.addAttribute("allDevices", deviceService.findAll());
        model.addAttribute("allDeviceTypes",deviceTypeService.findAll());
        model.addAttribute("Step", new Step());

        return "/Calendar/userCalendar";

    }
}
