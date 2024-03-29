package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.Relation;
import be.uantwerpen.labplanner.Repository.CompositionRepository;
import be.uantwerpen.labplanner.Repository.RelationRepository;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Controller
public class RelationController {

    Logger logger = LoggerFactory.getLogger(HomeController.class);


    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RelationService relationService;

    @ModelAttribute("allRelations")
    public Iterable<Relation> populateRelations() {return this.relationService.findAll();}

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/users/relations",method = RequestMethod.GET)
    public String showRelations(final ModelMap model){
        model.addAttribute("allRelations",relationService.findAll());
        return "Users/relation-list";
    }

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/users/relations/put",method = RequestMethod.GET)
    public String viewCreateRelation(@org.jetbrains.annotations.NotNull final ModelMap model){
        // add list with all the users which have a Researcher Role in their role list.
        Role researcher = roleService.findByName("Researcher").orElse(null);
        List<User> researchers = new ArrayList<>();
        for (User user : userService.findAll()){
            if (user.getRoles().contains(researcher)){
                researchers.add(user);
            }
        }
        model.addAttribute("allResearchers",researchers);

        //add list with all users which have a Bachelor or Master Role in their role list.
        Role Bachelor = roleService.findByName("Bachelorstudent").orElse(null);
        Role Master = roleService.findByName("Masterstudent").orElse(null);
        List<User> students = new ArrayList<>();
        for (User user : userService.findAll()){
            if ((user.getRoles().contains(Bachelor))|| (user.getRoles().contains(Master)) || (user.getRoles().contains(researcher))){
                students.add(user);
            }
        }
        model.addAttribute("allStudents",students);

        model.addAttribute(new Relation(""));
        return "Users/relation-manage";
    }

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/users/relations/{id}",method = RequestMethod.GET)
    public String viewEditRelation(@PathVariable("id") long id, final ModelMap model){

        Relation relation = relationService.findById(id).orElse(null);
        if (relation == null){
            model.addAttribute("allRelations",relationService.findAll());
            model.addAttribute("RelationError", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("relation.error"));
            return "Users/relation-list";
        }



        // add list with all the users which have a Researcher Role in their role list.
        Role researcher = roleService.findByName("Researcher").orElse(null);


        List<User> researchers = new ArrayList<>();
        for (User user : userService.findAll()){
            if (user.getRoles().contains(researcher)){
                researchers.add(user);
            }
        }
        model.addAttribute("allResearchers",researchers);

        //add list with all users which have a Bachelor or Master Role in their role list.
        Role Bachelor = roleService.findByName("Bachelorstudent").orElse(null);
        Role Master = roleService.findByName("Masterstudent").orElse(null);
        List<User> students = new ArrayList<>();
        for (User user : userService.findAll()){
            if ((user.getRoles().contains(Bachelor))|| (user.getRoles().contains(Master))|| (user.getRoles().contains(researcher))){
                students.add(user);
            }
        }
        model.addAttribute("allStudents",students);

        model.addAttribute("relation",relationService.findById(id).orElse(null));
        return "Users/relation-manage";
    }

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = {"/usermanagement/users/relations/","/usermanagement/users/relations/{id}"},method = RequestMethod.POST)
    public String addRelation(@Valid Relation relation, BindingResult result, final ModelMap model) {
        //check if researcher is researcher
        Boolean isResearcher = false;
        for (Role role : relation.getResearcher().getRoles()) {
            if (role.getName().toLowerCase().contains("researcher")) {
                isResearcher = true;
            }
        }

        if (!isResearcher) {
            //error message en return researcher is not valid
            //with return statement
            model.addAttribute("RelationError", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("relation.researcherError"));
            model.addAttribute("allUsers", userService.findAll());
            return "Users/relation-manage";
        }


        //Check for all the students in list that they are only student
        if (relation.getStudents() != null)
        {
            for (User student : relation.getStudents()) {
                Boolean studentTrue = false;
                for (Role role : student.getRoles()) {
                    if ((role.getName().toLowerCase().contains("student")) || (role.getName().toLowerCase().contains("researcher"))) {
                        studentTrue = true;
                    }
                }

                //delete every checked user from list which is not a student
                if (!studentTrue) {
                    relation.deleteStudent(student);
                }
            }
        }


        model.addAttribute("allUsers", userService.findAll());
        relationService.save(relation);

        return "redirect:/usermanagement/users/relations";
    }

    @PreAuthorize("hasAnyAuthority('User Management')")
    @RequestMapping(value = "/usermanagement/users/relations/{id}/delete",method = RequestMethod.GET)
    public String deleteRelation(@PathVariable long id, final ModelMap model) {

        Relation relation = relationService.findById(id).orElse(null);
        if (relation == null){
            model.addAttribute("allRelations",relationService.findAll());
            model.addAttribute("RelationError", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("relation.error"));
            return "Users/relation-list";
        }


        relationService.deleteById(id);
        model.clear();
        return "redirect:/usermanagement/users/relations";
    }

}
