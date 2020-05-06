package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import be.uantwerpen.labplanner.common.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class RegisterController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;


    private List<User> registredUsers = new ArrayList<>();




    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String viewRegisterUser(@org.jetbrains.annotations.NotNull final ModelMap model){
        model.addAttribute("allRoles",roleService.findAll());
        model.addAttribute(new User("","DEFAULT","","","","","","",null,null,null));
        return "register-manage";
    }


    @RequestMapping(value = {"/register"}, method = RequestMethod.POST)
    public String addUser(@Valid User user,  BindingResult result, final ModelMap model, RedirectAttributes ra) {



        //check for standard errors & empty fields


        if ((result.hasErrors()) || (user.getPassword() == null) || (user.getFirstName() == null) || (user.getLastName() == null) || (user.getEmail() == null) || (user.getUaNumber() == null) || (user.getFirstName().trim().equals("")) || (user.getLastName().trim().equals("")) || (user.getPassword().trim().equals("")) || (user.getUaNumber().trim().equals("")) || (user.getEmail().trim().equals(""))) {
            model.addAttribute("allRoles", roleService.findAll());
            model.addAttribute("allUsers", userService.findAll());
            model.addAttribute("UserInUse", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("user.addError"));

            return "register-manage";
        }

        //trim everything


        //test for duplicate UA number or email adress.
        for (User temp : userService.findAll()) {

            if ((temp.getUaNumber() != null) && ((temp.getUaNumber().equals(user.getUaNumber())))) {
                model.addAttribute("allRoles", roleService.findAll());
                model.addAttribute("allUsers", userService.findAll());
                model.addAttribute("UserInUse", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("user.UAError"));

                return "register-manage";
            }

            if ((temp.getEmail() != null) && (temp.getEmail().toLowerCase().equals(user.getEmail().toLowerCase()))) {
                model.addAttribute("allRoles", roleService.findAll());
                model.addAttribute("allUsers", userService.findAll());
                model.addAttribute("UserInUse", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("user.MailError"));

                return "register-manage";
            }

        }


        //set username to UANUMBER
        user.setUsername(user.getUaNumber());
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("UserInUse", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("user.UAError"));
            model.addAttribute("allRoles", roleService.findAll());
            return "register-manage";

        }

        //validate Password
        if (!validatePassWord(user.getPassword())){
            model.addAttribute("UserInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("user.passwordError"));
            return "register-manage";
        }

        //validate Email
        if (!isValidEmailAddress(user.getEmail())){
            model.addAttribute("UserInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("user.mailError"));
            return "register-manage";
        }

        //if it passes all tests
        //may not save it to userService, but in seperate folder where admin can validate.
        userService.save(user);
        return "redirect:/login?registered";
    }



    boolean validatePassWord(String pw) {
        //check password
        if (pw.length() < 6) {
            return false;

        } else if (pw.equals(pw.toLowerCase()) || pw.equals(pw.toUpperCase())) {

            return false;
        } else if (!pw.equals(pw.trim())) {
            return false;
        }
        return true;
    }

    private boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

}
