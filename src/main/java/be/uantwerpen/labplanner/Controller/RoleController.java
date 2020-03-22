package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.service.users.PrivilegeService;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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

    @RequestMapping(value = "/usermanagement/roles",method = RequestMethod.GET)
    public String showRoles(final ModelMap model){
        model.addAttribute("allRoles",roleService.findAll());
        return "/Roles/role-list";
    }

    @RequestMapping(value = "/usermanagement/roles/put", method = RequestMethod.GET)
    public String ViewCreateRole(final ModelMap model){
        model.addAttribute("allPrivileges",privilegeService.findAll());
        model.addAttribute(new Role(""));
        return "/Roles/role-manage";
    }

    @RequestMapping(value = "/usermanagement/roles/{id}",method = RequestMethod.GET)
    public String viewEditRole(@PathVariable long id, final ModelMap model){
        model.addAttribute("allPrivileges",privilegeService.findAll());
        model.addAttribute("role",roleService.findById(id));
        return "/Roles/role-manage";
    }

    @RequestMapping(value = {"/usermanagement/roles/","/usermanagement/roles/{id}"},method = RequestMethod.POST)
    public String addRole(@Valid Role role, BindingResult result, final ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("allPrivileges", privilegeService.findAll());
            return "/Roles/role-manage";
        }
        if (role.getId() == null) {
            if (roleService.findByName(role.getName()).isPresent()) {
                model.addAttribute("allPrivileges", privilegeService.findAll());
                model.addAttribute("roleInUse", "Role " + role.getName() + " is already in use!");
                return "/Roles/role-manage";
            }
            roleService.save(role);
            return "redirect:/usermanagement/roles";
        }

        roleService.save(role);
        return "redirect:/usermanagement/roles";
    }

    @RequestMapping(value = "/usermanagement/roles/{id}/delete",method = RequestMethod.GET)
    public String deleteRole(@PathVariable long id, final ModelMap model){
        roleService.deleteById(id);
        model.clear();
        return "redirect:/usermanagement/roles";
    }
}
