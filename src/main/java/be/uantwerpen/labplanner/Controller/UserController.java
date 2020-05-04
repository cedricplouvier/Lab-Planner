package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.GlobalVariables;
import be.uantwerpen.labplanner.Model.OfficeHours;
import be.uantwerpen.labplanner.Model.Relation;
import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Service.StepService;
import be.uantwerpen.labplanner.Service.RelationService;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import be.uantwerpen.labplanner.common.service.users.UserService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
public class UserController {

    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StepService stepService;

    @Autowired
    private RelationService relationService;


    //Populate
    @ModelAttribute("allUsers")
    public Iterable<User> populateUsers() {
        return this.userService.findAll();
    }

    @ModelAttribute("allRoles")
    public Iterable<Role> populateRoles() {
        return this.roleService.findAll();
    }

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/users", method = RequestMethod.GET)
    public String showUsers(final ModelMap model) {
        model.addAttribute("allUsers", userService.findAll());
        return "Users/user-list";
    }

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/users/put", method = RequestMethod.GET)
    public String viewCreateUser(@org.jetbrains.annotations.NotNull final ModelMap model) {
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("allUsers", userService.findAll());
        model.addAttribute(new User("", "", "", "", "", "", "", "", null, null, null));
        return "Users/user-manage";
    }


    @RequestMapping(value = "/officeHours", method = RequestMethod.GET)
    public String viewEditOfficeHours(@org.jetbrains.annotations.NotNull final ModelMap model) {
        //get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute(user);
        model.addAttribute("officeHours", GlobalVariables.currentOfficeHours);
        return "Users/officeHours-manage";
    }

    @RequestMapping(value = "/officeHours", method = RequestMethod.POST)
    public String saveOfficeHours(@Valid OfficeHours officeHours, BindingResult result, @org.jetbrains.annotations.NotNull final ModelMap model) {
        //get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();


        //Only admin can change
        Role adminole = roleService.findByName("Administrator").get();
        if (currentUser.getRoles().contains(adminole)) {
            //Set global openning hours if numbers are correct
            if ((officeHours.getStartMinute() >= 0 && officeHours.getStartMinute() <= 59) ||
                    (officeHours.getStartHour() >= 0 && officeHours.getStartHour() <= 23) ||
                    (officeHours.getEndMinute() >= 0 && officeHours.getEndMinute() <= 59) ||
                    (officeHours.getEndHour() >= 0 && officeHours.getEndHour() <= 23)) {
                GlobalVariables.currentOfficeHours.setStartHour(officeHours.getStartHour());
                GlobalVariables.currentOfficeHours.setStartMinute(officeHours.getStartMinute());
                GlobalVariables.currentOfficeHours.setEndHour(officeHours.getEndHour());
                GlobalVariables.currentOfficeHours.setEndMinute(officeHours.getEndMinute());
                GlobalVariables.currentOfficeHours.setOfficeHoursOn(officeHours.isOfficeHoursOn());
                GlobalVariables.currentOfficeHours.setWeekendOn(officeHours.isWeekendOn());
                GlobalVariables.currentOfficeHours.setHolidaysOn(officeHours.isHolidaysOn());
            }
        }

        return "redirect:/home";
    }


    @RequestMapping(value = "/password", method = RequestMethod.GET)
    public String viewEditPassword(@org.jetbrains.annotations.NotNull final ModelMap model) {
        //get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        model.addAttribute(user);

        return "Users/password-manage";
    }

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public String savePassword(@Valid User user, BindingResult result, @org.jetbrains.annotations.NotNull final ModelMap model) {
        //get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User curruser = (User) authentication.getPrincipal();

        if (curruser.getId() != user.getId()) {
            //error
            return null;
        } else if (user.getPassword().length() < 6) {
            model.addAttribute("PWError", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("users.pwshort"));
            model.addAttribute(user);
            return "Users/password-manage";

        } else if (user.getPassword().equals(user.getPassword().toLowerCase()) || user.getPassword().equals(user.getPassword().toUpperCase())) {

            model.addAttribute(user);
            model.addAttribute("PWError", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("users.pwcapital"));
            return "Users/password-manage";
        } else if (!user.getPassword().matches(".*\\d.*")) {
            model.addAttribute("PWError", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("users.pwnumber"));
            model.addAttribute(user);
            return "Users/password-manage";
        } else if (!user.getPassword().equals(user.getPassword().trim())) {
            model.addAttribute("PWError", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("users.pwspace"));
            model.addAttribute(user);
            return "Users/password-manage";
        }

        curruser.setPassword(user.getPassword());
        userService.save(curruser);


        return "redirect:/home";
    }


    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/users/{id}", method = RequestMethod.GET)
    public String viewEditUser(@PathVariable("id") long id, final ModelMap model) {
        model.addAttribute("allUsers", userService.findAll());
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("user", userService.findById(id).orElse(null));
        return "Users/user-manage";
    }


    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = {"/usermanagement/users/", "/usermanagement/users/{id}"}, method = RequestMethod.POST)
    public String addUser(@Valid User user, BindingResult result, final ModelMap model) {
        if ((result.hasErrors()) || (user.getPassword() == null) || (user.getUsername() == null) || (user.getUsername().trim().equals("")) || (user.getPassword().trim().equals(""))) {
            model.addAttribute("allRoles", roleService.findAll());
            model.addAttribute("allUsers", userService.findAll());
            model.addAttribute("UserInUse", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("user.addError"));

            return "Users/user-manage";
        }


        //test for duplicate UA number
        for (User temp : userService.findAll()) {

            if ((temp.getUaNumber() != null) && (user.getUaNumber() != null) && ((temp.getUaNumber().equals(user.getUaNumber())) && (temp.getId() != user.getId()))) {
                model.addAttribute("allRoles", roleService.findAll());
                model.addAttribute("allUsers", userService.findAll());
                model.addAttribute("UserInUse", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("user.UAError"));

                return "Users/user-manage";
            }
        }


        if (user.getId() == null) {
            //if the given username is unique, save the user in the database
            if (userService.findByUsername(user.getUsername()).isPresent()) {
                model.addAttribute("allRoles", roleService.findAll());
                model.addAttribute("allUsers", userService.findAll());
                model.addAttribute("UserInUse", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("user.uniqueError"));
                return "Users/user-manage";
            }
            //trim input and save
            user.setUsername(user.getUsername().trim());
            if (!user.getPassword().equals(user.getPassword().trim())) {
                model.addAttribute("UserInUse", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("user.passwordError"));
                model.addAttribute("allRoles", roleService.findAll());
                model.addAttribute("allUsers", userService.findAll());
                return "Users/user-manage";
            }
            userService.save(user);
            return "redirect:/usermanagement/users";
        }

        // already id, so existing user
        //Check if name is not already used.
        User tempUser = userService.findById(user.getId()).orElse(null);
        if (!tempUser.getUsername().equals(user.getUsername())) {
            if (userService.findByUsername(user.getUsername()).isPresent()) {
                model.addAttribute("UserInUse", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("user.uniqueError"));
                model.addAttribute("allRoles", roleService.findAll());
                model.addAttribute("allUsers", userService.findAll());
                return "Users/user-manage";
            }

            //trim input and save
            user.setUsername(user.getUsername().trim());
            if (!user.getPassword().equals(user.getPassword().trim())) {
                model.addAttribute("UserInUse", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("user.passwordError"));
                model.addAttribute("allRoles", roleService.findAll());
                model.addAttribute("allUsers", userService.findAll());
                return "Users/user-manage";
            }
            userService.save(user);
            return "redirect:/usermanagement/users";
        }
        //trim input and save
        user.setUsername(user.getUsername().trim());
        if (!user.getPassword().equals(user.getPassword().trim())) {
            model.addAttribute("UserInUse", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("user.passwordError"));
            model.addAttribute("allRoles", roleService.findAll());
            model.addAttribute("allUsers", userService.findAll());
            return "Users/user-manage";
        }
        userService.save(user);
        return "redirect:/usermanagement/users";
    }


    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/users/{id}/delete", method = RequestMethod.GET)
    public String deleteUser(@PathVariable long id, final ModelMap model) {

        //get current user.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getId() == id) {
            model.addAttribute("allUsers", userService.findAll());
            model.addAttribute("inUseError", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("user.selfDeleteError"));
            return "Users/user-list";
        }


        List<Step> allSteps = stepService.findAll();
        boolean isUsed = false;
        for (Step step : allSteps) {
            if (step.getUser().getId() == id) {
                isUsed = true;
            }
        }

        for (Relation relation : relationService.findAll()) {
            if ((relation.getResearcher().getId() == id) || (relation.getStudents().contains(userService.findById(id).orElse(null)))) {
                isUsed = true;
            }
        }

        if (isUsed) {
            model.addAttribute("allUsers", userService.findAll());
            model.addAttribute("inUseError", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("user.deleteError"));
            return "Users/user-list";
        }

        userService.deleteById(id);
        model.clear();
        return "redirect:/usermanagement/users";
    }


}
