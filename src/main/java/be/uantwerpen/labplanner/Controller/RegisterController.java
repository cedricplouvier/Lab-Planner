package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import be.uantwerpen.labplanner.common.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        return "register";
    }


    @RequestMapping(value = {"/register"}, method = RequestMethod.POST)
    public String addUser(@Valid User user,  BindingResult result, final ModelMap model, RedirectAttributes ra) {
        //check for standard errors
        if ((result.hasErrors())|| (user.getPassword() == null) || (user.getUsername() ==null) || (user.getUsername().trim().equals("")) || (user.getPassword().trim().equals(""))){
            model.addAttribute("allRoles", roleService.findAll());
            model.addAttribute("allUsers",userService.findAll());
            model.addAttribute("UserInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("user.addError") );

            return "register";
        }


        //test for duplicate UA number
        for(User temp : userService.findAll()){

            if ( (temp.getUaNumber()!= null) && (user.getUaNumber()!=null)   && ((temp.getUaNumber().equals(user.getUaNumber()))&&(temp.getId()!=user.getId()))){
                model.addAttribute("allRoles", roleService.findAll());
                model.addAttribute("allUsers",userService.findAll());
                model.addAttribute("UserInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("user.UAError") );

                return "register";
            }
        }

        //check password
        if(user.getPassword().length()<6){
            model.addAttribute("UserInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("users.pwshort") );
            model.addAttribute(user);
            return "register";

        }

        else if (user.getPassword().equals(user.getPassword().toLowerCase()) || user.getPassword().equals(user.getPassword().toUpperCase())){

            model.addAttribute(user);
            model.addAttribute("UserInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("users.pwcapital") );
            return "register";
        }

        else if (!user.getPassword().matches(".*\\d.*")){
            model.addAttribute("UserInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("users.pwnumber") );
            model.addAttribute(user);
            return "register";
        }

        else if (!user.getPassword().equals(user.getPassword().trim())){
            model.addAttribute("UserInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("users.pwspace") );
            model.addAttribute(user);
            return "register";
        }

        //if it passes all tests



            //trim input and save
            user.setUsername(user.getUsername().trim());
            if (!user.getPassword().equals(user.getPassword().trim())){
                model.addAttribute("UserInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("user.passwordError") );
                model.addAttribute("allRoles", roleService.findAll());
                model.addAttribute("allUsers",userService.findAll());
                return "register";
            }

            //may not save it to userService, but in seperate folder where admin can validate.
            userService.save(user);
            return "registerSuccess";
        }





}
