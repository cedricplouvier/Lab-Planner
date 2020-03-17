package be.uantwerpen.labplanner.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CalendarController {
    //Mappings
    @RequestMapping(value="/calendar/weekly", method= RequestMethod.GET)
    public String showDevices(final ModelMap model){
//        model.addAttribute("allDevices", deviceService.findAll());
        return "/Calendar/weekly";
    }
}
