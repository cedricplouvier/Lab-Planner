package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Services.StepService;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

public class StepController {
    @RequestMapping(value="/booking", method= RequestMethod.GET)
    public String showStepPage(final ModelMap model){
        model.addAttribute("allDevices", deviceService.findAll());
        model.addAttribute("Step", new Step());
        return "step";
    }
    @RequestMapping(value={"/users/", "/users/{id}"},
            method= RequestMethod.POST)
    public String addStep(@Valid Step step, BindingResult result, final ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("Step", step);
            return "step-timeslot";
        }
        StepService.save(step);
        return "redirect:/booking";
    }
    @RequestMapping(value="/", method= RequestMethod.POST)
    public String showTimeslot(Step step, final ModelMap model){
            model.addAttribute("Step", step);
            return "step-timeslot";
    }
}
