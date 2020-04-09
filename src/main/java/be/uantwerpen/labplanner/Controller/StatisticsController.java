package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StatisticsController {

    @Autowired
    private DeviceService deviceService;

    @RequestMapping("/statistics/statistics")
    public String showStatisticsPage(){
        return "/Statistics/statistics";
    }


}
