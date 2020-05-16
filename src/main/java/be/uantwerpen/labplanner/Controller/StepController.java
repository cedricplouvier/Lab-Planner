package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.*;
import be.uantwerpen.labplanner.Repository.ExperimentRepository;
import be.uantwerpen.labplanner.Service.*;
import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.repository.users.UserRepository;
import be.uantwerpen.labplanner.common.service.stock.ProductService;
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
    private PieceOfMixtureService pieceOfMixtureService;
    @Autowired
    private OwnProductService productService;
    @Autowired
    private EmailController emailController;

    @Autowired
    private RelationService relationService;
    @Autowired
    private SystemSettingsService systemSettingsService;
    @Autowired
    private OfficeHoursService officeHoursService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ExperimentRepository experimentRepository;

    private Map<Step, User> addedSteps = new HashMap<>();
    private Map<Step, User> editedSteps = new HashMap<>();
    private Map<Step, User> deletedSteps = new HashMap<>();

    private Map<Experiment, User> addedExperiments = new HashMap<>();
    private Map<Experiment, User> editedExperiments = new HashMap<>();
    private Map<Experiment, User> deletedExperiments = new HashMap<>();


    public Map<Step, User> getAddedSteps() {
        return addedSteps;
    }

    public void setAddedSteps(Map<Step, User> addedSteps) {
        this.addedSteps = addedSteps;
    }

    public Map<Step, User> getEditedSteps() {
        return editedSteps;
    }

    public void setEditedSteps(Map<Step, User> editedSteps) {
        this.editedSteps = editedSteps;
    }

    public Map<Step, User> getDeletedSteps() {
        return deletedSteps;
    }

    public void setDeletedSteps(Map<Step, User> deletedSteps) {
        this.deletedSteps = deletedSteps;
    }

    public Map<Experiment, User> getAddedExperiments() {
        return addedExperiments;
    }

    public void setAddedExperiments(Map<Experiment, User> addedExperiments) {
        this.addedExperiments = addedExperiments;
    }

    public Map<Experiment, User> getEditedExperiments() {
        return editedExperiments;
    }

    public void setEditedExperiments(Map<Experiment, User> editedExperiments) {
        this.editedExperiments = editedExperiments;
    }

    public Map<Experiment, User> getDeletedExperiments() {
        return deletedExperiments;
    }

    public void setDeletedExperiments(Map<Experiment, User> deletedExperiments) {
        this.deletedExperiments = deletedExperiments;
    }

    public void clearLists() {
        addedSteps.clear();
        editedSteps.clear();
        deletedSteps.clear();
        addedExperiments.clear();
        editedExperiments.clear();
        deletedExperiments.clear();
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
        model.addAttribute("allMixtures", mixtureService.findAll());
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

        for (Role role : userRoles) {
            if (role.getName().equals("Administrator")) {
                hasAdmin = true;
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
        return "PlanningTool/planningtool";
    }

    public void resetStockLevels(Step step) {
        if (step.getAmount() != 0) {
            Mixture mix = step.getMixture();
            for (Composition composition : mix.getCompositions()) {
                OwnProduct prod = composition.getProduct();
                double stocklevel = prod.getStockLevel();
                double reservedlevel = prod.getReservedStockLevel();
                stocklevel += composition.getAmount() * step.getAmount() / 100;
                reservedlevel -= composition.getAmount() * step.getAmount() / 100;
                prod.setStockLevel(stocklevel);
                prod.setReservedStockLevel(reservedlevel);
                productService.save(prod);
            }
        }
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
            ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("steps.inputError")));
            return "redirect:/planning/";
        }

        //check, double booking
        if (overlapCheck(step)) {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("steps.doubleBooking")));
            return "redirect:/planning/";
        }

        //check holidays, weekend and opening hours
        if (dateTimeIsUnavailable(step, currentUser)) {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", getMessageForSelectedStep(step, currentUser));

            return "redirect:/planning/";
        }

        //check for enough stock level
        Boolean enoughStock = true;
        if (step.getAmount() != 0) {
            Mixture mix = step.getMixture();
            for (Composition composition : mix.getCompositions()) {
                double stocklevel = composition.getProduct().getStockLevel();
                stocklevel -= composition.getAmount() * step.getAmount() / 100;
                if (stocklevel < 0) {
                    enoughStock = false;
                }

            }
        }
        if (!enoughStock) {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("enough.stock")));
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
                    ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("step.continuity.problem")));
                    return "redirect:/planning/";
                }
            }
        }


        if (result.hasErrors()) {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String(result.getFieldError().toString()));
            return "redirect:/planning/";
        }
        if (step.getAmount() < 0) {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("step.amount.negative")));
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
            ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("steps.notAllowed")));
            return "redirect:/planning/";

        }

        if (step.getId() != null) {
            //this is edit of existing step, so reset levels so new levels can be saved
            Step previousStep = stepService.findById(step.getId()).orElse(null);
            resetStockLevels(previousStep);
        }

        if (step.getAmount() != 0) {
            Mixture mix = step.getMixture();
            for (Composition composition : mix.getCompositions()) {
                OwnProduct prod = composition.getProduct();
                double stocklevel = prod.getStockLevel();
                double reservedlevel = prod.getReservedStockLevel();
                stocklevel -= composition.getAmount() * step.getAmount() / 100;
                reservedlevel += composition.getAmount() * step.getAmount() / 100;
                prod.setStockLevel(stocklevel);
                prod.setReservedStockLevel(reservedlevel);
                productService.save(prod);
            }
        }

        List<OwnProduct> products = productService.findAll();
        for (OwnProduct tempProd : products) {
            if (tempProd.getStockLevel() < tempProd.getLowStockLevel()) {
                emailController.sendLowStockEmail(tempProd, step.getUser(), "step");
            }
        }

        boolean newStep = step.isNew();

        stepService.save(step);
        if (newStep) {
            addedSteps.put(step, currentUser);
        } else {

            if (editedSteps.containsKey(step)) {
                editedSteps.replace(step, currentUser);
            } else {
                editedSteps.put(step, currentUser);
            }
        }

        ra.addFlashAttribute("Status", "Success");
        ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("steps.success")));
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
                model.addAttribute("allMixtures", mixtureService.findAll());
                return "PlanningTool/step-manage";
            } else {
                ra.addFlashAttribute("Status", new String("Error"));
                ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("steps.EditError")));
                return "redirect:/planning/";
            }
        } else {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("steps.foundError")));
            return "redirect:/planning/";
        }
    }

    //check if specific user is allowed to edit.
    boolean allowedToEdit(User user, long id) {

        //also check for Researcher.
        Role adminole = roleService.findByName("Administrator").get();
        Role promotorRole = roleService.findByName("Researcher").get();
        Boolean allowedToEdit = false;

        //Admin can edit all the steps
        if (user.getRoles().contains(adminole)) {
            allowedToEdit = true;
        }

        Step step = stepService.findById(id).orElse(null);
        if (step != null) {
            //user can edit his own step
            if (step.getUser().equals(user)) {
                allowedToEdit = true;
            }
            //researcher can edit step of one of his students.
            if (user.getRoles().contains(promotorRole)) {
                //get all the relations of the specific researcher
                List<Relation> relations = relationService.findAll();

                for (Relation relation : relations) {
                    //only select relation for specific researcher
                    if (relation.getResearcher().equals(user)) {
                        //check if the student is part of the student scope
                        if (relation.getStudents().contains(step.getUser())) {
                            allowedToEdit = true;
                        }
                    }
                }

            }
        } else {
            Experiment experiment = experimentService.findById(id).orElse(null);
            if (experiment != null) {
                if (experiment.getUser().equals(user))
                    allowedToEdit = true;
            }
            //researcher can edit step of one of his students.
            if (user.getRoles().contains(promotorRole)) {
                //get all the relations of the specific researcher
                List<Relation> relations = relationService.findAll();

                for (Relation relation : relations) {
                    //only select relation for specific researcher
                    if (relation.getResearcher().equals(user)) {
                        //check if the student is part of the student scope
                        if (relation.getStudents().contains(experiment.getUser())) {
                            allowedToEdit = true;
                        }
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

        Step foundStepById = stepService.findById(id).orElse(null);
        if (foundStepById == null) {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("steps.foundError")));
            return "redirect:/planning/";
        }


        List<Step> allsteps = stepService.findAll();
        Set<Role> userRoles = user.getRoles();
        Role adminRol = roleService.findByName("Administrator").get();

        Role promotorRole = roleService.findByName("Researcher").get();

        boolean ownStep = false;


        //If Step is part of experiment, it can't be deleted
        if (foundStepById != null && isStepPartOfExperiment(foundStepById)) {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String(user.getFirstName() + " " + user.getLastName() + ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("steps.deleteExperimentError")));
            logger.error(user.getUsername() + " tried to delete step that is part of experiment!");
        } else if (userRoles.contains(adminRol)) {
            if (deletedSteps.containsKey(foundStepById)) {
                deletedSteps.replace(foundStepById, user);
            } else {
                deletedSteps.put(foundStepById, user);
            }


            ra.addFlashAttribute("Status", new String("Success"));
            ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("steps.deleted")));
            resetStockLevels(foundStepById);
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
                if (deletedSteps.containsKey(foundStepById)) {
                    deletedSteps.replace(foundStepById, user);
                } else {
                    deletedSteps.put(foundStepById, user);
                }
                ra.addFlashAttribute("Status", new String("Success"));
                ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("steps.deleted")));
                resetStockLevels(foundStepById);
                stepService.delete(id);
            } else {
                ra.addFlashAttribute("Status", new String("Error"));
                ra.addFlashAttribute("Message", new String(user.getFirstName() + " " + user.getLastName() + " " + ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("steps.deleteError")));
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
                if (deletedSteps.containsKey(foundStepById)) {
                    deletedSteps.replace(foundStepById, user);
                } else {
                    deletedSteps.put(foundStepById, user);
                }
                ra.addFlashAttribute("Status", new String("Success"));
                ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("steps.deleted")));
                resetStockLevels(foundStepById);
                stepService.delete(id);
            } else {
                ra.addFlashAttribute("Status", new String("Error"));
                ra.addFlashAttribute("Message", new String(user.getFirstName() + " " + user.getLastName() + " " + ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("steps.deleteError")));
                logger.error(user.getUsername() + " tried to delete someone elses step or step id doesn't exist");
            }
        }

        //check if deleted step also in added or editedstep map & delete
        addedSteps.remove(foundStepById);
        editedSteps.remove(foundStepById);

        model.clear();
        return "redirect:/planning/";
    }


    @RequestMapping(value = "/planning/experiments", method = RequestMethod.GET)
    public String viewShowExperiments(final ModelMap model) {
        model.addAttribute("allExperiments", experimentService.findAll());
        model.addAttribute("allExperimentTypes", allFixedExperimentTypes());
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
                if (temp.getExperimentType().getId().equals(id)) {
                    isUsed = true;
                }
            }
            if (isUsed) {
                ra.addFlashAttribute("Status", new String("Error"));
                ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("experiment.type.inuse")));
                logger.error(currentUser.getUsername() + " tried to delete experiment type that is still in use.");
            } else {
                ExperimentType experimentType = experimentTypeService.findById(id).get();
                List<StepType> stepTypes = experimentType.getStepTypes();
                for (StepType stepType : stepTypes) {
                    stepTypeService.delete(stepType.getId());
                }
                experimentTypeService.delete(id);
                ra.addFlashAttribute("Status", new String("Success"));
                ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("experiment.type.delete.success")));

            }
        } else {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("experiment.type.delete.norights")));
        }
        model.clear();
        return "redirect:/planning/experiments";

    }


    @RequestMapping(value = "/planning/experiments/book/{id}/delete", method = RequestMethod.GET)
    public String deleteExperiment(@PathVariable Long id, final ModelMap model, RedirectAttributes ra) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Role> userRoles = currentUser.getRoles();
        Role adminRol = roleService.findByName("Administrator").get();
        Experiment experiment = experimentService.findById(id).orElse(null);
        if (experiment != null) {
            if (allowedToEdit(currentUser, id)) {
                if (experiment.getPiecesOfMixture() != null) {
                    for (PieceOfMixture pom : experiment.getPiecesOfMixture()) {
                        pieceOfMixtureService.delete(pom);
                    }
                }
                for (Step step : experiment.getSteps()) {
                    stepService.delete(step.getId());
                }
                //if it is custom experiment, delete experimentType as well
                if (!experiment.getExperimentType().getIsFixedType()) {
                    for (StepType stepType : experiment.getExperimentType().getStepTypes()) {
                        stepTypeService.delete(stepType.getId());
                    }

                    ExperimentType expTypeToDelete = experiment.getExperimentType();
                    experiment.setExperimentType(null);
                    experimentTypeService.delete(expTypeToDelete.getId());
                }


                //add amounts back to the stock, if piece of mixture is not null
                if (experiment.getPiecesOfMixture() != null) {
                    Map<OwnProduct, Double> productMapStock = new HashMap<>();
                    Map<OwnProduct, Double> productMapReserved = new HashMap<>();


                    for (PieceOfMixture piece : experiment.getPiecesOfMixture()) {
                        Mixture mix = piece.getMixture();
                        List<Composition> compositions = mix.getCompositions();
                        for (Composition comp : compositions) {
                            OwnProduct prod = comp.getProduct();
                            if (!productMapStock.containsKey(prod)) {
                                productMapStock.put(prod, prod.getStockLevel());
                            }
                            if (!productMapReserved.containsKey(prod)) {
                                productMapReserved.put(prod, prod.getReservedStockLevel());
                            }
                            double stocklevel = productMapStock.get(prod);
                            double reservedLevel = productMapReserved.get(prod);
                            stocklevel += comp.getAmount() * piece.getMixtureAmount() / 100;
                            reservedLevel -= comp.getAmount() * piece.getMixtureAmount() / 100;
                            productMapStock.put(prod, stocklevel);
                            productMapReserved.put(prod, reservedLevel);
                        }
                    }
                    Iterator it = productMapStock.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        OwnProduct prod = (OwnProduct) pair.getKey();
                        prod.setStockLevel((Double) pair.getValue());
                        productService.save(prod);
                    }
                    //add amounts to reserved stock
                    Iterator it3 = productMapReserved.entrySet().iterator();
                    while (it3.hasNext()) {
                        Map.Entry pair = (Map.Entry) it3.next();
                        OwnProduct prod = (OwnProduct) pair.getKey();
                        prod.setReservedStockLevel((Double) pair.getValue());
                        productService.save(prod);
                    }
                }


                // add deleted experiment to list
                if (deletedExperiments.containsKey(experiment)) {
                    deletedExperiments.replace(experiment, currentUser);
                } else {
                    deletedExperiments.put(experiment, currentUser);
                }
                addedExperiments.remove(experiment);
                editedExperiments.remove(experiment);
                experimentService.delete(id);
                ra.addFlashAttribute("Status", new String("Success"));
                ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("experiment.deleteSuccess")));

            } else {
                ra.addFlashAttribute("Status", new String("Error"));
                ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("experiment.deleteUnauthorized")));
            }
        } else {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("experiment.deleteInvalidId") + id + "."));
        }
        model.clear();
        return "redirect:/planning/";

    }


    @RequestMapping(value = "/planning/experiments/book/fixed", method = RequestMethod.GET)
    public String viewBookFixedExperiment(final ModelMap model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Step> userSteps = new ArrayList<Step>();
        List<Step> otherSteps = new ArrayList<Step>();

        for (Step step : stepService.findAll()) {
            if (step.getUser().getId() == currentUser.getId()) {
                userSteps.add(step);
            } else {
                otherSteps.add(step);
            }
        }
        ArrayList<String> accessRights = new ArrayList<String>();

        for (Role role : currentUser.getRoles()) {
            accessRights.add(role.getName());
        }

        HolidayManager manager = HolidayManager.getInstance(HolidayCalendar.BELGIUM);
        Set<Holiday> holidays = manager.getHolidays(Calendar.getInstance().get(Calendar.YEAR));
        model.addAttribute("allDevices", deviceService.findAll());
        model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
        model.addAttribute("allMixtures", mixtureService.findAll());
        model.addAttribute("allStepTypes", stepTypeService.findAll());
        model.addAttribute("allExperimentTypes", allFixedExperimentTypes());
        model.addAttribute("userAccessRights", accessRights);
        model.addAttribute("userSteps", userSteps);
        model.addAttribute("otherSteps", otherSteps);
        model.addAttribute("experiment", new Experiment());
        model.addAttribute("holidays", holidays);


        return "PlanningTool/planning-exp-book-fixed";
    }

    @RequestMapping(value = "/planning/experiments/book/custom", method = RequestMethod.GET)
    public String viewBookCustomExperiment(final ModelMap model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Step> userSteps = new ArrayList<Step>();
        List<Step> otherSteps = new ArrayList<Step>();

        for (Step step : stepService.findAll()) {
            if (step.getUser().getId() == currentUser.getId()) {
                userSteps.add(step);
            } else {
                otherSteps.add(step);
            }
        }
        ArrayList<String> accessRights = new ArrayList<String>();

        for (Role role : currentUser.getRoles()) {
            accessRights.add(role.getName());
        }
        // Add options for continuity and fixed step length
        List<String> typeOptions = new ArrayList<>();
        typeOptions.add("No");
        typeOptions.add("Soft (at least)");
        typeOptions.add("Soft (at most)");
        typeOptions.add("Hard");

        List<String> allFixedTimeTypeOptions = new ArrayList<>();
        allFixedTimeTypeOptions.add("Equal");
        allFixedTimeTypeOptions.add("At least");
        allFixedTimeTypeOptions.add("At most");

        List<String> directionOptions = new ArrayList<>();
        directionOptions.add("After");
        directionOptions.add("Before");

        model.addAttribute("allTypeOptions", typeOptions);
        model.addAttribute("allFixedTimeTypeOptions", allFixedTimeTypeOptions);
        model.addAttribute("allTypeDirections", directionOptions);


        HolidayManager manager = HolidayManager.getInstance(HolidayCalendar.BELGIUM);
        Set<Holiday> holidays = manager.getHolidays(Calendar.getInstance().get(Calendar.YEAR));
        model.addAttribute("allDevices", deviceService.findAll());
        model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
        model.addAttribute("allMixtures", mixtureService.findAll());
        model.addAttribute("allStepTypes", stepTypeService.findAll());
        model.addAttribute("allExperimentTypes", allFixedExperimentTypes());
        model.addAttribute("userAccessRights", accessRights);
        model.addAttribute("userSteps", userSteps);
        model.addAttribute("otherSteps", otherSteps);
        model.addAttribute("experiment", new Experiment());
        model.addAttribute("holidays", holidays);
        return "PlanningTool/planning-exp-book-custom";
    }


    @PreAuthorize("hasAuthority('Planning - Book step/experiment') or hasAuthority('Planning - Adjust step/experiment own') or hasAuthority('Planning - Adjust step/experiment own/promotor') or hasAuthority('Planning - Adjust step/experiment all') ")
    @RequestMapping(value = {"/planning/experiments/book", "/planning/experiments/book/{id}"}, method = RequestMethod.POST)
    public String addExperiment(@Valid Experiment experiment, BindingResult result, final ModelMap model, RedirectAttributes ra) throws ParseException {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Locale current = LocaleContextHolder.getLocale();

        Map<OwnProduct, Double> productMapStock = new HashMap<>();
        Map<OwnProduct, Double> productMapReserved = new HashMap<>();

        if (experiment == null) {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("experiment.save.error")));
            return "redirect:/planning/";
        }

        //when editting experiment that is not in database
        if (experiment.getId() != null && !experimentService.findById(experiment.getId()).isPresent()) {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("experiment.edit.error")));
            return "redirect:/planning/";
        }


        //Error message that is used as a feedback to user when there is a problem with his input
        String errorMessage = "";

        //list of steps that is filled by selected steps and saved when there is no problem with input
        List<Step> tmpListSteps = new ArrayList<Step>();

        //if the experiment is new, set current user to experiment. Otherwise keep the user same
        if (experiment.getUser() == null) {
            experiment.setUser(currentUser);
        }

        //Split steps on userSteps and otherSteps
        //user steps - steps of user that has booked or want's to book this experiment
        List<Step> userSteps = new ArrayList<Step>();
        List<Step> otherSteps = new ArrayList<Step>();
        for (Step step : stepService.findAll()) {
            if (step.getUser().getId() == experiment.getUser().getId()) {
                userSteps.add(step);
            } else {
                otherSteps.add(step);
            }
        }

        //get roles of currentUser
        ArrayList<String> accessRights = new ArrayList<String>();

        for (Role role : currentUser.getRoles()) {
            accessRights.add(role.getName());
        }

        if (experiment == null || experiment.getExperimentType() == null) {
            errorMessage = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("experiment.save.error");

            prepareModelAtributesToRebookExperiment(model, experiment, errorMessage, userSteps, otherSteps, accessRights);
                return "PlanningTool/planning-exp-book-custom";

        }


        //load experimentType by selected id (more secure than use the one from frontend)
        if (experiment.getExperimentType().getIsFixedType()) {
            for (ExperimentType expType : experimentTypeService.findAll()) {
                if (expType.getId().equals(experiment.getExperimentType().getId())) {
                    experiment.setExperimentType(expType);
                }
            }
        }

        //check, if there are some steps in experiment
        if (experiment.getSteps() == null || experiment.getExperimentType().getStepTypes() == null) {
            errorMessage = ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.booking.nosteps");
            prepareModelAtributesToRebookExperiment(model, experiment, errorMessage, userSteps, otherSteps, accessRights);

            if (experiment.getExperimentType().getIsFixedType()) {
                return "PlanningTool/planning-exp-book-fixed";
            } else {
                return "PlanningTool/planning-exp-book-custom";
            }
        }


        //If this is custom experiment, some modifications needs to be performed
        if (!experiment.getExperimentType().getIsFixedType()) {

            //1. set name of experimentType if it's null or empty
            if (experiment.getExperimentType().getExpname() == null || experiment.getExperimentType().getExpname() == "") {
                int nameIndex = 0;
                String expTypeName = "";
                // check, if name already exists
                boolean nameFound = false;
                boolean nameAlreadyExists;
                while (!nameFound) {
                    nameAlreadyExists = false;
                    expTypeName = "Custom" + nameIndex;
                    for (int i = 0; i < experimentTypeService.findAll().size(); i++) {
                        if (experimentTypeService.findAll().get(i).getExpname().equals(expTypeName)) {
                            nameAlreadyExists = true;
                        }
                    }
                    if (!nameAlreadyExists) {
                        nameFound = true;
                    }
                    nameIndex++;
                }
                experiment.getExperimentType().setExpname(expTypeName);
            }

            //2. adjust stepTypes according to received experiment model
            for (int i = 0; i < experiment.getExperimentType().getStepTypes().size(); i++) {
                //if stepType is null delete it from list (happens when row was deleted during booking custom exp.)
                if (experiment.getExperimentType().getStepTypes().get(i) == null || experiment.getSteps().get(i) == null || experiment.getExperimentType().getStepTypes().get(i).getStepTypeName() == null) {
                    experiment.getExperimentType().getStepTypes().remove(i);
                    experiment.getSteps().remove(i);
                    i--; //Size of array was changed, so same index needs to be check again
                }
                //if continuity is null, create default continuity
                else if (experiment.getExperimentType().getStepTypes().get(i).getContinuity() == null) {
                    experiment.getExperimentType().getStepTypes().get(i).setContinuity(new Continuity());
                }
                //if continuity is not null, create new ID for it, because this continuity can be deleted wit custom exp.
                else if (experiment.getExperimentType().getStepTypes().get(i).getContinuity() != null) {
                    Continuity tmpCont = new Continuity();

                    tmpCont.setMinutes(experiment.getExperimentType().getStepTypes().get(i).getContinuity().getMinutes());
                    tmpCont.setHours(experiment.getExperimentType().getStepTypes().get(i).getContinuity().getHours());
                    tmpCont.setType(experiment.getExperimentType().getStepTypes().get(i).getContinuity().getType());
                    tmpCont.setDirectionType(experiment.getExperimentType().getStepTypes().get(i).getContinuity().getDirectionType());

                    experiment.getExperimentType().getStepTypes().get(i).setContinuity(tmpCont);
                }
            }

        }

        //If experiment was already booked remove steps from previous booking that are now not used anymore
        if (experiment.getId() != null) {
            Experiment expFromPreviousBooking = experimentService.findById(experiment.getId()).orElse(null);
            if (expFromPreviousBooking != null) {
                for (Step previousStep : expFromPreviousBooking.getSteps()) {
                    boolean stepStillInUse = false;
                    for (Step currentStep : experiment.getSteps()) {
                        //if previous step is still present, keep it
                        if (currentStep.getId() != null && currentStep.getId().equals(previousStep.getId())) {
                            stepStillInUse = true;
                        }
                    }
                    //if previous step is not in use, delete it
                    if (!stepStillInUse) {
                        System.out.println("Step with id " + previousStep.getId() + " was deleted");
                        stepService.delete(previousStep.getId());
                    }
                }
            }
        }


        //check if experiment name is unique
        for (Experiment exp : experimentService.findAll()) {
            //If there is experiment with different ID and same name, it's not unique
            if (experiment.getExperimentname().equals(exp.getExperimentname()) && !Objects.equals(experiment.getId(), exp.getId())) {
                errorMessage = ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.name.alreadyexists");
                prepareModelAtributesToRebookExperiment(model, experiment, errorMessage, userSteps, otherSteps, accessRights);

                if (experiment.getExperimentType().getIsFixedType()) {
                    return "PlanningTool/planning-exp-book-fixed";
                } else {
                    return "PlanningTool/planning-exp-book-custom";
                }
            }
        }

        //set current experimentType by experimentType id,
        //if it's fixed type (custom can be adjusted in frontend, no need to load it in backend)
        if (experiment.getExperimentType().getIsFixedType()) {
            for (ExperimentType expType : experimentTypeService.findAll()) {
                if (expType.getId().equals(experiment.getExperimentType().getId())) {
                    experiment.setExperimentType(expType);
                }
            }
        }

        //check negative mixture
        if (experiment.getPiecesOfMixture() != null) {
            for (PieceOfMixture pom : experiment.getPiecesOfMixture()) {
                if (pom.getMixtureAmount() < 0) {
                    errorMessage = ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("step.amount.negative");
                    prepareModelAtributesToRebookExperiment(model, experiment, errorMessage, userSteps, otherSteps, accessRights);

                    if (experiment.getExperimentType().getIsFixedType()) {
                        return "PlanningTool/planning-exp-book-fixed";
                    } else {
                        return "PlanningTool/planning-exp-book-custom";
                    }
                }
            }
        }

        if (experiment.getPiecesOfMixture() != null) {
            for (PieceOfMixture pom : experiment.getPiecesOfMixture()) {
                if (pom.getMixtureAmount() < 0) {
                    errorMessage = ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("step.amount.negative");
                    prepareModelAtributesToRebookExperiment(model, experiment, errorMessage, userSteps, otherSteps, accessRights);

                    if (experiment.getExperimentType().getIsFixedType()) {
                        return "PlanningTool/planning-exp-book-fixed";
                    } else {
                        return "PlanningTool/planning-exp-book-custom";
                    }
                }
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
                    if (!productMapStock.containsKey(prod)) {
                        productMapStock.put(prod, prod.getStockLevel());
                    }
                    if (!productMapReserved.containsKey(prod)) {
                        productMapReserved.put(prod, prod.getReservedStockLevel());
                    }
                    double stocklevel = productMapStock.get(prod);
                    double reservedStocklevel = productMapReserved.get(prod);
                    stocklevel += comp.getAmount() * piece.getMixtureAmount() / 100;
                    reservedStocklevel -= comp.getAmount() * piece.getMixtureAmount() / 100;

                    productMapStock.put(prod, stocklevel);
                    productMapReserved.put(prod, reservedStocklevel);
                }
            }
        } else {
            for (OwnProduct prod : productService.findAll()) {
                productMapStock.put(prod, prod.getStockLevel());
                productMapReserved.put(prod, prod.getReservedStockLevel());
            }
        }

        //Both, step size and stepType size has to be same
        if (experiment.getSteps().size() != experiment.getExperimentType().getStepTypes().size()) {
            errorMessage = ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.steps.lengthproblem");
            prepareModelAtributesToRebookExperiment(model, experiment, errorMessage, userSteps, otherSteps, accessRights);

            if (experiment.getExperimentType().getIsFixedType()) {
                return "PlanningTool/planning-exp-book-fixed";
            } else {
                return "PlanningTool/planning-exp-book-custom";
            }

        }

        //if the experiment is new, set current user to experiment.
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
            errorMessage = ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.edit.norights");
            prepareModelAtributesToRebookExperiment(model, experiment, errorMessage, userSteps, otherSteps, accessRights);

            if (experiment.getExperimentType().getIsFixedType()) {
                return "PlanningTool/planning-exp-book-fixed";
            } else {
                return "PlanningTool/planning-exp-book-custom";
            }
        }

        int index = 0;
        //check correctness of steps
        for (Step step : experiment.getSteps()) {

            //check, if step data are correct
            if ((step.getDevice() == null || step.getStart() == null || step.getEnd() == null ||
                    step.getStartHour() == null || step.getEndHour() == null ||
                    step.getStart().trim().equals("") || step.getEnd().trim().equals("") ||
                    step.getStartHour().trim().equals("") || step.getEndHour().trim().equals(""))) {

                errorMessage = ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.wrong.input");
                prepareModelAtributesToRebookExperiment(model, experiment, errorMessage, userSteps, otherSteps, accessRights);

                if (experiment.getExperimentType().getIsFixedType()) {
                    return "PlanningTool/planning-exp-book-fixed";
                } else {
                    return "PlanningTool/planning-exp-book-custom";
                }
            }

            if (isPorblemWithFixedLength(experiment.getExperimentType().getStepTypes().get(index), step)) {
                errorMessage = ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.error.fixedtime1") + experiment.getExperimentType().getStepTypes().get(index).getStepTypeName() +ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.error.fixedtime2");

                        prepareModelAtributesToRebookExperiment(model, experiment, errorMessage, userSteps, otherSteps, accessRights);

                if (experiment.getExperimentType().getIsFixedType()) {
                    return "PlanningTool/planning-exp-book-fixed";
                } else {
                    return "PlanningTool/planning-exp-book-custom";
                }
            }

            //check, double booking
            if (overlapCheck(step)) {
                errorMessage = ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.save.doublebooking")+ step.getDevice().getDevicename();
                prepareModelAtributesToRebookExperiment(model, experiment, errorMessage, userSteps, otherSteps, accessRights);

                if (experiment.getExperimentType().getIsFixedType()) {
                    return "PlanningTool/planning-exp-book-fixed";
                } else {
                    return "PlanningTool/planning-exp-book-custom";
                }
            }

            //check holidays, weekend and opening hours
            if (dateTimeIsUnavailable(step, currentUser)) {
                errorMessage = getMessageForSelectedStep(step, currentUser);
                prepareModelAtributesToRebookExperiment(model, experiment, errorMessage, userSteps, otherSteps, accessRights);

                if (experiment.getExperimentType().getIsFixedType()) {
                    return "PlanningTool/planning-exp-book-fixed";
                } else {
                    return "PlanningTool/planning-exp-book-custom";
                }
            }

            //If Step has no assigned users, assign current user
            if (step.getUser() == null) {
                step.setUser(currentUser);
            }
            step.setStepType(experiment.getExperimentType().getStepTypes().get(experiment.getSteps().indexOf(step)));
            tmpListSteps.add(step);
            index++;

        }

        //check if steps inside this experiment fulfills continuity
        if (isProblemWithContinuity(experiment.getSteps())) {
            errorMessage = ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.error.continuity");
            prepareModelAtributesToRebookExperiment(model, experiment, errorMessage, userSteps, otherSteps, accessRights);

            if (experiment.getExperimentType().getIsFixedType()) {
                return "PlanningTool/planning-exp-book-fixed";
            } else {
                return "PlanningTool/planning-exp-book-custom";
            }
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
                    if (!productMapStock.containsKey(prod)) {
                        productMapStock.put(prod, prod.getStockLevel());
                    }
                    if (!productMapReserved.containsKey(prod)) {
                        productMapReserved.put(prod, prod.getReservedStockLevel());
                    }
                    double stocklevel = productMapStock.get(prod);
                    double reservedStockLevel = productMapReserved.get(prod);
                    stocklevel -= comp.getAmount() * piece.getMixtureAmount() / 100;
                    productMapStock.put(prod, stocklevel);
                    reservedStockLevel += comp.getAmount() * piece.getMixtureAmount() / 100;
                    productMapReserved.put(prod, reservedStockLevel);
                }
            }

        //check if a temp stock level in map is <0, if so, there is unsuficient stock.
        Iterator it = productMapStock.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if ((Double) pair.getValue() < 0) {
                enoughStock = false;
            }
        }

        //if not enough stock, change stock levels back to previous edit
        if (!enoughStock) {
            errorMessage = ResourceBundle.getBundle("messages", current).getString("enough.stock");
            prepareModelAtributesToRebookExperiment(model, experiment, errorMessage, userSteps, otherSteps, accessRights);

            if (experiment.getExperimentType().getIsFixedType()) {
                return "PlanningTool/planning-exp-book-fixed";
            } else {
                return "PlanningTool/planning-exp-book-custom";
            }
        }

        // if experiment fulfills all needs, save it into database

        //withdraw amounts from temp stock
        Iterator it2 = productMapStock.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry pair = (Map.Entry) it2.next();
            OwnProduct prod = (OwnProduct) pair.getKey();
            prod.setStockLevel((Double) pair.getValue());
            productService.save(prod);
        }
        //add amounts to reserved stock
        Iterator it3 = productMapReserved.entrySet().iterator();
        while (it3.hasNext()) {
            Map.Entry pair = (Map.Entry) it3.next();
            OwnProduct prod = (OwnProduct) pair.getKey();
            prod.setReservedStockLevel((Double) pair.getValue());
            productService.save(prod);
        }

        List<OwnProduct> products = productService.findAll();
        for (OwnProduct tempProd : products) {
            if (tempProd.getStockLevel() < tempProd.getLowStockLevel()) {
                emailController.sendLowStockEmail(tempProd, experiment.getUser(), "experiment");
            }
        }


        //If it's custom experiment, save stepTypes and experimentType
        if (!experiment.getExperimentType().getIsFixedType()) {
            //remove connection between step and stepType
            for (Step step : experiment.getSteps()) {
                step.setStepType(null);
            }
            //prepare list of steptypes to save it in database
            List<StepType> tmpStepTypeList = experiment.getExperimentType().getStepTypes();
            //remove connection between experimentType and stepType
            experiment.getExperimentType().setStepTypes(null);

            for (StepType stepType : tmpStepTypeList) {
                //if stepType contains default continuity, use special continuity entity for it
                if (stepType.getContinuity().getType().equals("No") && stepType.getContinuity().getHours() == 0 && stepType.getContinuity().getMinutes() == 0 && stepType.getContinuity().getDirectionType().equals("After")) {
                    stepTypeService.saveStepTypeInCustomExperiment(stepType);
                } else {
                    stepTypeService.saveNewStepType(stepType);
                }
            }
            experiment.getExperimentType().setStepTypes(tmpStepTypeList);
        }


        //Save steps into database
        for (Step step : tmpListSteps) {
            stepService.saveSomeAttributes(step);
        }
        experiment.setSteps(tmpListSteps);


        //If it's custom experiment, save also experimentType
        if (!experiment.getExperimentType().getIsFixedType()) {
            experimentTypeService.saveExperimentType(experiment.getExperimentType());
        }

        //Save piecesOfExperiment in database
        if (experiment.getPiecesOfMixture() != null) {
            for (PieceOfMixture pom : experiment.getPiecesOfMixture()) {
                pieceOfMixtureService.save(pom);
            }
        }

        //set Start date and end date of experiment
        experiment.setStartDate(tmpListSteps.get(0).getStart());
        experiment.setEndDate(tmpListSteps.get(tmpListSteps.size() - 1).getEnd());

        //add edited experiment to logger
        boolean newExperiment = experiment.isNew();

        //save experiment into database
        experimentService.saveExperiment(experiment);

        if (newExperiment) {
            addedExperiments.put(experiment, currentUser);
        } else {

            if (editedExperiments.containsKey(experiment)) {
                editedExperiments.replace(experiment, currentUser);
            } else {
                editedExperiments.put(experiment, currentUser);
            }
        }

        ra.addFlashAttribute("Status", "Success");
        String message = new String(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.added"));
        ra.addFlashAttribute("Message", message);
        return "redirect:/planning/";
    }


    //Method to prepare model atributes when user entered wrong input
    private void prepareModelAtributesToRebookExperiment(final ModelMap model, Experiment experiment, String errorMessage, List<Step> userSteps, List<Step> otherSteps, ArrayList<String> accessRights) {

        List<String> typeOptions = new ArrayList<>();
        typeOptions.add("No");
        typeOptions.add("Soft (at least)");
        typeOptions.add("Soft (at most)");
        typeOptions.add("Hard");

        List<String> allFixedTimeTypeOptions = new ArrayList<>();
        allFixedTimeTypeOptions.add("Equal");
        allFixedTimeTypeOptions.add("At least");
        allFixedTimeTypeOptions.add("At most");

        List<String> directionOptions = new ArrayList<>();
        directionOptions.add("After");
        directionOptions.add("Before");

        model.addAttribute("allTypeOptions", typeOptions);
        model.addAttribute("allFixedTimeTypeOptions", allFixedTimeTypeOptions);
        model.addAttribute("allTypeDirections", directionOptions);


        HolidayManager manager = HolidayManager.getInstance(HolidayCalendar.BELGIUM);
        Set<Holiday> holidays = manager.getHolidays(Calendar.getInstance().get(Calendar.YEAR));
        model.addAttribute("holidays", holidays);
        model.addAttribute("errorMsg", errorMessage);
        model.addAttribute("experiment", experiment);
        model.addAttribute("allDevices", deviceService.findAll());
        model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
        model.addAttribute("allExperiments", experimentService.findAll());
        model.addAttribute("allStepTypes", stepTypeService.findAll());
        model.addAttribute("allMixtures", mixtureService.findAll());
        model.addAttribute("userSteps", userSteps);
        model.addAttribute("otherSteps", otherSteps);
        model.addAttribute("allExperimentTypes", allFixedExperimentTypes());
        model.addAttribute("userAccessRights", accessRights);
    }

    @PreAuthorize("hasAuthority('Planning - Book step/experiment') or hasAuthority('Planning - Adjust step/experiment own') or hasAuthority('Planning - Adjust step/experiment own/promotor') or hasAuthority('Planning - Adjust step/experiment all') ")
    @RequestMapping(value = "/planning/experiments/book/{id}", method = RequestMethod.GET)
    public String viewEditExperiment(@PathVariable long id, final ModelMap model, RedirectAttributes ra) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Experiment experiment = experimentService.findById(id).orElse(null);
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


                //Split steps on userSteps and otherSteps
                //user steps - steps of user that has booked or want's to book this experiment
                List<Step> userSteps = new ArrayList<Step>();
                List<Step> otherSteps = new ArrayList<Step>();
                for (Step step : stepService.findAll()) {
                    //if user of step is same as owner of experiment - put him in userSteps list
                    if (step.getUser().getId() == experimentService.findById(id).orElse(null).getUser().getId()) {
                        userSteps.add(step);
                    } else {
                        otherSteps.add(step);
                    }
                }

                ArrayList<String> accessRights = new ArrayList<String>();

                for (Role role : user.getRoles()) {
                    accessRights.add(role.getName());
                }

                prepareModelAtributesToRebookExperiment(model, experiment, "", userSteps, otherSteps, accessRights);

                if (experiment.getExperimentType().getIsFixedType()) {
                    return "PlanningTool/planning-exp-book-fixed";
                } else {
                    return "PlanningTool/planning-exp-book-custom";
                }
            } else {
                ra.addFlashAttribute("Status", new String("Error"));
                ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.edit.step.norights")));
                return "redirect:/planning/";
            }
        } else {
            ra.addFlashAttribute("Status", new String("Error"));
            ra.addFlashAttribute("Message", new String("Not found"));
            return "redirect:/planning/experiments";
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

        List<String> typeOptions = new ArrayList<>();
        typeOptions.add(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("options.type.no"));
        typeOptions.add(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("options.type.soft.atleast"));
        typeOptions.add(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("options.type.soft.atmost"));
        typeOptions.add(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("options.type.hard"));

        List<String> fixedTimeTypeOptions = new ArrayList<>();
        fixedTimeTypeOptions.add(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("options.type.equal"));
        fixedTimeTypeOptions.add(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("options.type.atleast"));
        fixedTimeTypeOptions.add(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("options.type.atmost"));

        List<String> directionOptions = new ArrayList<>();
        directionOptions.add(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("options.type.after"));
        directionOptions.add(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("options.type.before"));

        model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
        model.addAttribute("allStepTypes", stepTypeService.findAll());
        model.addAttribute("experimentType", new ExperimentType());
        model.addAttribute("allTypeOptions", typeOptions);
        model.addAttribute("allFixedTimeTypeOptions", fixedTimeTypeOptions);
        model.addAttribute("allTypeDirections", directionOptions);
        return "PlanningTool/planning-exp-manage";
    }

    @PreAuthorize("hasAuthority('Planning - Make new experiment')")
    @RequestMapping(value = "/planning/experiments/{id}", method = RequestMethod.GET)
    public String viewEditExperimentType(@PathVariable Long id, final ModelMap model) {
        //If experiment with this id is not present, return back to experiment page
        if (!experimentTypeService.findById(id).isPresent()) {
            return "redirect:/planning/experiments";
        }

        List<String> typeOptions = new ArrayList<>();
        typeOptions.add(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("options.type.no"));
        typeOptions.add(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("options.type.soft.atleast"));
        typeOptions.add(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("options.type.soft.atmost"));
        typeOptions.add(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("options.type.hard"));

        List<String> fixedTimeTypeOptions = new ArrayList<>();
        fixedTimeTypeOptions.add(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("options.type.equal"));
        fixedTimeTypeOptions.add(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("options.type.atleast"));
        fixedTimeTypeOptions.add(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("options.type.atmost"));

        List<String> directionOptions = new ArrayList<>();
        directionOptions.add(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("options.type.after"));
        directionOptions.add(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("options.type.before"));

        model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
        model.addAttribute("allStepTypes", stepTypeService.findAll());
        model.addAttribute("experimentType", experimentTypeService.findById(id).orElse(null));
        model.addAttribute("allTypeOptions", typeOptions);
        model.addAttribute("allFixedTimeTypeOptions", fixedTimeTypeOptions);
        model.addAttribute("allTypeDirections", directionOptions);

        return "PlanningTool/planning-exp-manage";
    }

    @PreAuthorize("hasAuthority('Planning - Make new experiment')")
    @RequestMapping(value = {"/planning/experiments/", "/planning/experiments/{id}"}, method = RequestMethod.POST)
    public String addNewExperimentType(@Valid ExperimentType experimentType, BindingResult result, ModelMap
            model, RedirectAttributes ra) {

        if (result.hasErrors()) {
            ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error")));
            return "redirect:/planning/experiments/put";
        }

        if (experimentType.getStepTypes() == null) {
            ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.nosteps")));
            return "redirect:/planning/experiments/put";
        }

        //check if experimentType name is unique
        for (ExperimentType expType : experimentTypeService.findAll()) {
            //If there is experimentType with different ID and same name, it's not unique
            if (experimentType.getExpname().equals(expType.getExpname()) && !Objects.equals(experimentType.getId(), expType.getId())) {
                ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.alreadyexists")));
                return "redirect:/planning/experiments/put";

            }
        }

        //remove deleted rows (null)
        //if name is null, than this row was removed
        for (int i = 0; i < experimentType.getStepTypes().size(); i++) {
            if (experimentType.getStepTypes().get(i).getStepTypeName() == null) {
                experimentType.getStepTypes().remove(i);
                i--; //Size of array was changed, so same index needs to be checked again
            }
        }


        ExperimentType tempExperimentType = experimentType.getId() == null ? null : experimentTypeService.findById(experimentType.getId()).orElse(null);
        if (tempExperimentType == null) {
            for (ExperimentType exptyp : experimentTypeService.findAll()) {
                if (experimentType.getExpname().equals(exptyp.getExpname())) {
                    ra.addFlashAttribute("Status", new String("Error"));
                    ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.new.alreadyexists")));
                    return "redirect:/planning/experiments/put";
                }
            }
            for (StepType stepType : experimentType.getStepTypes()) {
                if (stepType.getContinuity().getHours() < 0) {
                    ra.addFlashAttribute("Status", new String("Error"));
                    ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.hours")));
                    return "redirect:/planning/experiments/put";
                }
                if (stepType.getContinuity().getMinutes() > 59 || stepType.getContinuity().getMinutes() < 0) {
                    ra.addFlashAttribute("Status", new String("Error"));
                    ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.minutes")));
                    return "redirect:/planning/experiments/put";
                } else
                    stepTypeService.saveNewStepType(stepType);
            }
            ra.addFlashAttribute("Status", new String("Success"));
            ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.success")));
        } else {


            for (StepType stepType : experimentType.getStepTypes()) {
                if (stepType.getContinuity().getHours() < 0) {
                    ra.addFlashAttribute("Status", new String("Error"));
                    ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.hours")));
                    return "redirect:/planning/experiments/{id}";
                }
                if (stepType.getContinuity().getMinutes() > 59 || stepType.getContinuity().getMinutes() < 0) {
                    ra.addFlashAttribute("Status", new String("Error"));
                    ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.minutes")));
                    return "redirect:/planning/experiments/{id}";
                } else
                    stepTypeService.saveNewStepType(stepType);
            }
            ra.addFlashAttribute("Status", new String("Success"));
            ra.addFlashAttribute("Message", new String(ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.edited")));
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

    public boolean isPorblemWithFixedLength(StepType stepType, Step step) throws ParseException {
        //If stepType doesn't have fixed length -> there is no problem with fixed length
        if (!stepType.getHasFixedLength()) {
            return false;
        }
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        DateTime startDate = formatter.parseDateTime(step.getStart() + " " + step.getStartHour());
        DateTime endDate = formatter.parseDateTime(step.getEnd() + " " + step.getEndHour());
        //Check if selected dates fulfills fixedTime needs
        switch (stepType.getFixedTimeType()) {
            case "Equal":
                if (((endDate.getMillis() - startDate.getMillis()) == 1000 * 60 * 60 * stepType.getFixedTimeHours() + 1000 * 60 * stepType.getFixedTimeMinutes())) {
                    return false;
                }
                break;
            case "At least":
                if (((endDate.getMillis() - startDate.getMillis()) >= 1000 * 60 * 60 * stepType.getFixedTimeHours() + 1000 * 60 * stepType.getFixedTimeMinutes())) {
                    return false;
                }
                break;
            case "At most":
                if (((endDate.getMillis() - startDate.getMillis()) <= 1000 * 60 * 60 * stepType.getFixedTimeHours() + 1000 * 60 * stepType.getFixedTimeMinutes())) {
                    return false;
                }
                break;
            default:
                return true;
        }
        return true;
    }

    // return error message that describes Problem with date of selected step
    public String getMessageForSelectedStep(Step step, User currentUser) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        DateTime currentStartDate = formatter.parseDateTime(step.getStart() + " " + step.getStartHour());
        DateTime currentEndDate = formatter.parseDateTime(step.getEnd() + " " + step.getEndHour());

        //check if startDate is before endDate
        if (currentEndDate.isBefore(currentStartDate)) {
            return ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.save.date") + currentStartDate.toString("yyyy-MM-dd HH:mm") + ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.after.enddate");
        }
        //check if startDate is not the same as endDate
        if (currentEndDate.equals(currentStartDate)) {
            return ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.save.date") + currentStartDate.toString("yyyy-MM-dd HH:mm") + ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.same.enddate");
        }

        Role adminole = roleService.findByName("Administrator").get();
        Role promotorRole = roleService.findByName("Researcher").get();
        Role masterstudentRole = roleService.findByName("Masterstudent").get();

        //if it isn't admin, researcher or master student, it needs to be inside openning hours
        if (!currentUser.getRoles().contains(adminole) && !currentUser.getRoles().contains(promotorRole) && !currentUser.getRoles().contains(masterstudentRole)) {
            //check opening hours
            if (!isInsideOpeningHours(currentStartDate)) {
                return ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.save.date") + currentStartDate.toString("yyyy-MM-dd HH:mm") + ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.save.date2");
            }
            if (!isInsideOpeningHours(currentEndDate)) {
                return ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.save.date") + currentEndDate.toString("yyyy-MM-dd HH:mm") + ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.save.date2");
            }
        }

        //check booking in past
        if (dateIsInPast(currentEndDate)) {
            return "Error while trying to save step as a part of experiment. Date " + currentEndDate.toString("yyyy-MM-dd HH:mm") + " is in past.";
        }


        //check weekend
        if (isWeekend(currentStartDate)) {
            return ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.save.date") + currentStartDate.toString("yyyy-MM-dd HH:mm") + ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.save.date3");
        }
        if (isWeekend(currentEndDate)) {
            return ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.save.date") + currentEndDate.toString("yyyy-MM-dd HH:mm") + ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.save.date3");
        }
        //check holidays
        if (isInsideHoliday(currentStartDate)) {
            return ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.save.date") + currentStartDate.toString("yyyy-MM-dd HH:mm") + ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.save.date4");

        }
        if (isInsideHoliday(currentEndDate)) {
            return ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.save.date") + currentEndDate.toString("yyyy-MM-dd HH:mm") + ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.save.date4");

        }

        //CheckOverNight
        if (currentStartDate.getDayOfYear() < currentEndDate.getDayOfYear()) {
            if (!step.getDevice().getDeviceType().getOvernightuse()) {
                return ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.save.device") + step.getDevice().getDevicename() + ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("experiment.type.error.save.device2");

            }
        }
        return "Success";
    }

    // returns true, when there is a problem with continuity
    public boolean dateTimeIsUnavailable(Step step, User currentUser) throws ParseException {
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

        Role adminole = roleService.findByName("Administrator").orElse(null);
        Role promotorRole = roleService.findByName("Researcher").orElse(null);
        Role masterstudentRole = roleService.findByName("Masterstudent").orElse(null);

        //if it isn't admin, researcher or master student, it needs to be inside openning hours
        if (!currentUser.getRoles().contains(adminole) && !currentUser.getRoles().contains(promotorRole) && !currentUser.getRoles().contains(masterstudentRole)) {

            //check opening hours
            if (!isInsideOpeningHours(currentStartDate)) {
                return true;
            }
            if (!isInsideOpeningHours(currentEndDate)) {
                return true;
            }
        }
        //check weekend
        if (isWeekend(currentStartDate)) {
            return true;
        }
        if (isWeekend(currentEndDate)) {
            return true;
        }

        //check endDate in past
        if (dateIsInPast(currentEndDate)) {
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

            String No = ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("options.type.no");

            switch (steps.get(i).getStepType().getContinuity().getType()) {
                case "Zacht (ten minste)":
                case "Soft (at least)" :
                    if ((steps.get(i).getStepType().getContinuity().getDirectionType().equals("After"))||steps.get(i).getStepType().getContinuity().getDirectionType().equals("Na")) {
                        //next step should be after end of previous
                        //nextStartDate - currentEndTime >= currentStartTime
                        if (!((nextStartDate.getMillis() - currentEndDate.getMillis()) >= 1000 * 60 * 60 * steps.get(i).getStepType().getContinuity().getHours() + 1000 * 60 * steps.get(i).getStepType().getContinuity().getMinutes())) {
                            return true;
                        }
                        //if Continuity has time direction "after" then
                        // current start date can't be after next start date.
                        if (currentStartDate.isAfter(nextStartDate) || currentEndDate.isAfter(nextEndDate)) {
                            return true;
                        }
                    } else if ((steps.get(i).getStepType().getContinuity().getDirectionType().equals("Before")||steps.get(i).getStepType().getContinuity().getDirectionType().equals("Voor"))) {
                        //next step should be before end of previous
                        //currentEndTime - nextStartDate >= currentStartTime
                        if (!((currentEndDate.getMillis() - nextStartDate.getMillis()) >= 1000 * 60 * 60 * steps.get(i).getStepType().getContinuity().getHours() + 1000 * 60 * steps.get(i).getStepType().getContinuity().getMinutes())) {
                            return true;
                        }
                    } else {
                        //If direction (before or after) is not specified - problem
                        return true;
                    }
                    break;
                case "Zacht (ten meeste)":
                case "Soft (at most)":
                    if ((steps.get(i).getStepType().getContinuity().getDirectionType().equals("After"))||steps.get(i).getStepType().getContinuity().getDirectionType().equals("Na")) {
                        //next step should be after end of previous
                        //nextStartDate - currentEndTime <= currentStartTime
                        if (!((nextStartDate.getMillis() - currentEndDate.getMillis()) <= 1000 * 60 * 60 * steps.get(i).getStepType().getContinuity().getHours() + 1000 * 60 * steps.get(i).getStepType().getContinuity().getMinutes())) {
                            return true;
                        }
                        //if Continuity has time direction "after" then
                        // current start date can't be after next start date.
                        if (currentStartDate.isAfter(nextStartDate) || currentEndDate.isAfter(nextEndDate)) {
                            return true;
                        }
                    } else if ((steps.get(i).getStepType().getContinuity().getDirectionType().equals("Before")||steps.get(i).getStepType().getContinuity().getDirectionType().equals("Voor"))) {
                        //next step should be before end of previous
                        //currentEndTime - nextStartDate <= currentStartTime
                        if (!((currentEndDate.getMillis() - nextStartDate.getMillis()) <= 1000 * 60 * 60 * steps.get(i).getStepType().getContinuity().getHours() + 1000 * 60 * steps.get(i).getStepType().getContinuity().getMinutes())) {
                            return true;
                        }
                    } else {
                        //If direction (before or after) is not specified - problem
                        return true;
                    }
                    break;
                case "Hard":
                    if ((steps.get(i).getStepType().getContinuity().getDirectionType().equals("After"))||steps.get(i).getStepType().getContinuity().getDirectionType().equals("Na")) {
                        //next step should be after end of previous
                        //nextStartDate - currentEndTime == currentStartTime
                        if (!((nextStartDate.getMillis() - currentEndDate.getMillis()) == 1000 * 60 * 60 * steps.get(i).getStepType().getContinuity().getHours() + 1000 * 60 * steps.get(i).getStepType().getContinuity().getMinutes())) {
                            return true;
                        }
                        //if Continuity has time direction "after" then
                        // current start date can't be after next start date.
                        if (currentStartDate.isAfter(nextStartDate) || currentEndDate.isAfter(nextEndDate)) {
                            return true;
                        }
                    } else if ((steps.get(i).getStepType().getContinuity().getDirectionType().equals("Before")||steps.get(i).getStepType().getContinuity().getDirectionType().equals("Voor"))) {
                        //next step should be before end of previous
                        //currentEndTime - nextStartDate == currentStartTime
                        if (!((currentEndDate.getMillis() - nextStartDate.getMillis()) == 1000 * 60 * 60 * steps.get(i).getStepType().getContinuity().getHours() + 1000 * 60 * steps.get(i).getStepType().getContinuity().getMinutes())) {
                            return true;
                        }
                    } else {
                        //If direction (before or after) is not specified - problem
                        return true;
                    }
                    break;
                case "Geen":
                case "No":
                    //If there is no continuity,
                    //current start date can't be after next start, but can start at same time
                    if (currentStartDate.isAfter(nextStartDate) || currentEndDate.isAfter(nextEndDate)) {
                        return true;
                    }
                    break;
                default:
                    return true;
            }
        }
        return false;
    }

    //Check, if dateTime is inside officeHours
    public boolean isInsideOpeningHours(DateTime dateTime) {
        return ((dateTime.getMinuteOfDay() >= SystemSettings.getCurrentSystemSettings().getCurrentOfficeHours().getStartHour() * 60 + SystemSettings.getCurrentSystemSettings().getCurrentOfficeHours().getStartMinute()) &&
                ((dateTime.getMinuteOfDay() <= SystemSettings.getCurrentSystemSettings().getCurrentOfficeHours().getEndHour() * 60 + SystemSettings.getCurrentSystemSettings().getCurrentOfficeHours().getEndMinute())));
    }

    //Check, if dateTime is in past
    public boolean dateIsInPast(DateTime dateTime) {
        return (dateTime.getMillis() < DateTime.now().getMillis());
    }


    //Check,if it's weekend
    public boolean isWeekend(DateTime dateTime) {
        return (dateTime.getDayOfWeek() > 5);
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
        if (experimentService != null) {
            List<Experiment> allExperiments = experimentService.findAll();
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
        if (experimentService != null) {
            List<Experiment> allExperiments = experimentService.findAll();
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

    private List<ExperimentType> allFixedExperimentTypes() {
        //Only fixed Experiment types should be show
        List<ExperimentType> fixedExpTypes = new ArrayList<>();
        for (ExperimentType expType : experimentTypeService.findAll()) {
            if (expType.getIsFixedType()) {
                fixedExpTypes.add(expType);
            }
        }
        return fixedExpTypes;
    }

}
