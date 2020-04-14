package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.Relation;
import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Repository.ExperimentTypeRepository;
import be.uantwerpen.labplanner.Service.*;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.repository.users.UserRepository;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class StepControllerTests {


    @Mock
    private DeviceService deviceService;
    @Mock
    private StepService stepService;
    @Mock
    private DeviceTypeService deviceTypeService;
    @Mock
    private RoleService roleService;
    @Mock
    private ExperimentService experimentService;
    @Mock
    private ExperimentTypeService experimentTypeService;
    @Mock
    private MixtureService mixtureService;
    @Mock
    private StepTypeService stepTypeService;
    @Mock
    private ExperimentTypeRepository experimentTypeRepository;

    @Mock
    private RelationService relationService;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private StepController stepController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){


        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(this.stepController).build();
    }

    @Test
    @WithUserDetails("Ruben")
    //View step list with admin
    public void ViewStepListTest() throws Exception{
        Step step = new Step();
      //  relations.add(relation);


        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        role.setId((long) 31);
     //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/"))
                .andExpect(status().isOk())
                .andExpect(view().name("PlanningTool/planningtool"));

    }

    @Test
    @WithUserDetails("Bachelor")
    //View step list with Bachelor and his steps
    public void ViewStepListBachelorTest() throws Exception{
        Step step = new Step();
        User user = new User("tester","tester");
        user.setId((long) 39);
        step.setUser(user);

        List<Step> steps = new ArrayList<>();
        steps.add(step);
        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        role.setId((long) 31);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userSteps",hasSize(1)))
                .andExpect(view().name("PlanningTool/planningtool"));

    }

    @Test
    @WithUserDetails("Master")
    //View step list with no steps
    public void ViewStepListEnmptyTest() throws Exception{
        Step step = new Step();
        User user = new User("tester","tester");
        user.setId((long) 39);
        step.setUser(user);

        List<Step> steps = new ArrayList<>();
        steps.add(step);
        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        role.setId((long) 31);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userSteps",hasSize(0)))
                .andExpect(view().name("PlanningTool/planningtool"));

    }

    @Test
    @WithUserDetails("Researcher")
    //View step as researcher
    public void ViewStepListResearcherTest() throws Exception{
        Step step = new Step();
        User user = new User("tester","tester");
        user.setId((long) 39);
        step.setUser(user);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researccher","tester");
        res.setId((long) 41);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);
        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 31);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("studentSteps",hasSize(1)))
                .andExpect(view().name("PlanningTool/planningtool"));

    }


    @Test
    @WithUserDetails("Ruben")
    //view edit page of step
    public void ViewEditStepAdmin() throws Exception{
        Step step = new Step();
        step.setId((long) 5);
        User user = new User("tester","tester");
        user.setId((long) 39);
        step.setUser(user);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 31);

        Role researcher = new Role("Researcher");
        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 32);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researccher","tester");
        res.setId((long) 41);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);
        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 31);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/{id}",5))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allSteps",hasSize(1)))
                .andExpect(view().name("/PlanningTool/step-manage"));

    }

    @Test
    @WithUserDetails("Ruben")
    //view edit page of step
    public void ViewEditStepAdminNonValidId() throws Exception{
        Step step = new Step();
        step.setId((long) 5);
        User user = new User("tester","tester");
        user.setId((long) 39);
        step.setUser(user);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 31);

        when(stepService.findById((long) 9)).thenReturn(Optional.empty());

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 32);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researccher","tester");
        res.setId((long) 41);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);
        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 31);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/{id}",9))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/planning/"));

    }

    @Test
    @WithUserDetails("Bachelor")
    //view edit page of step as owner
    public void ViewEditStepAsOwner() throws Exception{
        Step step = new Step();
        step.setId((long) 5);
        User user = new User("tester","tester");
        user.setId((long) 39);
        step.setUser(user);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 31);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 32);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher","tester");
        res.setId((long) 41);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);
        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 31);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/{id}",5))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allSteps",hasSize(1)))
                .andExpect(view().name("/PlanningTool/step-manage"));

    }

    @Test
    @WithUserDetails("Researcher")
    //view edit page of step as owner
    public void ViewEditStepAsResearcher() throws Exception{
        Step step = new Step();
        step.setId((long) 5);
        User user = new User("tester","tester");
        user.setId((long) 39);
        step.setUser(user);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 31);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 30);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher","tester");
        res.setId((long) 41);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);
        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 31);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/{id}",5))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allSteps",hasSize(1)))
                .andExpect(model().attribute("Step",notNullValue()))
                .andExpect(view().name("/PlanningTool/step-manage"));

    }

    @Test
    @WithUserDetails("Master")
    //view edit page of step as owner
    public void ViewEditStepAsNonOwner() throws Exception{
        Step step = new Step();
        step.setId((long) 5);
        User user = new User("tester","tester");
        user.setId((long) 39);
        step.setUser(user);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 31);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 30);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher","tester");
        res.setId((long) 41);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);
        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 31);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/{id}",5))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/planning/"));

    }

    @Test
    @WithUserDetails("Ruben")
    //view edit page of step as owner
    public void AddStepInvalidInput() throws Exception{
        Step step = new Step();
        step.setId((long) 5);
        User user = new User("tester","tester");
        user.setId((long) 39);
        step.setUser(user);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 31);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 30);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher","tester");
        res.setId((long) 41);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);
        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 31);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(post("/planning/").flashAttr("step",step))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/planning/"));

    }

    @Test
    @WithUserDetails("Ruben")
    //view edit page of step as owner
    public void AddStepOverlap() throws Exception{
        User user = new User("tester","tester");
        user.setId((long) 39);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user,d1,"2020-03-18","2020-03-18","11:00","12:00","");
        step.setId((long) 5);

        Step s1= new Step(user,d1,"2020-03-18","2020-03-18","11:00","12:00","");
        Step s2= new Step(user,d1,"2020-03-17","2020-03-17","08:00","18:00","");
        s1.setId((long) 6);
        s2.setId((long) 7);
        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 31);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 30);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher","tester");
        res.setId((long) 41);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(s1);
        steps.add(s2);
        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 31);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(post("/planning/").flashAttr("step",step))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/planning/"));

    }

    @Test
    @WithUserDetails("Ruben")
    //view edit page of step as owner
    public void AddStepAdmin() throws Exception{
        User user = new User("tester","tester");
        user.setId((long) 39);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user,d1,"2020-03-18","2020-03-18","11:00","12:00","");
        step.setId((long) 5);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 31);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 30);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher","tester");
        res.setId((long) 41);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 31);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(post("/planning/").flashAttr("step",step))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", notNullValue()))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));



    }

    @Test
    @WithUserDetails("Bachelor")
    //view edit page of step as owner
    public void AddStepOwner() throws Exception{
        User user = new User("tester","tester");
        user.setId((long) 39);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user,d1,"2020-03-18","2020-03-18","11:00","12:00","");
        step.setId((long) 5);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 31);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 30);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher","tester");
        res.setId((long) 41);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 31);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(post("/planning/").flashAttr("step",step))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Success"))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));



    }

    @Test
    @WithUserDetails("Researcher")
    //view edit page of step as owner
    public void AddStepResearcher() throws Exception{
        User user = new User("tester","tester");
        user.setId((long) 39);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user,d1,"2020-03-18","2020-03-18","11:00","12:00","");
        step.setId((long) 5);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 31);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 30);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher","tester");
        res.setId((long) 41);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 31);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(post("/planning/").flashAttr("step",step))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Success"))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));



    }

    @Test
    @WithUserDetails("Master")
    //view edit page of step as owner
    public void AddStepNonValid() throws Exception{
        User user = new User("tester","tester");
        user.setId((long) 39);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user,d1,"2020-03-18","2020-03-18","11:00","12:00","");
        step.setId((long) 5);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 31);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 30);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher","tester");
        res.setId((long) 41);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 31);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(post("/planning/").flashAttr("step",step))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Error"))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));



    }

    @Test
    @WithUserDetails("Ruben")
    //view edit page of step as owner
    public void DeleteStepAdmin() throws Exception{
        User user = new User("tester","tester");
        user.setId((long) 39);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user,d1,"2020-03-18","2020-03-18","11:00","12:00","");
        step.setId((long) 5);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 31);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 30);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher","tester");
        res.setId((long) 41);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 31);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/{id}/delete",5))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }

    @Test
    @WithUserDetails("Ruben")
    //view edit page of step as owner
    public void DeleteStepAdminNonValid() throws Exception{
        User user = new User("tester","tester");
        user.setId((long) 39);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user,d1,"2020-03-18","2020-03-18","11:00","12:00","");
        step.setId((long) 5);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 31);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 30);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher","tester");
        res.setId((long) 41);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 31);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/{id}/delete",6))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }

    @Test
    @WithUserDetails("Bachelor")
    //view edit page of step as owner
    public void DeleteStepOwner() throws Exception{
        User user = new User("tester","tester");
        user.setId((long) 39);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user,d1,"2020-03-18","2020-03-18","11:00","12:00","");
        step.setId((long) 5);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 31);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 30);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher","tester");
        res.setId((long) 41);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 31);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/{id}/delete",5))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }

    @Test
    @WithUserDetails("Researcher")
    //view edit page of step as owner
    public void DeleteStepResearcher() throws Exception{
        User user = new User("tester","tester");
        user.setId((long) 39);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user,d1,"2020-03-18","2020-03-18","11:00","12:00","");
        step.setId((long) 5);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 31);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 30);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher","tester");
        res.setId((long) 41);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 31);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/{id}/delete",5))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }


    @Test
    @WithUserDetails("Master")
    //view edit page of step as owner
    public void DeleteStepNonOwner() throws Exception{
        User user = new User("tester","tester");
        user.setId((long) 39);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user,d1,"2020-03-18","2020-03-18","11:00","12:00","");
        step.setId((long) 5);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 31);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 30);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher","tester");
        res.setId((long) 41);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 31);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/{id}/delete",5))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Error"))
                .andExpect(view().name("redirect:/planning/"));
    }








}
