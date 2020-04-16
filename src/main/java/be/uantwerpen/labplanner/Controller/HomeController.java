package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.Experiment;
import be.uantwerpen.labplanner.Model.Relation;
import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Service.DeviceService;
import be.uantwerpen.labplanner.Service.ExperimentService;
import be.uantwerpen.labplanner.Service.RelationService;
import be.uantwerpen.labplanner.Service.ReportService;
import be.uantwerpen.labplanner.Service.StepService;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@Controller
public class HomeController {

    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private StepService stepService;
    @Autowired
    private ExperimentService experimentService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private RelationService relationService;

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
        return "redirect:/calendar/weekly";

    }

    @RequestMapping("/planningtool")
    public String showPlanningtoolPage(){
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

        Set<User> students = new HashSet<User>();

        //find all the sutudents from which the user is researcher of.
        List<Relation> relations = relationService.findAll();
        for (Relation relation : relations){
            if (relation.getResearcher().equals(user)){
                students.addAll(relation.getStudents());
            }
        }


        List<Step> studentSteps = new ArrayList<>();

        List<Step> userSteps = new ArrayList<>();
        List<Step> allsteps = stepService.findAll();

            Iterator<Step> it = allsteps.iterator();
            while (it.hasNext()) {
                Step tempStep = it.next();
                if(tempStep.getUser().equals(user)) {
                    userSteps.add(tempStep);
                }
                else if (students.contains(tempStep.getUser())){
                    studentSteps.add(tempStep);
                }
            }



        List<Experiment> studentExperiments = new ArrayList<>();
        List<Experiment> userExperiments = new ArrayList<>();

        List<Experiment> allExperiments = experimentService.findAll();

        Iterator<Experiment> itExp = allExperiments.iterator();
        while (itExp.hasNext()) {
            Experiment tempExp = itExp.next();
            if(tempExp.getUser().equals(user)) {
                userExperiments.add(tempExp);
            }
            else if (students.contains(tempExp.getUser())){
                studentExperiments.add(tempExp);
            }
        }

            model.addAttribute("reportAmount", reportService.findAll().size());
            model.addAttribute("userSteps", userSteps);
            model.addAttribute("Step", new Step());
            model.addAttribute("currentUser",user.getUsername());
            model.addAttribute("studentSteps",studentSteps);

        model.addAttribute("userExperiments", userExperiments);
        model.addAttribute("studentExperiments", studentExperiments);

        return "homepage";
    }
}
