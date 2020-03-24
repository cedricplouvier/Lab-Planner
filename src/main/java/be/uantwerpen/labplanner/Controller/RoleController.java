package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.common.model.users.Privilege;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.service.users.PrivilegeService;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.beans.MethodDescriptor;

@Controller
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PrivilegeService privilegeService;

    //Populate
    @ModelAttribute("allRoles")
    public Iterable<Role> populateRoles(){return roleService.findAll();}

    @ModelAttribute("allPrivileges")
    public Iterable<Privilege> populatePrivileges(){return privilegeService.findAll();}

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/roles",method = RequestMethod.GET)
    public String showRoles(final ModelMap model){
        model.addAttribute("allRoles",roleService.findAll());
        return "/Roles/role-list";
    }

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/roles/put", method = RequestMethod.GET)
    public String ViewCreateRole(final ModelMap model){
        model.addAttribute("allPrivileges",privilegeService.findAll());
        model.addAttribute(new Role(""));
        return "/Roles/role-manage";
    }

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/roles/{id}",method = RequestMethod.GET)
    public String viewEditRole(@PathVariable long id, final ModelMap model){
        model.addAttribute("allPrivileges",privilegeService.findAll());
        model.addAttribute("role",roleService.findById(id));
        return "/Roles/role-manage";
    }

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = {"/usermanagement/roles/","/usermanagement/roles/{id}"},method = RequestMethod.POST)
    public String addRole(@Valid Role role, BindingResult result, final ModelMap model) {
        if (result.hasErrors() || role.getName().trim().equals("")) {
            model.addAttribute("allPrivileges", privilegeService.findAll());
            model.addAttribute("roleInUse", "Error when trying to save role, must have a valid name");
            return "/Roles/role-manage";
        }
        if (role.getId() == null) {
            if (roleService.findByName(role.getName()).isPresent()) {
                model.addAttribute("allPrivileges", privilegeService.findAll());
                model.addAttribute("roleInUse", "Role " + role.getName() + " is already in use!");
                return "/Roles/role-manage";
            }
            //trim input and save
            role.setName(role.getName().trim());
            roleService.save(role);
            return "redirect:/usermanagement/roles";
        }

        Role tempRole = roleService.findById(role.getId()).orElse(null);
        if (!tempRole.getName().equals(role.getName())){
            if (roleService.findByName(role.getName()).isPresent()){
                model.addAttribute("allPrivileges", privilegeService.findAll());
                model.addAttribute("roleInUse", "Role " + role.getName() + " is already in use!");
                return "/Roles/role-manage";
            }
            //trim input and save
            role.setName(role.getName().trim());
            roleService.save(role);
            return "redirect:/usermanagement/roles";
        }
        //trim input and save
        role.setName(role.getName().trim());
        roleService.save(role);
        return "redirect:/usermanagement/roles";
    }

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/roles/{id}/delete",method = RequestMethod.GET)
    public String deleteRole(@PathVariable long id, final ModelMap model){
        roleService.deleteById(id);
        model.clear();
        return "redirect:/usermanagement/roles";
    }
}
