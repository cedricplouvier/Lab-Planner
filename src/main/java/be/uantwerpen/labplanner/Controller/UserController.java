package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import be.uantwerpen.labplanner.common.service.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.security.access.prepost.PreAuthorize;
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

    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    //Populate
    @ModelAttribute("allUsers")
    public Iterable<User> populateUsers() {return this.userService.findAll();}

    @ModelAttribute("allRoles")
    public Iterable<Role> populateRoles() {return this.roleService.findAll();}

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/users",method = RequestMethod.GET)
    public String showUsers(final ModelMap model){
        model.addAttribute("allUsers",userService.findAll());
        return "/Users/user-list";
    }

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/users/put",method = RequestMethod.GET)
    public String viewCreateUser(@org.jetbrains.annotations.NotNull final ModelMap model){
        model.addAttribute("allRoles",roleService.findAll());
        model.addAttribute(new User("","","","","","","","",null,null,null));
        return "/Users/user-manage";
    }

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/users/{id}",method = RequestMethod.GET)
    public String viewEditUser(@PathVariable long id, final ModelMap model){
        model.addAttribute("allRoles",roleService.findAll());
        model.addAttribute("user",userService.findById(id).orElse(null));
        return "/Users/user-manage";
    }


    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = {"/usermanagement/users/","/usermanagement/users/{id}"},method = RequestMethod.POST)
    public String addUser(@Valid User user, BindingResult result, final ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleService.findAll());
            return "/Users/user-manage";
        }

        if (user.getId() == null) {
            //if the given username is unique, save the user in the database
            if (userService.findByUsername(user.getUsername()).isPresent()) {
                model.addAttribute("allRoles", roleService.findAll());
                model.addAttribute("UserInUse", "Username " + user.getUsername() + " is already in use!");
                return "/Users/user-manage";
            }
            userService.save(user);
            return "redirect:/usermanagement/users";
        }

        //Check if name is not already used.
        User tempUser = userService.findById(user.getId()).orElse(null);
        if(!tempUser.getUsername().equals(user.getUsername())){
            if(userService.findByUsername(user.getUsername()).isPresent()){
                model.addAttribute("UserInUse", "Username " + user.getUsername() + " is already in use!");
                model.addAttribute("allRoles", roleService.findAll());
                return "/Users/user-manage";
            }
            userService.save(user);
            return "redirect:/usermanagement/users";
        }

        userService.save(user);
        return "redirect:/usermanagement/users";
    }

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/users/{id}/delete",method = RequestMethod.GET)
    public String deleteUser(@PathVariable long id, final ModelMap model){
        userService.deleteById(id);
        model.clear();
        return "redirect:/usermanagement/users";
    }




}
