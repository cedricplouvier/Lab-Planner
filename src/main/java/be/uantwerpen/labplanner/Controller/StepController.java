package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.*;
import be.uantwerpen.labplanner.Repository.ExperimentRepository;
import be.uantwerpen.labplanner.Repository.ExperimentTypeRepository;
import be.uantwerpen.labplanner.Service.*;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.repository.users.UserRepository;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
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
    private ExperimentTypeRepository experimentTypeRepository;
    @Autowired
    private PieceOfMixtureService pieceOfMixtureService;
    @Autowired
    private OwnProductService productService;

    @Autowired
    private RelationService relationService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ExperimentRepository experimentservice;

    private Map<Long,User> editedElements = new HashMap<>();

    private Map<Step,User> deletedSteps = new HashMap<>();

    private Map<Experiment,User> deletedExperiments = new HashMap<>();

    public Map<Long, User> getEditedElements() {
        return editedElements;
    }

    public void setEditedElements(Map<Long, User> editedElements) {
        this.editedElements = editedElements;
    }

    public Map<Step, User> getDeletedSteps() {
        return deletedSteps;
    }

    public void setDeletedSteps(Map<Step, User> deletedSteps) {
        this.deletedSteps = deletedSteps;
    }

    public Map<Experiment, User> getDeletedExperiments() {
        return deletedExperiments;
    }

    public void setDeletedExperiments(Map<Experiment, User> deletedExperiments) {
        this.deletedExperiments = deletedExperiments;
    }

    private Logger logger = LoggerFactory.getLogger(StepController.class);

    //Populate
    @ModelAttribute("allDevices")
    public Iterable<Device> populateDevices() {
        return this.deviceService.findAll();
    }

    @ModelAttribute("allDeviceTypes")
    public Iterable<DeviceType> populateDeviceTypes() {
        return this.deviceTypeService.findAll();
    }

    @ModelAttribute("allSteps")
    public Iterable<Step> populateSteps() {
        return this.stepService.findAll();
    }

    //Mappings
    @PreAuthorize("hasAuthority('Planning - Overview')")
    @RequestMapping(value = "/planning/", method = RequestMethod.GET)
    public String showStepPage(final ModelMap model) {
        model.addAttribute("allDevices", deviceService.findAll());
        model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
        model.addAttribute("Step", new Step());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        //crate list of all Promotor students
        Set<User> students = new HashSet<>();

        List<Relation> relations = relationService.findAll();
        for (Relation relation : relations) {
            if (relation.getResearcher().equals(user)) {
                students.addAll(relation.getStudents());
            }
        }

        List<Step> studentSteps = new ArrayList<>();
        List<Step> userSteps = new ArrayList<>();
        List<Step> allsteps = stepService.findAll();

        List<Experiment> studentExperiments = new ArrayList<>();
        List<Experiment> userExperiments = new ArrayList<>();
        List<Experiment> allExperiments = experimentService.findAll();
        Boolean hasAdmin = false;
        Set<Role> userRoles = user.getRoles();

        for(Role role: userRoles)
        {
            if(role.getName().equals("Administrator"))
            {
                hasAdmin=true;
            }
        }

        if (hasAdmin) {
            Iterator<Step> it = allsteps.iterator();
            while (it.hasNext()) {
                Step temp = it.next();
                userSteps.add(temp);
            }

            Iterator<Experiment> itExp = allExperiments.iterator();
            while (itExp.hasNext()) {
                Experiment temp = itExp.next();
                userExperiments.add(temp);
            }
            model.addAttribute("userSteps", userSteps);
            model.addAttribute("userExperiments", userExperiments);
            model.addAttribute("Step", new Step());
        } else {
            Iterator<Step> it = allsteps.iterator();
            while (it.hasNext()) {
                Step temp = it.next();
                if (temp.getUser().equals(user)) {
                    userSteps.add(temp);
                } else if (students.contains(temp.getUser())) {
                    studentSteps.add(temp);
                }
            }
            Iterator<Experiment> itExp = allExperiments.iterator();
            while (itExp.hasNext()) {
                Experiment temp = itExp.next();
                if (temp.getUser().equals(user)) {
                    userExperiments.add(temp);
                } else if (students.contains(temp.getUser())) {
                    studentExperiments.add(temp);
                }
            }
            model.addAttribute("userSteps", userSteps);
            model.addAttribute("Step", new Step());
            model.addAttribute("studentSteps", studentSteps);


            model.addAttribute("userExperiments", userExperiments);
            model.addAttribute("studentExperiments", studentExperiments);
        }
//        model.addAttribute("startformat", new String());
//        model.addAttribute("endformat", new String());
        return "PlanningTool/planningtool";
    }

    @PreAuthorize("hasAuthority('Planning - Book step/experiment') or hasAuthority('Planning - Adjust step/experiment own') or hasAuthority('Planning - Adjust step/experiment own/promotor') or hasAuthority('Planning - Adjust step/experiment all') ")
    @RequestMapping(value = {"/planning/", "/planning/{id}"}, method = RequestMethod.POST)
    public String addStep(@Valid Step step, BindingResult result, final ModelMap model, RedirectAttributes ra) throws ParseException {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //if new step, add the current student to the step.
        if (step.getUser() == null) {
            step.setUser(currentUser);
        }

        //check for valid input
        if ((step.getStart() == null || step.getEnd() == null || step.getStartHour() == null || step.getEndHour() == null) || (step.getStart().trim().equals("") || step.getEnd().trim().equals("") || step.getStartHour().trim().equals("") || step.getEndHour().trim().equals(""))) {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String("Error while trying to save step."));
            return "redirect:/planning/";
        }

        //check, double booking
        if (overlapCheck(step)) {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String("Error while trying to save Experiment. Problem with double booking of device " + step.getDevice().getDevicename()));
            return "redirect:/planning/";
        }

        //check holidays, weekend and opening hours
        if (dateTimeIsUnavailable(step)) {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", getMessageForSelectedStep(step));

            return "redirect:/planning/";
        }

        //when editing step (step has already ID)
        if (step.getId() != null) {

            //If Step is part of experiment, check Continuity and chronology
            Experiment exp = getExperimentOfStep(step);
            if (exp != null) {
                exp = getExperimentWithChangedStepIfPossible(exp, step);
                //check if steps inside this experiment fulfills continuity
                if (isProblemWithContinuity(exp.getSteps())) {
                    ra.addFlashAttribute("Status", new String("Error"));
                    ra.addFlashAttribute("Message", new String("Error while trying to save step as part of experiment. Problem with continuity"));
                    return "redirect:/planning/";
                }
            }
        }


        if (result.hasErrors()) {
            ra.addFlashAttribute("Status", new String("Error"));
            if (result.hasErrors()) {
                ra.addFlashAttribute("Message", new String(result.getFieldError().toString()));
            } else
                ra.addFlashAttribute("Message", new String("Error while trying to save step."));
            return "redirect:/planning/";
        }

        //Current user can only be, user of the step, the promotor of the user or admin.
        Role adminole = roleService.findByName("Administrator").get();
        Role promotorRole = roleService.findByName("Researcher").get();
        Boolean allowedToEdit = false;

        //Admin can edit all the steps
        if (currentUser.getRoles().contains(adminole)) {
            allowedToEdit = true;
        }

        //user can edit his own step
        else if (step.getUser().equals(currentUser)) {
            allowedToEdit = true;
        }

        //researcher can edit step of one of his students.
        else if (currentUser.getRoles().contains(promotorRole)) {
            //get all the relations of the specific researcher
            List<Relation> relations = relationService.findAll();

            for (Relation relation : relations) {
                //only select relation for specific researcher
                if (relation.getResearcher().equals(currentUser)) {
                    //check if the student is part of the student scope
                    if (relation.getStudents().contains(step.getUser())) {
                        allowedToEdit = true;
                    }
                }
            }

        }

        if (!allowedToEdit) {
            //no rights, so error message & save nothing
            ra.addFlashAttribute("Status", "Error");
            String message = new String("Student has no right to edit step");
            ra.addFlashAttribute("Message", message);
            return "redirect:/planning/";

        }


        stepService.save(step);

        if (editedElements.containsKey(step.getId())){
            editedElements.replace(step.getId(),currentUser);
        }
        else {
            editedElements.put(step.getId(), currentUser);
        }

        ra.addFlashAttribute("Status", "Success");
        String message = new String("Step has been added/edited.");
        ra.addFlashAttribute("Message", message);
        return "redirect:/planning/";
    }


    @PreAuthorize("hasAuthority('Planning - Book step/experiment') or hasAuthority('Planning - Adjust step/experiment own') or hasAuthority('Planning - Adjust step/experiment own/promotor') or hasAuthority('Planning - Adjust step/experiment all') ")
    @RequestMapping(value = "/planning/{id}", method = RequestMethod.GET)
    public String viewEditStep(@PathVariable long id, final ModelMap model, RedirectAttributes ra) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();


        if (stepService.findById(id).isPresent()) {

            Boolean allowedToEdit = allowedToEdit(user, id);

            if (allowedToEdit) {
                model.addAttribute("Step", stepService.findById(id).orElse(null));
                model.addAttribute("allDevices", deviceService.findAll());
                model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
                model.addAttribute("allSteps", stepService.findAll());
                return "PlanningTool/step-manage";
            } else {
                ra.addFlashAttribute("Status", new String("Error"));
                ra.addFlashAttribute("Message", new String("user can not edit specific step!"));
                return "redirect:/planning/";
            }
        } else {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String("user can not edit specific step!"));
            return "redirect:/planning/";
        }
    }

    //check if specific user is allowed to edit.
    boolean allowedToEdit(User user, long id){
        //also check for Researcher.
        Role adminole = roleService.findByName("Administrator").get();
        Role promotorRole = roleService.findByName("Researcher").get();
        Boolean allowedToEdit = false;

        //Admin can edit all the steps
        if (user.getRoles().contains(adminole)) {
            allowedToEdit = true;
        }

        //user can edit his own step
        else if (stepService.findById(id).get().getUser().equals(user)) {
            allowedToEdit = true;
        }

        //researcher can edit step of one of his students.
        else if (user.getRoles().contains(promotorRole)) {
            //get all the relations of the specific researcher
            List<Relation> relations = relationService.findAll();

            for (Relation relation : relations) {
                //only select relation for specific researcher
                if (relation.getResearcher().equals(user)) {
                    //check if the student is part of the student scope
                    if (relation.getStudents().contains(stepService.findById(id).get().getUser())) {
                        allowedToEdit = true;
                    }
                }
            }

        }

        return allowedToEdit;
    }

    @PreAuthorize("hasAuthority('Planning - Delete step/experiment own') or hasAuthority('Planning - Delete step/experiment own/promotor') or hasAuthority('Planning - Delete step/experiment all')")
    @RequestMapping(value = "/planning/{id}/delete", method = RequestMethod.GET)
    public String deleteStep(@PathVariable long id, final ModelMap model, RedirectAttributes ra) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<Step> allsteps = stepService.findAll();
        Set<Role> userRoles = user.getRoles();
        Role adminRol = roleService.findByName("Administrator").get();

        Role promotorRole = roleService.findByName("Researcher").get();

        boolean ownStep = false;

        Step foundStepById = null;
        for (Step tmpStep : allsteps) {
            if (tmpStep.getId() == id)
                foundStepById = tmpStep;
        }

        //If Step is part of experiment, it can't be deleted
        if (foundStepById != null && isStepPartOfExperiment(foundStepById)) {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String(user.getFirstName() + " " + user.getLastName() + " tried to delete step that is part of experiment!"));
            logger.error(user.getUsername() + " tried to delete step that is part of experiment!");
        } else if (userRoles.contains(adminRol)) {
            if (deletedSteps.containsKey(id)){
                deletedSteps.replace(foundStepById,user);
            }
            else {
                deletedSteps.put(foundStepById, user);
            }


            stepService.delete(id);
        } else if (userRoles.contains(promotorRole)) {
            List<Relation> relations = relationService.findAll();
            Set<User> students = new HashSet<>();
            for (Relation relation : relations) {
                if (relation.getResearcher().equals(user)) {
                    students.addAll(relation.getStudents());
                }
            }

            Iterator<Step> it = allsteps.iterator();
            while (it.hasNext()) {
                Step temp = it.next();
                if (students.contains(temp.getUser()) && temp.getId().equals(id)) {
                    ownStep = true;
                }
            }
            if (ownStep) {
                if (deletedSteps.containsKey(id)){
                    deletedSteps.replace(foundStepById,user);
                }
                else {
                    deletedSteps.put(foundStepById, user);
                }
                stepService.delete(id);
            } else {
                ra.addFlashAttribute("Status", new String("Error"));
                ra.addFlashAttribute("Message", new String(user.getFirstName() + " " + user.getLastName() + " tried to delete someone elses step or step id doesn't exist"));
                logger.error(user.getUsername() + " tried to delete someone elses step or step id doesn't exist");
            }


        } else {
            Iterator<Step> it = allsteps.iterator();
            while (it.hasNext()) {
                Step temp = it.next();
                if (temp.getUser().equals(user) && temp.getId().equals(id)) {
                    ownStep = true;
                }
            }
            if (ownStep) {
                if (deletedSteps.containsKey(id)){
                    deletedSteps.replace(foundStepById,user);
                }
                else {
                    deletedSteps.put(foundStepById, user);
                }
                stepService.delete(id);
            } else {
                ra.addFlashAttribute("Status", new String("Error"));
                ra.addFlashAttribute("Message", new String(user.getFirstName() + " " + user.getLastName() + " tried to delete someone elses step or step id doesn't exist"));
                logger.error(user.getUsername() + " tried to delete someone elses step or step id doesn't exist");
            }
        }
        model.clear();
        return "redirect:/planning/";
    }

    @RequestMapping(value = "/planning/experiments", method = RequestMethod.GET)
    public String viewShowExperiments(final ModelMap model) {
        model.addAttribute("allExperiments", experimentService.findAll());
        model.addAttribute("allExperimentTypes", experimentTypeService.findAll());
        return "PlanningTool/planning-exp-list";
    }

    @PreAuthorize("hasAuthority('Planning - Make new experiment')")
    @RequestMapping(value = "/planning/experiments/{id}/delete", method = RequestMethod.GET)
    public String deleteExperimentType(@PathVariable Long id, final ModelMap model, RedirectAttributes ra) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Experiment> Experiments = experimentService.findAll();
        Set<Role> userRoles = currentUser.getRoles();
        Role adminRol = roleService.findByName("Administrator").get();
        boolean isUsed = false;
        boolean hasAdmin = false;
        for (Role userRole : userRoles) {
            if (userRole.getName().equals(adminRol.getName())) {
                hasAdmin = true;
            }
        }

        if (hasAdmin) {
            Iterator<Experiment> it = Experiments.iterator();
            while (it.hasNext()) {
                Experiment temp = it.next();
                if (temp.getExperimentType().getId() == id) {
                    isUsed = true;
                }
            }
            if (isUsed) {
                ra.addFlashAttribute("Status", new String("Error"));
                ra.addFlashAttribute("Message", new String("Experiment type is still in use."));
                logger.error(currentUser.getUsername() + " tried to delete experiment type that is still in use.");
            } else {
                ExperimentType experimentType = experimentTypeService.findById(id).get();
                List<StepType> stepTypes = experimentType.getStepTypes();
                for (StepType stepType : stepTypes) {
                    stepTypeService.delete(stepType.getId());
                }
                experimentTypeService.delete(id);
                ra.addFlashAttribute("Status", new String("Success"));
                ra.addFlashAttribute("Message", new String("Experiment type successfully deleted."));
            }
        } else {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String("You have no rights to delete experiment."));
        }
        model.clear();
        return "redirect:/planning/experiments";

    }


    @RequestMapping(value = "/planning/experiments/book/{id}/delete", method = RequestMethod.GET)
    public String deleteExperiment(@PathVariable Long id, final ModelMap model, RedirectAttributes ra) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Role> userRoles = currentUser.getRoles();
        Role adminRol = roleService.findByName("Administrator").get();
        if (userRoles.contains(adminRol)) {
            Experiment experiment = experimentService.findById(id).get();

            for (PieceOfMixture pom : experiment.getPiecesOfMixture()) {
                pieceOfMixtureService.delete(pom);
            }

            for (Step step : experiment.getSteps()) {
                stepService.delete(step.getId());
            }

            //add amounts back to the stock.
            Map<OwnProduct, Double> productMap = new HashMap<>();

            for (PieceOfMixture piece : experiment.getPiecesOfMixture()) {
                Mixture mix = piece.getMixture();
                List<Composition> compositions = mix.getCompositions();
                for (Composition comp : compositions) {
                    OwnProduct prod = comp.getProduct();
                    if (!productMap.containsKey(prod)) {
                        productMap.put(prod, prod.getStockLevel());
                    }
                    double stocklevel = productMap.get(prod);
                    stocklevel += comp.getAmount() * piece.getMixtureAmount() / 100;
                    productMap.put(prod, stocklevel);
                }
            }
            Iterator it = productMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                OwnProduct prod = (OwnProduct) pair.getKey();
                prod.setStockLevel((Double) pair.getValue());
                productService.save(prod);
            }


            experimentService.delete(id);
            ra.addFlashAttribute("Status", new String("Success"));
            ra.addFlashAttribute("Message", new String("Experiment successfully deleted."));

        } else {

            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String("Error, Experiment was not deleted."));
        }
        model.clear();
        return "redirect:/planning/";

    }


    @RequestMapping(value = "/planning/experiments/book", method = RequestMethod.GET)
    public String viewBookNewExperiment(final ModelMap model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Step> stepList = new ArrayList<Step>();
        //prepare list of empty steps
        for (int i = 0; i < 100; i++) {
            stepList.add(new Step());
        }

        List<Step> userSteps = new ArrayList<Step>();
        List<Step> otherSteps = new ArrayList<Step>();

        for (Step step : stepService.findAll()) {
            if (step.getUser().getId() == currentUser.getId()) {
                userSteps.add(step);
            } else {
                otherSteps.add(step);
            }
        }
        HolidayManager manager = HolidayManager.getInstance(HolidayCalendar.BELGIUM);
        Set<Holiday> holidays = manager.getHolidays(Calendar.getInstance().get(Calendar.YEAR));
        model.addAttribute("allDevices", deviceService.findAll());
        model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
        model.addAttribute("allMixtures", mixtureService.findAll());
        model.addAttribute("allStepTypes", stepTypeService.findAll());
        model.addAttribute("allExperimentTypes", experimentTypeService.findAll());
        model.addAttribute("userSteps", userSteps);
        model.addAttribute("otherSteps", otherSteps);
        model.addAttribute("experiment", new Experiment());
        model.addAttribute("holidays", holidays);
        return "PlanningTool/planning-exp-book";
    }


    @PreAuthorize("hasAuthority('Planning - Book step/experiment') or hasAuthority('Planning - Adjust step/experiment own') or hasAuthority('Planning - Adjust step/experiment own/promotor') or hasAuthority('Planning - Adjust step/experiment all') ")
    @RequestMapping(value = {"/planning/experiments/book", "/planning/experiments/book/{id}"}, method = RequestMethod.POST)
    public String addExperiment(@Valid Experiment experiment, BindingResult result, final ModelMap model, RedirectAttributes ra) throws ParseException {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Locale current = LocaleContextHolder.getLocale();
        Map<OwnProduct, Double> productMap = new HashMap<>();


        if (experiment == null) {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String("Error while trying to save Experiment."));
            return "redirect:/planning/";
        }


        //Error message that is used as a feedback to user when there is a problem with his input
        String errorMessage = "";

        //list of steps that is filled by selected steps and saved when there is no problem with input
        List<Step> tmpListSteps = new ArrayList<Step>();

        if (experiment == null || experiment.getExperimentType() == null) {
            errorMessage = "Error while trying to save Experiment.";
            prepareModelAtributesToRebookExperiment(model, experiment, errorMessage);
            return "PlanningTool/planning-exp-book";
        }

        //check if experiment name is unique
        for (Experiment exp : experimentService.findAll()) {
            //If there is experiment with different ID and same name, it's not unique
            if (experiment.getExperimentname().equals(exp.getExperimentname()) && !Objects.equals(experiment.getId(), exp.getId())) {
                errorMessage = "Experiment with this name already exists";
                prepareModelAtributesToRebookExperiment(model, experiment, errorMessage);
                return "PlanningTool/planning-exp-book";
            }
        }

        //set current experimentType by experimentType id
        for (ExperimentType expType : experimentTypeService.findAll()) {
            if (expType.getId().equals(experiment.getExperimentType().getId())) {
                experiment.setExperimentType(expType);
            }
        }

        //check negative mixture
        for (PieceOfMixture pom : experiment.getPiecesOfMixture()) {
            if (pom.getMixtureAmount() < 0) {
                errorMessage = "Ammount of mixture can't be negative";
                prepareModelAtributesToRebookExperiment(model, experiment, errorMessage);
                return "PlanningTool/planning-exp-book";
            }
        }

        //In case of edit experiment, reset stocklevels, so that eventuel new stock levels can be saved, and stock is not withdrawn multiple times.
        if (experimentService.findByExperimentName(experiment.getExperimentname()).isPresent()) {
            //add amounts back to the stock.
            Experiment previousExperiment = experimentService.findByExperimentName(experiment.getExperimentname()).orElse(null);
            for (PieceOfMixture piece : previousExperiment.getPiecesOfMixture()) {
                Mixture mix = piece.getMixture();
                List<Composition> compositions = mix.getCompositions();
                for (Composition comp : compositions) {
                    OwnProduct prod = comp.getProduct();
                    if (!productMap.containsKey(prod)) {
                        productMap.put(prod, prod.getStockLevel());
                    }
                    double stocklevel = productMap.get(prod);
                    stocklevel += comp.getAmount() * piece.getMixtureAmount() / 100;
                    productMap.put(prod, stocklevel);
                }
            }
        } else {
            for (OwnProduct prod : productService.findAll()) {
                productMap.put(prod, prod.getStockLevel());
            }
        }

        //Both, step size and stepType size has to be same
        if (experiment.getSteps().size() != experiment.getExperimentType().getStepTypes().size()) {
            errorMessage = "Error while trying to save Experiment. Problem with length of steps";
            prepareModelAtributesToRebookExperiment(model, experiment, errorMessage);
            return "PlanningTool/planning-exp-book";
        }

        //if the experiment is new, set current user to experiment. Otherwise keep the user same
        if (experiment.getUser() == null) {
            experiment.setUser(currentUser);
        }

        //Current user can only be, user of the Experiment, the promotor of the user or admin.
        Role adminole = roleService.findByName("Administrator").get();
        Role promotorRole = roleService.findByName("Researcher").get();
        Boolean allowedToEdit = false;
        boolean enoughStock = true;

        //Admin can edit all the experiments
        if (currentUser.getRoles().contains(adminole)) {
            allowedToEdit = true;
        }

        //user can edit his own experiments
        else if (experiment.getUser().equals(currentUser)) {
            allowedToEdit = true;
        }

        //researcher can edit experiment of one of his students.
        else if (currentUser.getRoles().contains(promotorRole)) {
            //get all the relations of the specific researcher
            List<Relation> relations = relationService.findAll();

            for (Relation relation : relations) {
                //only select relation for specific researcher
                if (relation.getResearcher().equals(currentUser)) {
                    //check if the student is part of the student scope
                    if (relation.getStudents().contains(experiment.getUser())) {
                        allowedToEdit = true;
                    }
                }
            }
        }

        if (!allowedToEdit) {
            //no rights, so error message & save nothing
            errorMessage = "Student has no right to edit experiment";
            prepareModelAtributesToRebookExperiment(model, experiment, errorMessage);
            return "/PlanningTool/planning-exp-book";
        }

        //check correctness of steps
        for (Step step : experiment.getSteps()) {

            //proceed only if experiment still satisfies all needs

            //check, if step data are correct
            if ((step.getStart() == null || step.getEnd() == null ||
                    step.getStartHour() == null || step.getEndHour() == null ||
                    step.getStart().trim().equals("") || step.getEnd().trim().equals("") ||
                    step.getStartHour().trim().equals("") || step.getEndHour().trim().equals(""))) {

                errorMessage = "Error while trying to save Experiment. Wrong input";
                prepareModelAtributesToRebookExperiment(model, experiment, errorMessage);
                return "/PlanningTool/planning-exp-book";
            }

            //check, double booking
            if (overlapCheck(step)) {
                errorMessage = "Error while trying to save Experiment. Problem with double booking of device " + step.getDevice().getDevicename();
                prepareModelAtributesToRebookExperiment(model, experiment, errorMessage);
                return "/PlanningTool/planning-exp-book";
            }

            //check holidays, weekend and opening hours
            if (dateTimeIsUnavailable(step)) {
                errorMessage = getMessageForSelectedStep(step);
                prepareModelAtributesToRebookExperiment(model, experiment, errorMessage);
                return "/PlanningTool/planning-exp-book";
            }
            step.setUser(currentUser);
            step.setStepType(experiment.getExperimentType().getStepTypes().get(experiment.getSteps().indexOf(step)));
            tmpListSteps.add(step);

        }

        //check if steps inside this experiment fulfills continuity
        if (isProblemWithContinuity(experiment.getSteps())) {
            errorMessage = "Error while trying to save Experiment. Problem with continuity";
            prepareModelAtributesToRebookExperiment(model, experiment, errorMessage);
            return "/PlanningTool/planning-exp-book";
        }

        //check if enough stock available (if not enoughstock= false)
        //put al the stocklevels in a map

        //withdraw amounts from temp stock
        if (experiment.getPiecesOfMixture() != null)
            for (PieceOfMixture piece : experiment.getPiecesOfMixture()) {
                Mixture mix = piece.getMixture();
                List<Composition> compositions = mix.getCompositions();
                for (Composition comp : compositions) {
                    OwnProduct prod = comp.getProduct();
                    if (!productMap.containsKey(prod)) {
                        productMap.put(prod, prod.getStockLevel());
                    }
                    double stocklevel = productMap.get(prod);
                    stocklevel -= comp.getAmount() * piece.getMixtureAmount() / 100;
                    productMap.put(prod, stocklevel);
                }
            }

        //check if a temp stock level in map is <0, if so, there is unsuficient stock.
        Iterator it = productMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if ((Double) pair.getValue() < 0) {
                enoughStock = false;
            }
        }

        //if not enough stock, change stock levels back to previous edit
        if (!enoughStock) {
            errorMessage = ResourceBundle.getBundle("messages", current).getString("enough.stock");
            prepareModelAtributesToRebookExperiment(model, experiment, errorMessage);
            return "/PlanningTool/planning-exp-book";
        }

        // if experiment fulfills all needs, save it into database

        //withdraw amounts from temp stock
        Iterator it2 = productMap.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry pair = (Map.Entry) it2.next();
            OwnProduct prod = (OwnProduct) pair.getKey();
            prod.setStockLevel((Double) pair.getValue());
            productService.save(prod);
        }

        //Save steps into database
        for (Step step : tmpListSteps) {
            stepService.saveSomeAttributes(step);
        }
        experiment.setSteps(tmpListSteps);

        //Save piecesOfExperiment in database
        if (experiment.getPiecesOfMixture() != null) {
            for (PieceOfMixture pom : experiment.getPiecesOfMixture()) {
                pieceOfMixtureService.save(pom);
            }
        }

        //set Start date and end date of experiment
        experiment.setStartDate(tmpListSteps.get(0).getStart());
        experiment.setEndDate(tmpListSteps.get(tmpListSteps.size() - 1).getEnd());

        //save experiment into database
        experimentService.saveExperiment(experiment);
        ra.addFlashAttribute("Status", "Success");
        String message = new String("Experiment has been added/edited.");
        ra.addFlashAttribute("Message", message);
        return "redirect:/planning/";
    }


    //Method to prepare model atributes when user entered wrong input
    private void prepareModelAtributesToRebookExperiment(final ModelMap model, Experiment experiment, String errorMessage) {
        model.addAttribute("errorMsg", errorMessage);
        model.addAttribute("experiment", experiment);
        model.addAttribute("allDevices", deviceService.findAll());
        model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
        model.addAttribute("allExperiments", experimentService.findAll());
        model.addAttribute("allExperimentTypes", experimentTypeService.findAll());
        model.addAttribute("allMixtures", mixtureService.findAll());
    }

    @PreAuthorize("hasAuthority('Planning - Book step/experiment') or hasAuthority('Planning - Adjust step/experiment own') or hasAuthority('Planning - Adjust step/experiment own/promotor') or hasAuthority('Planning - Adjust step/experiment all') ")
    @RequestMapping(value = "/planning/experiments/book/{id}", method = RequestMethod.GET)
    public String viewEditExperiment(@PathVariable long id, final ModelMap model, RedirectAttributes ra) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();


        if (experimentService.findById(id).isPresent()) {
            //also check for Researcher.
            Role adminole = roleService.findByName("Administrator").get();
            Role promotorRole = roleService.findByName("Researcher").get();
            Boolean allowedToEdit = false;

            //Admin can edit all the experiments
            if (user.getRoles().contains(adminole)) {
                allowedToEdit = true;
            }

            //user can edit his own experiments
            else if (experimentService.findById(id).get().getUser().equals(user)) {
                allowedToEdit = true;
            }

            //researcher can edit experiments of one of his students.
            else if (user.getRoles().contains(promotorRole)) {
                //get all the relations of the specific researcher
                List<Relation> relations = relationService.findAll();

                for (Relation relation : relations) {
                    //only select relation for specific researcher
                    if (relation.getResearcher().equals(user)) {
                        //check if the student is part of the student scope
                        if (relation.getStudents().contains(experimentService.findById(id).get().getUser())) {
                            allowedToEdit = true;
                        }
                    }
                }

            }

            if (allowedToEdit) {
                model.addAttribute("experiment", experimentService.findById(id).orElse(null));
                model.addAttribute("allDevices", deviceService.findAll());
                model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
                model.addAttribute("allExperiments", experimentService.findAll());
                model.addAttribute("allExperimentTypes", experimentTypeService.findAll());
                model.addAttribute("allMixtures", mixtureService.findAll());
                return "PlanningTool/planning-exp-book";
            } else {
                ra.addFlashAttribute("Status", new String("Error"));
                ra.addFlashAttribute("Message", new String("user can not edit specific step!"));
                return "redirect:/planning/";
            }
        } else {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String("user can not edit specific step!"));
            return "PlanningTool/planning-exp-book";
        }
    }


    //Info page for experiment
    @PreAuthorize("hasAuthority('Planning - Book step/experiment') or hasAuthority('Planning - Adjust step/experiment own') or hasAuthority('Planning - Adjust step/experiment own/promotor') or hasAuthority('Planning - Adjust step/experiment all') ")
    @RequestMapping(value = "/planning/experiments/book/info/{id}", method = RequestMethod.GET)
    public String viewExperimentInfo(@PathVariable Long id, final ModelMap model) throws IOException {
        model.addAttribute("experiment", experimentService.findById(id).orElse(null));
        return "PlanningTool/planning-exp-info";
    }

    @PreAuthorize("hasAuthority('Planning - Make new experiment')")
    @RequestMapping(value = "/planning/experiments/put", method = RequestMethod.GET)
    public String viewCreateExperimentType(final ModelMap model) {
        List<String> options = new ArrayList<>();
        options.add("No");
        options.add("Soft (at least)");
        options.add("Soft (at most)");
        options.add("Hard");
        model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
        model.addAttribute("allStepTypes", stepTypeService.findAll());
        model.addAttribute("experimentType", new ExperimentType());
        model.addAttribute("allOptions", options);
        return "PlanningTool/planning-exp-manage";
    }

    @PreAuthorize("hasAuthority('Planning - Make new experiment')")
    @RequestMapping(value = "/planning/experiments/{id}", method = RequestMethod.GET)
    public String viewEditExperimentType(@PathVariable Long id, final ModelMap model) {
        List<String> options = new ArrayList<>();
        options.add("No");
        options.add("Soft (at least)");
        options.add("Soft (at most)");
        options.add("Hard");
        model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
        model.addAttribute("allStepTypes", stepTypeService.findAll());
        model.addAttribute("experimentType", experimentTypeService.findById(id).get());
        model.addAttribute("allOptions", options);
        return "PlanningTool/planning-exp-manage";
    }

    @PreAuthorize("hasAuthority('Planning - Make new experiment')")
    @RequestMapping(value = {"/planning/experiments/", "/planning/experiments/{id}"}, method = RequestMethod.POST)
    public String addNewExperimentType(@Valid ExperimentType experimentType, BindingResult result, ModelMap
            model, RedirectAttributes ra) {

        if (result.hasErrors()) {
            ra.addFlashAttribute("Message", new String("There was a problem in adding the Experiment Type."));
            return "redirect:/planning/experiments/{id}";
        }
        ExperimentType tempExperimentType = experimentType.getId() == null ? null : experimentTypeRepository.findById(experimentType.getId()).orElse(null);
        if (tempExperimentType == null) {
            for (ExperimentType exptyp : experimentTypeService.findAll()) {
                if (experimentType.getExpname().equals(exptyp.getExpname())) {
                    ra.addFlashAttribute("Status", new String("Error"));
                    ra.addFlashAttribute("Message", new String("There was a problem in adding the Experiment Type:\nThis experiment type name is already occupied!"));
                    return "redirect:/planning/experiments/put";
                }
            }
            for (StepType stepType : experimentType.getStepTypes()) {
                if (stepType.getContinuity().getHours() < 0) {
                    ra.addFlashAttribute("Status", new String("Error"));
                    ra.addFlashAttribute("Message", new String("There was a problem in adding the Experiment Type:\nInvalid value for hours."));
                    return "redirect:/planning/experiments/put";
                }
                if (stepType.getContinuity().getMinutes() > 59 || stepType.getContinuity().getMinutes() < 0) {
                    ra.addFlashAttribute("Status", new String("Error"));
                    ra.addFlashAttribute("Message", new String("There was a problem in adding the Experiment Type:\nInvalid value for minutes."));
                    return "redirect:/planning/experiments/put";
                } else
                    stepTypeService.saveNewStepType(stepType);
            }
            ra.addFlashAttribute("Status", new String("Success"));
            ra.addFlashAttribute("Message", new String("Experiment type successfully added."));
        }
        else{


        for (StepType stepType : experimentType.getStepTypes()) {
            if (stepType.getContinuity().getHours() < 0) {
                ra.addFlashAttribute("Status", new String("Error"));
                ra.addFlashAttribute("Message", new String("There was a problem in adding the Experiment Type:\nInvalid value for hours."));
                return "redirect:/planning/experiments/{id}";
            }
            if (stepType.getContinuity().getMinutes() > 59 || stepType.getContinuity().getMinutes() < 0) {
                ra.addFlashAttribute("Status", new String("Error"));
                ra.addFlashAttribute("Message", new String("There was a problem in adding the Experiment Type:\nInvalid value for minutes."));
                return "redirect:/planning/experiments/{id}";
            } else
                stepTypeService.saveNewStepType(stepType);
        }
            ra.addFlashAttribute("Status", new String("Success"));
            ra.addFlashAttribute("Message", new String("Experiment type successfully edited."));
        }
        experimentTypeService.saveExperimentType(experimentType);
        return "redirect:/planning/experiments";
    }

    public boolean overlapCheck(Step step) throws ParseException {
        Iterable<Step> allSteps = populateSteps();
        //in formatter Hours should be in capital letters, lowercase 'hh' is for AM - PM ( so from 0 to 12)
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date thisStepDateStart = formatter.parse(step.getStart() + " " + step.getStartHour());
        Date thisStepDateStop = formatter.parse(step.getEnd() + " " + step.getEndHour());

        for (Step s : allSteps) {
            if ((step.getDevice() == s.getDevice()) && ((step.getId() == null) || (!s.getId().equals(step.getId())))) {
                Date startDate = formatter.parse(s.getStart() + " " + s.getStartHour());
                Date stopDate = formatter.parse(s.getEnd() + " " + s.getEndHour());
                if ((thisStepDateStart.after(startDate) && thisStepDateStart.before(stopDate)) || (thisStepDateStop.after(startDate) && thisStepDateStop.before(stopDate)) || (startDate.after(thisStepDateStart) && startDate.before(thisStepDateStop)) || (stopDate.after(thisStepDateStart) && stopDate.before(thisStepDateStop))) {   //Start of stop van step ligt tussen de start en stop van een al reeds bestaande step -> sws overlap
                    return true;
                }
                if ((thisStepDateStart.equals(startDate)) && thisStepDateStop.equals(stopDate)) {
                    return true;
                }
            }
        }
        return false;
    }

    // return error message that describes Problem with date of selected step
    public String getMessageForSelectedStep(Step step) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        DateTime currentStartDate = formatter.parseDateTime(step.getStart() + " " + step.getStartHour());
        DateTime currentEndDate = formatter.parseDateTime(step.getEnd() + " " + step.getEndHour());

        //check if startDate is before endDate
        if (currentEndDate.isBefore(currentStartDate)) {
            return "Error while trying to save Experiment. Start date " + currentStartDate.toString("yyyy-MM-dd HH:mm") + " is after end date.";
        }
        //check if startDate is not the same as endDate
        if (currentEndDate.equals(currentStartDate)) {
            return "Error while trying to save Experiment. Date " + currentStartDate.toString("yyyy-MM-dd HH:mm") + " is the same as end date.";
        }


        //check opening hours
        if (!isInsideOpeningHours(currentStartDate)) {
            return "Error while trying to save step as a part of experiment. Date " + currentStartDate.toString("yyyy-MM-dd HH:mm") + " is not inside opening hours.";
        }
        if (!isInsideOpeningHours(currentEndDate)) {
            return "Error while trying to save step as a part of experiment. Date " + currentEndDate.toString("yyyy-MM-dd HH:mm") + " is not inside opening hours.";
        }
        //check weekend
        if (isWeekend(currentStartDate)) {
            return "Error while trying to save step as a part of experiment. Date " + currentStartDate.toString("yyyy-MM-dd HH:mm") + " is on weekend.";
        }
        if (isWeekend(currentEndDate)) {
            return "Error while trying to save step as a part of experiment. Date " + currentEndDate.toString("yyyy-MM-dd HH:mm") + " is on weekend.";
        }
        //check holidays
        if (isInsideHoliday(currentStartDate)) {
            return "Error while trying to save step as a part of experiment. Date " + currentStartDate.toString("yyyy-MM-dd HH:mm") + " is at holidays.";

        }
        if (isInsideHoliday(currentEndDate)) {
            return "Error while trying to save step as a part of experiment. Date " + currentEndDate.toString("yyyy-MM-dd HH:mm") + " is at holidays.";

        }

        //CheckOverNight
        if (currentStartDate.getDayOfYear() < currentEndDate.getDayOfYear()) {
            if (!step.getDevice().getDeviceType().getOvernightuse()) {
                return "Error while trying to save step as a part of experiment. Device " + step.getDevice().getDevicename() + " can't be used over nigh.";

            }
        }
        return "Success";
    }

    // returns true, when there is a problem with continuity
    public boolean dateTimeIsUnavailable(Step step) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        DateTime currentStartDate = formatter.parseDateTime(step.getStart() + " " + step.getStartHour());
        DateTime currentEndDate = formatter.parseDateTime(step.getEnd() + " " + step.getEndHour());

        //check if startDate is before endDate
        if (currentEndDate.isBefore(currentStartDate)) {
            return true;
        }
        //check if startDate is not the same as endDate
        if (currentEndDate.equals(currentStartDate)) {
            return true;
        }


        //check opening hours
        if (!isInsideOpeningHours(currentStartDate)) {
            return true;
        }
        if (!isInsideOpeningHours(currentEndDate)) {
            return true;
        }
        //check weekend
        if (isWeekend(currentStartDate)) {
            return true;
        }
        if (isWeekend(currentEndDate)) {
            return true;
        }
        //check holidays
        if (isInsideHoliday(currentStartDate)) {
            return true;
        }
        if (isInsideHoliday(currentEndDate)) {
            return true;
        }

        //CheckOverNight
        if (currentStartDate.getDayOfYear() < currentEndDate.getDayOfYear()) {
            if (!step.getDevice().getDeviceType().getOvernightuse()) {
                return true;
            }
        }
        return false;
    }


    // returns true, when there is a problem with continuity
    public boolean isProblemWithContinuity(List<Step> steps) throws ParseException {
        //No need to check continuity when size is 1
        if (steps.size() == 1)
            return false;
        List<DateTime> ListStartDates = new ArrayList<>();
        List<DateTime> ListEndDates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        //Check rules
        DateTime currentStartDate, currentEndDate, nextStartDate, nextEndDate;
        for (int i = 0; i < steps.size() - 1; i++) {
            currentStartDate = formatter.parseDateTime(steps.get(i).getStart() + " " + steps.get(i).getStartHour());
            currentEndDate = formatter.parseDateTime(steps.get(i).getEnd() + " " + steps.get(i).getEndHour());
            nextStartDate = formatter.parseDateTime(steps.get(i + 1).getStart() + " " + steps.get(i + 1).getStartHour());
            nextEndDate = formatter.parseDateTime(steps.get(i + 1).getEnd() + " " + steps.get(i + 1).getEndHour());
            //previousDates are not set when i == 0.
            //check chronology - current start date shouldn't be after next start date.
            if (currentStartDate.isAfter(nextStartDate) || currentEndDate.isAfter(nextEndDate)) {
                return true;
            }
            switch (steps.get(i).getStepType().getContinuity().getType()) {
                case "Soft (at least)":
                    //nextStartDate - currentStartTime >= currentStartTime
                    if (!(nextStartDate.getMillis() - (currentEndDate.getMillis()) >= 1000 * 60 * 60 * steps.get(i).getStepType().getContinuity().getHours() + 1000 * 60 * steps.get(i).getStepType().getContinuity().getMinutes())) {
                        return true;
                    }
                    break;
                case "Soft (at most)":
                    //nextStartDate - currentStartTime <= currentStartTime
                    if (!(nextStartDate.getMillis() - (currentEndDate.getMillis()) <= 1000 * 60 * 60 * steps.get(i).getStepType().getContinuity().getHours() + 1000 * 60 * steps.get(i).getStepType().getContinuity().getMinutes())) {
                        return true;
                    }
                    break;
                case "Hard":
                    //nextStartDate - currentStartTime == currentStartTime
                    if (!(nextStartDate.getMillis() - (currentEndDate.getMillis()) == 1000 * 60 * 60 * steps.get(i).getStepType().getContinuity().getHours() + 1000 * 60 * steps.get(i).getStepType().getContinuity().getMinutes())) {
                        return true;
                    }
                    break;
                case "No":
                    // no continuity
                    break;
                default:
                    return true;
            }
        }
        return false;
    }


    //Check, if hours are between 9 and 17
    public boolean isInsideOpeningHours(DateTime dateTime) {
        return ((dateTime.getHourOfDay() >= 9 &&
                ((dateTime.getHourOfDay() < 17) || ((dateTime.getHourOfDay() == 17) && (dateTime.getMinuteOfHour() == 0)))) &&
                (dateTime.getDayOfWeek() < 6));
    }

    //Check,if it's weekend
    public boolean isWeekend(DateTime dateTime) {
        return ((dateTime.getDayOfWeek() > 5));
    }

    //Check hollidays
    public boolean isInsideHoliday(DateTime dateTime) {
        HolidayManager m = HolidayManager.getInstance(HolidayCalendar.BELGIUM);
        Set<Holiday> holidays = m.getHolidays(dateTime.getYear());
        for (Holiday tmpHoliday : holidays) {
            if ((tmpHoliday.getDate().getMonth().getValue() == dateTime.getMonthOfYear()) &&
                    (tmpHoliday.getDate().getDayOfMonth() == dateTime.getDayOfMonth())) {
                return true;
            }
        }
        return false;
    }

    private boolean isStepPartOfExperiment(Step step) {
        if (experimentservice != null) {
            List<Experiment> allExperiments = experimentservice.findAll();
            for (Experiment tmpExp : allExperiments) {
                for (Step tmpStep : tmpExp.getSteps()) {
                    if (tmpStep.getId() == step.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Experiment getExperimentOfStep(Step step) {
        if (experimentservice != null) {
            List<Experiment> allExperiments = experimentservice.findAll();
            for (Experiment tmpExp : allExperiments) {
                for (Step tmpStep : tmpExp.getSteps()) {
                    if ((long) tmpStep.getId() == (long) step.getId()) {
                        for (int i = 0; i < tmpExp.getSteps().size(); i++) {
                            tmpExp.getSteps().get(i).setStepType(tmpExp.getExperimentType().getStepTypes().get(i));
                        }
                        return tmpExp;
                    }
                }
            }
        }
        return null;
    }

    private Experiment getExperimentWithChangedStepIfPossible(Experiment exp, Step step) {
        if (exp != null && step != null) {
            Experiment newExp = exp;
            List<Step> tmpSteps = exp.getSteps();
            for (int i = 0; i < tmpSteps.size(); i++) {
                if ((long) tmpSteps.get(i).getId() == (long) step.getId()) {
                    tmpSteps.set(i, step);
                    tmpSteps.get(i).setStepType(exp.getExperimentType().getStepTypes().get(i));
                }
            }
            newExp.setSteps(tmpSteps);
            return newExp;
        }
        return null;
    }
}
