package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.Service.StepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CalendarController {
    @Autowired
    StepService stepService;
    //Mappings
    @RequestMapping(value = "/calendar/weekly", method = RequestMethod.GET)
    public String showDevices(final ModelMap model) {
        model.addAttribute("allSteps", stepService.findAll());
        return "/Calendar/weekly";
    }
}
