package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.Service.OwnPrivilegeService;
import be.uantwerpen.labplanner.common.model.users.Privilege;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.service.users.PrivilegeService;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Controller
public class PrivilegeController {

    @Autowired
    private RoleService roleService;


    @Autowired
    private OwnPrivilegeService privilegeService;

    @ModelAttribute("allPrivileges")
    public Iterable<Privilege> populatePrivileges(){return privilegeService.findAll();}

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/privileges",method = RequestMethod.GET)
    public String showPrivileges(final ModelMap model){
        model.addAttribute("allPrivileges",privilegeService.findAll());
        return "/Privileges/privilege-list";
    }

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/privileges/put", method = RequestMethod.GET)
    public String ViewCreatePrivilege(final ModelMap model){
        model.addAttribute(new Privilege("",""));
        return "/Privileges/privilege-manage";
    }

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/privileges/{id}",method = RequestMethod.GET)
    public String viewEditPrivilege(@PathVariable long id, final ModelMap model){
        model.addAttribute("privilege",privilegeService.findById(id));
        return "/Privileges/privilege-manage";
    }

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = {"/usermanagement/privileges/","/usermanagement/privileges/{id}"},method = RequestMethod.POST)
    public String addPrivilege(@Valid Privilege privilege, BindingResult result, final ModelMap model) {
        if (result.hasErrors() || privilege.getName().trim().equals("") || privilege.getName() == null) {
            //validate on empty input name
            model.addAttribute("PrivilegeInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("privilege.errorAdd"));
            return "/Privileges/privilege-manage";
        }
        if (privilege.getId() == null) {
            if (privilegeService.findByName(privilege.getName()).isPresent()) {
                model.addAttribute("PrivilegeInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("privilege.errorUnique"));
                return "/Privileges/privilege-manage";
            }
            //trim input and save
            privilege.setName(privilege.getName().trim());
            privilegeService.save(privilege);
            return "redirect:/usermanagement/privileges";
        }

        Privilege tempPrivilege = privilegeService.findById(privilege.getId()).orElse(null);
        if(!tempPrivilege.getName().equals(privilege.getName())){
            if(privilegeService.findByName(privilege.getName()).isPresent()){
                model.addAttribute("PrivilegeInUse", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("privilege.errorUnique"));
                return "/Privileges/privilege-manage";
            }
            //trim input and save
            privilege.setName(privilege.getName().trim());
            privilegeService.save(privilege);
            return "redirect:/usermanagement/privileges";
        }
        //trim input and save
        privilege.setName(privilege.getName().trim());
        privilegeService.save(privilege);
        return "redirect:/usermanagement/privileges";
    }

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/privileges/{id}/delete",method = RequestMethod.GET)
    public String deletePrivilege(@PathVariable long id, final ModelMap model){
        List<Role> allRoles = roleService.findAll();
        boolean inUse = false;
        for (Role role : allRoles) {
            for (Privilege privilege : role.getPrivileges()) {
                if (privilege.getId()==id){
                    inUse = true;
                }
            }
        }

        if (inUse){
            model.addAttribute("allPrivileges",privilegeService.findAll());
            model.addAttribute("inUseError", ResourceBundle.getBundle("messages",LocaleContextHolder.getLocale()).getString("privilege.inUseError"));
            return "Privileges/privilege-list";
        }
        privilegeService.deleteById(id);
        model.clear();
        return "redirect:/usermanagement/privileges";
    }
}
