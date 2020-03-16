package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import be.uantwerpen.labplanner.common.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/users",method = RequestMethod.GET)
    public String showUsers(final ModelMap model){
        model.addAttribute("allUsers",userService.findAll());
        return "/Users/user-list";
    }

    @RequestMapping(value = "/users/put",method = RequestMethod.GET)
    public String viewCreateUser(final ModelMap model){
        model.addAttribute("allRoles",roleService.findAll());
        model.addAttribute(new User("","","","","","","","",null,null,null));
        return "/Users/user-manage";
    }

    @RequestMapping(value = "/users/{id}",method = RequestMethod.GET)
    public String viewEditUser(@PathVariable long id, final ModelMap model){
        model.addAttribute("allRoles",roleService.findAll());
        model.addAttribute("user",userService.findById(id).orElse(null));
        return "/Users/user-manage";
    }

    @RequestMapping(value = {"/users/","/users/{id}"},method = RequestMethod.POST)
    public String addUser(@Valid User user, BindingResult result, final ModelMap model){
        if (result.hasErrors()){
            model.addAttribute("allRoles",roleService.findAll());
            return "/Users/user-manage";
        }
        userService.save(user);
        return "redirect:/users";
    }

    @RequestMapping(value = "/users/{id}/delete",method = RequestMethod.GET)
    public String deleteUser(@PathVariable long id, final ModelMap model){
        userService.deleteById(id);
        model.clear();
        return "redirect:/users";
    }




}
