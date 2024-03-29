package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.OfficeHours;
import be.uantwerpen.labplanner.Model.Relation;
import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Model.SystemSettings;
import be.uantwerpen.labplanner.Service.OfficeHoursService;
import be.uantwerpen.labplanner.Service.StepService;
import be.uantwerpen.labplanner.Service.RelationService;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import be.uantwerpen.labplanner.common.service.users.UserService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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

    @Autowired
    private OfficeHoursService officeHoursService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //Populate
    @ModelAttribute("allUsers")
    public Iterable<User> populateUsers() {return this.userService.findAll();}

    @ModelAttribute("allRoles")
    public Iterable<Role> populateRoles() {return this.roleService.findAll();}


    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/users",method = RequestMethod.GET)
    public String showUsers(final ModelMap model){
        //get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        model.addAttribute("allUsers",userService.findAll());
        model.addAttribute("adminrole",roleService.findByName("Administrator").orElse(null));
        model.addAttribute("currentUserId",user.getId());
        return "Users/user-list";
    }



    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/users/put",method = RequestMethod.GET)
    public String viewCreateUser(@org.jetbrains.annotations.NotNull final ModelMap model){
        model.addAttribute("allRoles",roleService.findAll());
        model.addAttribute("allUsers",userService.findAll());
        model.addAttribute(new User("","DEFAULT","","","","","","",null,null,null));
        return "Users/user-manage";
    }


    @RequestMapping(value = "/officeHours", method = RequestMethod.GET)
    public String viewEditOfficeHours(@org.jetbrains.annotations.NotNull final ModelMap model) {
        //get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Role adminole = roleService.findByName("Administrator").get();
        model.addAttribute(user);
        //Only one entity of system settings should be in database
        model.addAttribute("officeHours", SystemSettings.getCurrentSystemSettings().getCurrentOfficeHours());
        model.addAttribute("allowedToChangeOfficeHours", user.getRoles().contains(adminole));
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
                officeHoursService.save(officeHours);
                SystemSettings.getCurrentSystemSettings().setCurrentOfficeHours(officeHours);
            }
        }

        return "redirect:/home";
    }

    @RequestMapping(value = "/password",method = RequestMethod.GET)
    public String viewEditPassword(@org.jetbrains.annotations.NotNull final ModelMap model){
        //get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        model.addAttribute(user);

        return "Users/password-manage";
    }

    @RequestMapping(value = "/password",method = RequestMethod.POST)
    public String savePassword(@Valid User user, BindingResult result, @org.jetbrains.annotations.NotNull final ModelMap model){
        //get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User curruser = (User) authentication.getPrincipal();


        if (!curruser.getId().equals(user.getId())){
            model.addAttribute("PWError", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("users.pwfalse") );
            model.addAttribute(user);
            return "Users/password-manage";
        }


        //false
        if (!validatePassword(user.getPassword())) {
            model.addAttribute("PWError", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("users.passwordCorrect"));
            model.addAttribute(user);
            return "Users/password-manage";
        }

        //if it passes all tests
        curruser.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(curruser);
        return "redirect:/home";
    }





    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/users/{id}",method = RequestMethod.GET)
    public String viewEditUser(@PathVariable("id") long id, final ModelMap model){
        //get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User curruser = (User) authentication.getPrincipal();

        Role admin = roleService.findByName("Administrator").orElse(null);
        User editUser = userService.findById(id).orElse(null);

        if (editUser==null){
            model.addAttribute("allUsers",userService.findAll());
            model.addAttribute("inUseError", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("user.editError"));
            model.addAttribute("adminrole",roleService.findByName("Administrator").orElse(null));
            model.addAttribute("currentUserId",curruser.getId());
            return "Users/user-list";
        }

        //curr user may not edit other admin
        if ((editUser.getRoles().contains(admin)) && (!editUser.equals(curruser))){

            model.addAttribute("allUsers",userService.findAll());
            model.addAttribute("inUseError", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("user.adminEditError"));
            model.addAttribute("adminrole",roleService.findByName("Administrator").orElse(null));
            model.addAttribute("currentUserId",curruser.getId());
            return "Users/user-list";
        }



        model.addAttribute("allUsers",userService.findAll());
        model.addAttribute("allRoles",roleService.findAll());
        model.addAttribute("user",userService.findById(id).orElse(null));


        return "Users/user-manage";
    }

    private boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }


    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = {"/usermanagement/users/","/usermanagement/users/{id}"}, method = RequestMethod.POST)
    public String addUser(@Valid User user,  BindingResult result, final ModelMap model) {
        if ((result.hasErrors())|| (user.getPassword() == null) || (user.getUsername() ==null) || (user.getUsername().trim().equals("")) || (user.getPassword().trim().equals(""))){
            model.addAttribute("allRoles", roleService.findAll());
            model.addAttribute("allUsers",userService.findAll());
            model.addAttribute("UserInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("user.addError") );

            return "Users/user-manage";
        }

        //check on invalid mail adres
        if ((!isValidEmailAddress(user.getEmail()))&&(!user.getEmail().trim().equals(""))){
            model.addAttribute("allRoles", roleService.findAll());
            model.addAttribute("allUsers",userService.findAll());
            model.addAttribute("UserInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("user.mailError") );

            return "Users/user-manage";
        }

        //test for duplicate UA number
        for(User temp : userService.findAll()){

            if ( (temp.getUaNumber()!= null) && (user.getUaNumber()!=null)   && ((temp.getUaNumber().equals(user.getUaNumber()))&&(!temp.getId().equals(user.getId())))){
                model.addAttribute("allRoles", roleService.findAll());
                model.addAttribute("allUsers",userService.findAll());
                model.addAttribute("UserInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("user.UAError") );

                return "Users/user-manage";
            }
        }


        //add new user
        if (user.getId() == null) {
            //if the given username is unique, save the user in the database
            if (userService.findByUsername(user.getUsername()).isPresent()) {
                model.addAttribute("allRoles", roleService.findAll());
                model.addAttribute("allUsers",userService.findAll());
                model.addAttribute("UserInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("user.uniqueError") );
                return "Users/user-manage";
            }
            //trim input and save
            user.setUsername(user.getUsername().trim());
            if (!user.getPassword().equals(user.getPassword().trim())){
                model.addAttribute("UserInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("user.passwordError") );
                model.addAttribute("allRoles", roleService.findAll());
                model.addAttribute("allUsers",userService.findAll());
                return "Users/user-manage";
            }

            //encode password
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.save(user);
            return "redirect:/usermanagement/users";
        }

        // already id, so existing user
        //Check if name is not already used.
        User tempUser = userService.findById(user.getId()).orElse(null);
        //check password, if password equals default_password, it needs to be changed back to the pw if the database.
        if (user.getPassword().equals("default_password")){
            //should not be hashed, because in database is already hashed.
            user.setPassword(tempUser.getPassword());
        }

        if(!tempUser.getUsername().equals(user.getUsername())){
            if(userService.findByUsername(user.getUsername()).isPresent()){
                model.addAttribute("UserInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("user.uniqueError") );
                model.addAttribute("allRoles", roleService.findAll());
                model.addAttribute("allUsers",userService.findAll());
                return "Users/user-manage";
            }

            //trim input and save
            user.setUsername(user.getUsername().trim());
            if (!user.getPassword().equals(user.getPassword().trim())){
                model.addAttribute("UserInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("user.passwordError") );
                model.addAttribute("allRoles", roleService.findAll());
                model.addAttribute("allUsers",userService.findAll());
                return "Users/user-manage";
            }
            userService.save(user);
            return "redirect:/usermanagement/users";
        }

        //trim input and save
        user.setUsername(user.getUsername().trim());
        if (!user.getPassword().equals(user.getPassword().trim())){
            model.addAttribute("UserInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("user.passwordError") );
            model.addAttribute("allRoles", roleService.findAll());
            model.addAttribute("allUsers",userService.findAll());
            return "Users/user-manage";
        }

        //existing id, so should not be rehashed.
        userService.save(user);
        return "redirect:/usermanagement/users";
    }

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/users/{id}/delete",method = RequestMethod.GET)
    public String deleteUser(@PathVariable long id, final ModelMap model) {

        //get current user.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();


        Role admin = roleService.findByName("Administrator").orElse(null);
        User userToDelete = userService.findById(id).orElse(null);

        if (userToDelete==null){
            model.addAttribute("allUsers",userService.findAll());
            model.addAttribute("inUseError", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("user.editError"));
            model.addAttribute("adminrole",roleService.findByName("Administrator").orElse(null));
            model.addAttribute("currentUserId",user.getId());
            return "Users/user-list";
        }


        if (userToDelete.getRoles().contains(admin)){
            model.addAttribute("allUsers",userService.findAll());
            model.addAttribute("inUseError", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("user.adminDeleteError"));
            model.addAttribute("adminrole",roleService.findByName("Administrator").orElse(null));
            model.addAttribute("currentUserId",user.getId());
            return "Users/user-list";
        }




        List<Step> allSteps = stepService.findAll();
        boolean isUsed = false;
        for (Step step : allSteps) {
            if (step.getUser().getId() == id) {
                isUsed = true;
            }
        }

        for (Relation relation : relationService.findAll()){
            if ((relation.getResearcher().getId() == id) || (relation.getStudents().contains(userService.findById(id).orElse(null)))){
                isUsed = true;
            }
        }

        if (isUsed){
            model.addAttribute("allUsers",userService.findAll());
            model.addAttribute("inUseError", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("user.deleteError"));
            model.addAttribute("adminrole",roleService.findByName("Administrator").orElse(null));
            model.addAttribute("currentUserId",user.getId());
            return "Users/user-list";
        }

        userService.deleteById(id);
        model.clear();
        return "redirect:/usermanagement/users";
    }

    private boolean validatePassword(String pw){
        if(pw.length()<6){
            return false;
        }
        //check for capital & lower letter and number
        else if (!pw.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$")){
            return false;
        }
        else if (!pw.equals(pw.trim())){
            return false;
        }
        return true;
    }

}