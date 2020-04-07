package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.*;
import be.uantwerpen.labplanner.Repository.DeviceRepository;
import be.uantwerpen.labplanner.Service.*;
import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.users.Privilege;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.repository.users.UserRepository;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import be.uantwerpen.labplanner.common.service.users.UserService;

import org.h2.util.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private ExperimentService experimentService;
    @Autowired
    private ExperimentTypeService experimentTypeService;
    @Autowired
    private MixtureService mixtureService;
    @Autowired
    private StepTypeService stepTypeService;


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
            model.addAttribute("allDevices", deviceService.findAll());
            model.addAttribute("allDeviceTypes",deviceTypeService.findAll());
            model.addAttribute("Step", new Step());

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
    public String addStep(@Valid Step step, BindingResult result, final ModelMap model, RedirectAttributes ra) throws ParseException {
        User currentUser =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(result.hasErrors() || overlapCheck(step) ){
            model.addAttribute("allDevices", deviceService.findAll());
            model.addAttribute("allDeviceTypes",deviceTypeService.findAll());
            model.addAttribute("allSteps",stepService.findAll());
            model.addAttribute("Step", new Step());
            ra.addFlashAttribute("Status", new String("Error"));
            if (result.hasErrors())
            {
                System.out.println(result.getFieldError().toString());
                ra.addFlashAttribute("Message",new String(result.getFieldError().toString()));
            }

            else
                ra.addFlashAttribute("Message",new String("Device is already booked in this timeslot."));
            return "redirect:/planning";
        }
        step.setUser(currentUser);
        stepService.saveSomeAttributes(step);
        model.addAttribute("allDevices", deviceService.findAll());
        model.addAttribute("allDeviceTypes",deviceTypeService.findAll());
        model.addAttribute("allSteps",stepService.findAll());
        model.addAttribute("Step", new Step());
        ra.addFlashAttribute("Status", "Success");
        String message = new String("New step has been added.");
        ra.addFlashAttribute("Message", message);
        return "redirect:/planning";
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
    @RequestMapping(value = "/planning/experiments", method = RequestMethod.GET)
    public String viewShowExperiments(final ModelMap model){
        model.addAttribute("allExperiments",experimentService.findAll());
        model.addAttribute("allExperimentTypes",experimentTypeService.findAll());
        return "/PlanningTool/planning-exp-list";
    }
    @RequestMapping(value = "/planning/experiments/{id}/delete",method = RequestMethod.GET)
    public String deleteExperimentType(@PathVariable Long id, final ModelMap model,RedirectAttributes ra){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Experiment> Experiments=experimentService.findAll();
        Set<Role> userRoles = currentUser.getRoles();
        Role adminRol = roleService.findByName("Administrator").get();
        boolean isUsed=false;

        if(userRoles.contains(adminRol)){
            Iterator<Experiment> it = Experiments.iterator();
            while (it.hasNext()) {
                Experiment temp = it.next();
                if(temp.getExperimentType().getId()==id){
                    isUsed = true;
                }
            }
            if(isUsed)
            {
                ra.addFlashAttribute("Status", new String("Error"));
                ra.addFlashAttribute("Message",new String("Experiment type is still in use."));
                logger.error(currentUser.getUsername()+" tried to delete experiment type that is still in use.");
            }
            else {
                ExperimentType experimentType = experimentTypeService.findById(id).get();
                List<StepType> stepTypes = experimentType.getStepTypes();
                for(StepType stepType :stepTypes){
                    stepTypeService.delete(stepType.getId());
                }
                experimentTypeService.delete(id);
                ra.addFlashAttribute("Status", new String("Success"));
                ra.addFlashAttribute("Message",new String("Experiment type successfully deleted."));
            }
        }
        model.clear();
        return "redirect:/planning/experiments";

    }
    @RequestMapping(value = "/planning/experiments/put",method = RequestMethod.GET)
    public String viewCreateExperimentType( final ModelMap model){
        List<String> options = new ArrayList<>();
        options.add("No");options.add("Soft");options.add("Hard");
        model.addAttribute("allDevices",deviceService.findAll());
        model.addAttribute("allDeviceTypes",deviceTypeService.findAll());
        model.addAttribute("allMixtures",mixtureService.findAll());
        model.addAttribute("allStepTypes", stepTypeService.findAll());
        model.addAttribute("experimentType",new ExperimentType());
        model.addAttribute("allOptions",options);
        return "/PlanningTool/planning-exp-manage";
    }
    @RequestMapping(value = "/planning/experiments/{id}",method = RequestMethod.GET)
    public String viewEditExperimentType(@PathVariable Long id,final ModelMap model){
        List<String> options = new ArrayList<>();
        options.add("No");options.add("Soft");options.add("Hard");
        model.addAttribute("allDevices",deviceService.findAll());
        model.addAttribute("allDeviceTypes",deviceTypeService.findAll());
        model.addAttribute("allMixtures",mixtureService.findAll());
        model.addAttribute("allStepTypes",stepTypeService.findAll());
        model.addAttribute("experimentType",experimentTypeService.findById(id).get());
        model.addAttribute("allOptions",options);
        return "/PlanningTool/planning-exp-manage";
    }
    @RequestMapping(value = {"/planning/experiments/","/planning/experiments/{id}"},method = RequestMethod.POST)
    public String addNewExperimentType(@Valid ExperimentType experimentType, BindingResult result, ModelMap model, RedirectAttributes ra){

        if (result.hasErrors()) {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message",new String("There was a problem in adding the Experiment Type."));
            System.out.println(result.getFieldError().toString());
            return "redirect:/planning/experiments";
        }
        for(StepType stepType : experimentType.getStepTypes()){stepTypeService.saveNewStepType(stepType);}
        experimentTypeService.saveExperimentType(experimentType);
        ra.addFlashAttribute("Status", new String("Success"));
        ra.addFlashAttribute("Message",new String("Experiment type successfully added."));
        return "redirect:/planning/experiments";
    }


    public boolean overlapCheck(Step step) throws ParseException {
        Iterable<Step> allSteps=populateSteps();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd hh");
        Date thisStepDateStart=formatter.parse(step.getStart()+" "+step.getStartHour());
        Date thisStepDateStop= formatter.parse(step.getEnd()+" "+step.getEndHour());
        for (Step s : allSteps) {
            if (step.getDevice()==s.getDevice())
            {
                Date startDate = formatter.parse(s.getStart()+" "+s.getStartHour());
                Date stopDate = formatter.parse(s.getEnd()+" "+s.getEndHour());
                if((thisStepDateStart.after(startDate) && thisStepDateStart.before(stopDate)) || (thisStepDateStop.after(startDate) && thisStepDateStop.before(stopDate)) || (startDate.after(thisStepDateStart) && startDate.before(thisStepDateStop)) || (stopDate.after(thisStepDateStart) && stopDate.before(thisStepDateStop)))
                {   //Start of stop van step ligt tussen de start en stop van een al reeds bestaande step -> sws overlap
                    return true;
                }
                if( (thisStepDateStart.equals(startDate)) && thisStepDateStop.equals(stopDate))
                {
                    return true;
                }
                if (thisStepDateStart.equals(thisStepDateStop))
                    return true;

            }
        }
        return false;
    }
}
