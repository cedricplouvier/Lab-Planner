package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Repository.DeviceRepository;
import be.uantwerpen.labplanner.Service.DeviceService;
import be.uantwerpen.labplanner.Service.DeviceTypeService;
import be.uantwerpen.labplanner.Service.StepService;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;


@Controller
public class StepController {
    //Services
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private StepService stepService;
    @Autowired
    private DeviceTypeService deviceTypeService;


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
    @RequestMapping(value="/planning", method= RequestMethod.GET)
    public String showStepPage(final ModelMap model){
        model.addAttribute("allDevices", deviceService.findAll());
        model.addAttribute("allDeviceTypes",deviceTypeService.findAll());
        model.addAttribute("Step", new Step());
        return "PlanningTool/planningtool";
    }
    @RequestMapping(value={"/planning" , "/planning/{id}"},method= RequestMethod.POST)
    public String addStep(@Valid Step step, BindingResult result, final ModelMap model){
        User currentUser =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(result.hasErrors()){
            model.addAttribute("allDevices", deviceService.findAll());
            model.addAttribute("allDeviceTypes",deviceTypeService.findAll());
            model.addAttribute("allSteps",stepService.findAll());
            model.addAttribute("Step", new Step());
            return "PlanningTool/planningtool";
        }
        step.setUser(currentUser);
        stepService.save(step);
        model.addAttribute("allDevices", deviceService.findAll());
        model.addAttribute("allDeviceTypes",deviceTypeService.findAll());
        model.addAttribute("allSteps",stepService.findAll());
        model.addAttribute("Step", new Step());
        return "PlanningTool/planningtool";
    }
    @RequestMapping(value="/", method= RequestMethod.POST)
    public String showTimeslot(Step step, final ModelMap model){
            model.addAttribute("Step", step);
            return "/PlanningTool/step-timeslot";
    }


}
