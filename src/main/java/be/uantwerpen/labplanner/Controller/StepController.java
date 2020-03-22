package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Repository.DeviceRepository;
import be.uantwerpen.labplanner.Service.DeviceService;
import be.uantwerpen.labplanner.Service.DeviceTypeService;
import be.uantwerpen.labplanner.Service.StepService;
import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.users.Privilege;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.repository.users.UserRepository;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import be.uantwerpen.labplanner.common.service.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
public class StepController {
    //Services
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private StepService stepService;
    @Autowired
    private DeviceTypeService deviceTypeService;
    @Autowired
    private RoleService roleService;

    @Autowired
    UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(StepController.class);

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<Step> userSteps = new ArrayList<>();
        List<Step> allsteps = stepService.findAll();
        Set<Role> userRoles = user.getRoles();
        Role adminRol = roleService.findByName("Administrator").get();

        if(userRoles.contains(adminRol)){
            Iterator<Step> it = allsteps.iterator();
            while (it.hasNext()) {
                Step temp = it.next();
                userSteps.add(temp);
            }
            model.addAttribute("userSteps", userSteps);
            model.addAttribute("Step", new Step());
        }
        else {
            Iterator<Step> it = allsteps.iterator();
            while (it.hasNext()) {
            Step temp = it.next();
                if(temp.getUser().equals(user)){
                userSteps.add(temp);
                }
            }
            model.addAttribute("userSteps", userSteps);
            model.addAttribute("Step", new Step());
        }
//        model.addAttribute("startformat", new String());
//        model.addAttribute("endformat", new String());
        return "PlanningTool/planningtool";
    }
    @PreAuthorize("hasAuthority('Planning - Book step/experiment')")
    @RequestMapping(value={"/planning" , "/planning/{id}"},method= RequestMethod.POST)
    public String addStep(@Valid Step step, BindingResult result, final ModelMap model) throws ParseException {
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

    @RequestMapping(value = "/planning/{id}/delete",method = RequestMethod.GET)
    public String deleteStep(@PathVariable long id, final ModelMap model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<Step> allsteps = stepService.findAll();
        Set<Role> userRoles = user.getRoles();
        Role adminRol = roleService.findByName("Administrator").get();

        boolean ownStep = false;

        if(userRoles.contains(adminRol)){
            stepService.delete(id);
        }
        else {
            Iterator<Step> it = allsteps.iterator();
            while (it.hasNext()) {
                Step temp = it.next();
                if(temp.getUser().equals(user) && temp.getId().equals(id)){
                    ownStep = true;
                }
            }
            if(ownStep) {
                stepService.delete(id);
            }
            else{
                logger.error(user.getUsername()+" tried to delete someone elses step or step id doesn't exist");
            }
        }
        model.clear();
        return "redirect:/planning";
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
                        if (thisStepDateStop.equals(thisStepDateStart) && !stopDate.equals(startDate)) {  // Deze Step stopt ook op deze dag
                            if (Integer.parseInt(step.getEndHour())>Integer.parseInt(s.getStartHour()))
                            {
                                return true;
                            }
                        }
                        if (stopDate.equals(startDate) && !thisStepDateStop.equals(thisStepDateStart)) {
                            if (Integer.parseInt(s.getEndHour())>Integer.parseInt(step.getStartHour()))
                                return true;
                        }
                        if(thisStepDateStop.equals(thisStepDateStart) && stopDate.equals(startDate))
                        {
                            if ( (Integer.parseInt(s.getStartHour()) <= Integer.parseInt(step.getStartHour())) && (Integer.parseInt(step.getStartHour()) < Integer.parseInt(s.getEndHour()))) {
                                return true;
                            }
                            if ((Integer.parseInt(s.getStartHour()) < Integer.parseInt(step.getEndHour())) && (Integer.parseInt(step.getEndHour()) <= Integer.parseInt(s.getEndHour())))
                                return true;
                        }
                    }
                }
            }
        }
        return false;
    }


}
