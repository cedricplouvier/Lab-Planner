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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


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
    @PreAuthorize("hasAuthority('Planning - Overview')")
    @RequestMapping(value="/planning", method= RequestMethod.GET)
    public String showStepPage(final ModelMap model){
        model.addAttribute("allDevices", deviceService.findAll());
        model.addAttribute("allDeviceTypes",deviceTypeService.findAll());
        model.addAttribute("Step", new Step());
//        model.addAttribute("startformat", new String());
//        model.addAttribute("endformat", new String());
        return "PlanningTool/planningtool";
    }
    @PreAuthorize("hasAuthority('Planning - Book step/experiment')")
    @RequestMapping(value={"/planning" , "/planning/{id}"},method= RequestMethod.POST)
    public String addStep(@Valid Step step,/*String startf,String endf,*/ BindingResult result, final ModelMap model) throws ParseException {
        User currentUser =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(result.hasErrors() || overlapCheck(step)){
            model.addAttribute("allDevices", deviceService.findAll());
            model.addAttribute("allDeviceTypes",deviceTypeService.findAll());
            model.addAttribute("allSteps",stepService.findAll());
            model.addAttribute("Step", new Step());
            model.addAttribute("Status", new String("Error"));
            if (result.hasErrors())
                model.addAttribute("Message",new String(result.getFieldError().toString()));
            else
                model.addAttribute("Message",new String("Device is already booked in this timeslot"));
            return "redirect:/planning";
        }
        step.setUser(currentUser);
        stepService.save(step);
        model.addAttribute("allDevices", deviceService.findAll());
        model.addAttribute("allDeviceTypes",deviceTypeService.findAll());
        model.addAttribute("allSteps",stepService.findAll());
        model.addAttribute("Step", new Step());
        model.addAttribute("Status", "Success");
        model.addAttribute("Message", "New step has been added.");
        return "redirect:/planning";
    }
    @RequestMapping(value="/", method= RequestMethod.POST)
    public String showTimeslot(Step step, final ModelMap model){
            model.addAttribute("Step", step);
            return "/PlanningTool/step-timeslot";
    }

    public boolean overlapCheck(Step step) throws ParseException {
            Iterable<Step> allSteps=populateSteps();
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
            Date thisStepDateStart=formatter.parse(step.getStart());
            Date thisStepDateStop= formatter.parse(step.getEnd());
        for (Step s : allSteps) {
            if (step.getDevice()==s.getDevice())
            {
                Date startDate = formatter.parse(s.getStart());
                Date stopDate = formatter.parse(s.getEnd());
                if((thisStepDateStart.after(startDate) && thisStepDateStart.before(stopDate)) || (thisStepDateStop.after(startDate) && thisStepDateStop.before(stopDate)) || (startDate.after(thisStepDateStart) && startDate.before(thisStepDateStop)) || (stopDate.after(thisStepDateStart) && stopDate.before(thisStepDateStop)))
                {   //Start of stop van step ligt tussen de start en stop van een al reeds bestaande step -> sws overlap
                    return true;
                }
                if (thisStepDateStart.equals(startDate))
                { //Starten op zelfde datum
                    if(!thisStepDateStop.equals(thisStepDateStart) && !stopDate.equals(startDate)) // Maar eindigen beide niet op deze datum -> sws overlap
                        return true;
                    else {
                        if (thisStepDateStop.equals(thisStepDateStart)) {  // Deze Step stopt ook op deze dag
                            if (Integer.parseInt(step.getEndHour())>Integer.parseInt(s.getStartHour()))
                            {
                                return true;
                            }
                        }
                        if (stopDate.equals(startDate)) {
                            if (Integer.parseInt(s.getEndHour())>Integer.parseInt(step.getStartHour()))
                                return true;
                        }
                        if(thisStepDateStop.equals(thisStepDateStart) && stopDate.equals(startDate))
                        {
                            if ( (Integer.parseInt(s.getStartHour()) <= Integer.parseInt(step.getStartHour())) && (Integer.parseInt(step.getStartHour()) <= Integer.parseInt(s.getEndHour()))) {
                                return true;
                            }
                            if ((Integer.parseInt(s.getStartHour()) <= Integer.parseInt(step.getEndHour())) && (Integer.parseInt(step.getEndHour()) <= Integer.parseInt(s.getEndHour())))
                                return true;
                        }
                    }
                }
            }
        }
        return false;
    }


}
