package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.*;
import be.uantwerpen.labplanner.Repository.ExperimentTypeRepository;
import be.uantwerpen.labplanner.Repository.OfficeHoursRepository;
import be.uantwerpen.labplanner.Repository.SystemSettingsRepository;
import be.uantwerpen.labplanner.Service.*;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.repository.users.RoleRepository;
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
    private PieceOfMixtureService pieceOfMixtureService;
    @Mock
    private StepTypeService stepTypeService;
    @Mock
    private OwnProductService productService;
    @Mock
    private ExperimentTypeRepository experimentTypeRepository;

    @Mock
    private RelationService relationService;
    @Mock
    private SystemSettingsService systemSettingsService;
    @Mock
    private OfficeHoursService officeHoursService;
    @Mock
    private SystemSettingsRepository systemSettingsRepository;
    @Mock
    private OfficeHoursRepository officeHoursRepository;

    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    private StepController stepController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {


        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(stepController).build();
    }

    @Test
    @WithUserDetails(value = "ruben.joosen@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //View step list with admin
    public void ViewStepListTest() throws Exception {
        Step step = new Step();
        //  relations.add(relation);


        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        role.setId((long) 32);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/"))
                .andExpect(status().isOk())
                .andExpect(view().name("PlanningTool/planningtool"));

    }

    @Test
    @WithUserDetails(value = "bachelor@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //View step list with Bachelor and his steps
    public void ViewStepListBachelorTest() throws Exception {
        Step step = new Step();
        User user = new User("tester", "tester");
        user.setId((long) 40);
        step.setUser(user);

        List<Step> steps = new ArrayList<>();
        steps.add(step);
        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(stepService.findAll()).thenReturn(steps);
        role.setId((long) 32);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userSteps", hasSize(1)))
                .andExpect(view().name("PlanningTool/planningtool"));

    }

    @Test
    @WithUserDetails(value = "master@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //View step list with no steps
    public void ViewStepListEnmptyTest() throws Exception {
        Step step = new Step();
        User user = new User("tester", "tester");
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
                .andExpect(model().attribute("userSteps", hasSize(0)))
                .andExpect(view().name("PlanningTool/planningtool"));

    }

    @Test
    @WithUserDetails(value = "researcher@uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //View step as researcher
    public void ViewStepListResearcherTest() throws Exception {
        Step step = new Step();
        User user = new User("tester", "tester");
        user.setId((long) 40);
        step.setUser(user);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researccher", "tester");
        res.setId((long) 42);
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
        role.setId((long) 32);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("studentSteps", hasSize(1)))
                .andExpect(view().name("PlanningTool/planningtool"));

    }


    @Test
    @WithUserDetails(value = "ruben.joosen@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //view edit page of step
    public void ViewEditStepAdmin() throws Exception {
        Step step = new Step();
        step.setId((long) 5);
        User user = new User("tester", "tester");
        user.setId((long) 40);
        step.setUser(user);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        Role researcher = new Role("Researcher");
        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 33);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researccher", "tester");
        res.setId((long) 42);
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
        role.setId((long) 32);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allSteps", hasSize(1)))
                .andExpect(view().name("PlanningTool/step-manage"));

    }

    @Test
    @WithUserDetails(value = "ruben.joosen@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //view edit page of step
    public void ViewEditStepAdminNonValidId() throws Exception {
        Step step = new Step();
        step.setId((long) 5);
        User user = new User("tester", "tester");
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
        User res = new User("Researccher", "tester");
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
        mockMvc.perform(get("/planning/{id}", 9))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/planning/"));

    }

    @Test
    @WithUserDetails(value = "bachelor@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //view edit page of step as owner
    public void ViewEditStepAsOwner() throws Exception {
        Step step = new Step();
        step.setId((long) 5);
        User user = new User("tester", "tester");
        user.setId((long) 40);
        step.setUser(user);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 33);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
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
        role.setId((long) 32);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/{id}", 5))
                .andExpect(status().is(200))
                .andExpect(model().attribute("allSteps", hasSize(1)))
                .andExpect(view().name("PlanningTool/step-manage"));

    }

    @Test
    @WithUserDetails(value = "researcher@uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //view edit page of step as owner
    public void ViewEditStepAsResearcher() throws Exception {
        Step step = new Step();
        step.setId((long) 5);
        User user = new User("tester", "tester");
        user.setId((long) 40);
        step.setUser(user);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
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
        role.setId((long) 32);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allSteps", hasSize(1)))
                .andExpect(model().attribute("Step", notNullValue()))
                .andExpect(view().name("PlanningTool/step-manage"));

    }

    @Test
    @WithUserDetails(value = "master@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //view edit page of step as owner
    public void ViewEditStepAsNonOwner() throws Exception {
        Step step = new Step();
        step.setId((long) 5);
        User user = new User("tester", "tester");
        user.setId((long) 40);
        step.setUser(user);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
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
        when(stepService.findById((long) 39)).thenReturn(Optional.of(step));
        role.setId((long) 31);


        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/{id}", 5))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/planning/"));

    }

    @Test
    @WithUserDetails(value = "ruben.joosen@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //view edit page of step as owner
    public void AddStepInvalidInput() throws Exception {
        Step step = new Step();
        step.setId((long) 5);
        User user = new User("tester", "tester");
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
        User res = new User("Researcher", "tester");
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
        mockMvc.perform(post("/planning/").flashAttr("step", step))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/planning/"));

    }

    @Test
    @WithUserDetails(value = "ruben.joosen@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //view edit page of step as owner
    public void AddStepOverlap() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 39);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user, d1, "2021-03-18", "2021-03-18", "11:00", "12:00", "");
        step.setId((long) 5);

        Step s1 = new Step(user, d1, "2021-03-18", "2021-03-18", "11:00", "12:00", "");
        Step s2 = new Step(user, d1, "2021-03-17", "2021-03-17", "08:00", "18:00", "");
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
        User res = new User("Researcher", "tester");
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
        mockMvc.perform(post("/planning/").flashAttr("step", step))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/planning/"));

    }

    @Test
    @WithUserDetails(value = "ruben.joosen@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //view edit page of step as owner
    public void AddStepAdmin() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 39);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user, d1, "2021-03-18", "2021-03-18", "11:00", "12:00", "");
        step.setId((long) 5);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 41);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);

        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        when(productService.findAll()).thenReturn(new ArrayList<>());

        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(post("/planning/").flashAttr("step", step))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", notNullValue()))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));


    }

    @Test
    @WithUserDetails(value = "bachelor@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //view edit page of step as owner
    public void AddStepOwner() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user, d1, "2021-03-18", "2021-03-18", "11:00", "12:00", "");
        step.setId((long) 5);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
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
        when(productService.findAll()).thenReturn(new ArrayList<>());
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(post("/planning/").flashAttr("step", step))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Success"))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));


    }

    @Test
    @WithUserDetails(value = "researcher@uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //view edit page of step as owner
    public void AddStepResearcher() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 39);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user, d1, "2021-03-18", "2021-03-18", "11:00", "12:00", "");
        step.setId((long) 5);

        //roles
        Role role1 = new Role("Administrator");
        role1.setId((long) 32);
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));


        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        researcher.setId((long) 31);
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);

        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        when(productService.findAll()).thenReturn(new ArrayList<>());

        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(post("/planning/").flashAttr("step", step))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Success"))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));


    }

    @Test
    @WithUserDetails(value = "master@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //view edit page of step as owner
    public void AddStepNonValid() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user, d1, "2021-03-18", "2021-03-18", "11:00", "12:00", "");
        step.setId((long) 5);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
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
        role.setId((long) 32);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(post("/planning/").flashAttr("step", step))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Error"))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));


    }

    @Test
    @WithUserDetails(value = "ruben.joosen@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //view edit page of step as owner
    public void DeleteStepAdmin() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user, d1, "2021-03-18", "2021-03-18", "11:00", "12:00", "");
        step.setId((long) 5);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
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
        role.setId((long) 32);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/{id}/delete", 5))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }

    @Test
    @WithUserDetails(value = "ruben.joosen@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //view edit page of step as owner
    public void DeleteStepAdminNonValid() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 39);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user, d1, "2021-03-18", "2021-03-18", "11:00", "12:00", "");
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
        User res = new User("Researcher", "tester");
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
        mockMvc.perform(get("/planning/{id}/delete", 6))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }

    @Test
    @WithUserDetails(value = "bachelor@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //view edit page of step as owner
    public void DeleteStepOwner() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user, d1, "2021-03-18", "2021-03-18", "11:00", "12:00", "");
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
        User res = new User("Researcher", "tester");
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
        mockMvc.perform(get("/planning/{id}/delete", 5))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }

    @Test
    @WithUserDetails(value = "researcher@uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //view edit page of step as owner
    public void DeleteStepResearcher() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 39);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user, d1, "2021-03-18", "2021-03-18", "11:00", "12:00", "");
        step.setId((long) 5);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);

        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/{id}/delete", 5))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }

    @Test
    @WithUserDetails(value = "researcher@uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //view edit page of step as owner
    public void DeleteStepResearcherNotOfOwner() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 39);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user, d1, "2021-03-18", "2021-03-18", "11:00", "12:00", "");
        step.setId((long) 5);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        User user2 = new User("tester", "tester");
        user2.setId((long) 97);

        Set<User> students = new HashSet<>();
        students.add(user2);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);


        List<Step> steps = new ArrayList<>();
        steps.add(step);

        when(stepService.findAll()).thenReturn(steps);
        when(relationService.findAll()).thenReturn(rels);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/{id}/delete", 5))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }


    @Test
    @WithUserDetails(value = "master@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //view edit page of step as owner
    public void DeleteStepNonOwner() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        Device d1 = new Device();
        d1.setId((long) 10);

        Step step = new Step(user, d1, "2021-03-18", "2021-03-18", "11:00", "12:00", "");
        step.setId((long) 5);

        //roles
        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        when(stepService.findById((long) 5)).thenReturn(Optional.of(step));

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
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
        role.setId((long) 32);
        //   when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/planning/{id}/delete", 5))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Error"))
                .andExpect(view().name("redirect:/planning/"));
    }

    @Test
    @WithUserDetails(value = "mohammad.amir2@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //View experiment type list with admin
    public void ViewExperimentTypeListTest() throws Exception {
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        role.setId((long) 31);
        mockMvc.perform(get("/planning/experiments"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allExperimentTypes", hasSize(1)))
                .andExpect(view().name("PlanningTool/planning-exp-list"));

    }

    @Test
    @WithUserDetails(value = "bachelor@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //View experiment type list with Bachelor
    public void ViewExperimentTypeListBachelorTest() throws Exception {
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        role.setId((long) 31);
        mockMvc.perform(get("/planning/experiments"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allExperimentTypes", hasSize(1)))
                .andExpect(view().name("PlanningTool/planning-exp-list"));

    }

    @Test
    @WithUserDetails(value = "mohammad.amir2@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //Add experiment type as admin
    public void AddExperimentTypeTest() throws Exception {
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(stepTypeService.findAll()).thenReturn(stepTypes);

        mockMvc.perform(post("/planning/experiments/").flashAttr("experimentType", experimentType))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Message", "Experiment type successfully added."))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Success"))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/experiments"));
    }

    @Test
    @WithUserDetails(value = "mohammad.amir2@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //Try adding a new experiment type with an invalid continuity
    public void AddExperimentTypeInvalidContinuityTest() throws Exception {
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 72, "Hard", "After"), "TestStepType");
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(stepTypeService.findAll()).thenReturn(stepTypes);

        mockMvc.perform(post("/planning/experiments/").flashAttr("experimentType", experimentType))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Message", "There was a problem in adding the Experiment Type:\nInvalid value for minutes."))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Error"))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/experiments/put"));
    }

    @Test
    @WithUserDetails(value = "mohammad.amir2@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //Try adding a new experiment type with an already used name
    public void AddExperimentTypeInvalidNameTest() throws Exception {
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);
        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(stepTypeService.findAll()).thenReturn(stepTypes);

        mockMvc.perform(post("/planning/experiments/").flashAttr("experimentType", experimentType))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Message", "There was a problem in adding the Experiment Type:\nThis experiment type name is already occupied!"))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Error"))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/experiments/put"));
    }

    @Test
    @WithUserDetails(value = "mohammad.amir2@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //Delete an experiment type
    public void DeleteExperimentTypeTest() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);

        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);
        List<Experiment> experiments = new ArrayList<>();

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);
        mockMvc.perform(get("/planning/experiments/{id}/delete", 60))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Message", "Experiment type successfully deleted."))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Success"))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/experiments"));
    }

    @Test
    @WithUserDetails(value = "mohammad.amir2@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //Try to delete an experiment type that has still an experiment in use
    public void DeleteExperimentTypeWhenStillInUseTest() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);

        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2021-04-20");
        step.setStartHour("09:30");
        step.setEnd("2021-04-20");
        step.setEndHour("10:00");
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2021-04-20");
        step1.setStartHour("14:00");
        step1.setEnd("2021-04-20");
        step1.setEndHour("15:00");
        step1.setDevice(device1);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2021-04-20");
        experiment.setEndDate("2021-04-20");
        experiment.setSteps(steps);
        experiment.setId((long) 63);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);
        mockMvc.perform(get("/planning/experiments/{id}/delete", 60))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Message", "Experiment type is still in use."))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Error"))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/experiments"));
    }

    @Test
    @WithUserDetails(value = "mohammad.amir2@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //View of creating a new experiment type
    public void ViewCreateExperimentTypeTest() throws Exception {
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        deviceType.setId((long) 40);
        List<DeviceType> deviceTypes = new ArrayList<>();
        deviceTypes.add(deviceType);
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);


        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(deviceTypeService.findAll()).thenReturn(deviceTypes);
        mockMvc.perform(get("/planning/experiments/put"))
                .andExpect(status().is(200))
                .andExpect(model().attribute("allDeviceTypes", hasSize(1)))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-manage"));
    }

    @Test
    @WithUserDetails(value = "mohammad.amir2@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //View of editing an exisiting experiment type
    public void ViewEditExperimentTypeTest() throws Exception {
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        deviceType.setId((long) 40);
        List<DeviceType> deviceTypes = new ArrayList<>();
        deviceTypes.add(deviceType);
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);

        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(deviceTypeService.findAll()).thenReturn(deviceTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        mockMvc.perform(get("/planning/experiments/{id}", 60))
                .andExpect(status().is(200))
                .andExpect(model().attribute("allDeviceTypes", hasSize(1)))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-manage"));
    }


    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookFixedExperimentInsideOfficeHoursAdmin() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("14:00");
        step1.setEnd("2020-06-18");
        step1.setEndHour("15:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Success"))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }

    @Test
    @WithUserDetails(value = "researcher@uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookFixedExperimentInsideOfficeHourResearcher() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("14:00");
        step1.setEnd("2020-06-18");
        step1.setEndHour("15:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Success"))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }

    @Test
    @WithUserDetails(value = "bachelor@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookFixedExperimentInsideOfficeHoursBachelor() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);


        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("14:00");
        step1.setEnd("2020-06-18");
        step1.setEndHour("15:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Mixture mix = new Mixture();
        mix.setId((long) 63);

        PieceOfMixture pom = new PieceOfMixture(mix, "Test", 1);
        pom.setId((long) 64);
        List<PieceOfMixture> pomList = new ArrayList<>();
        pomList.add(pom);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        experiment.setPiecesOfMixture(pomList);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(pieceOfMixtureService.findAll()).thenReturn(pomList);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Success"))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }

    @Test
    @WithUserDetails(value = "bachelor@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookFixedExperimentNegativeMixtureAmountProblem() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);


        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("14:00");
        step1.setEnd("2020-06-18");
        step1.setEndHour("15:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Mixture mix = new Mixture();
        mix.setId((long) 63);

        PieceOfMixture pom = new PieceOfMixture(mix, "Test", -1);
        pom.setId((long) 64);
        List<PieceOfMixture> pomList = new ArrayList<>();
        pomList.add(pom);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        experiment.setPiecesOfMixture(pomList);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(pieceOfMixtureService.findAll()).thenReturn(pomList);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));

    }


    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookFixedExperimentOutsideOfficeHoursAdmin() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("14:00");
        step1.setEnd("2020-06-18");
        //outside office hours
        step1.setEndHour("20:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Success"))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }

    @Test
    @WithUserDetails(value = "bachelor@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookFixedExperimentOutsidefficeHoursBachelor() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("14:00");
        step1.setEnd("2020-06-18");
        //outside office hours
        step1.setEndHour("20:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        //roles
        Role role0 = new Role("Masterstudent");
        when(roleService.findByName("Masterstudent")).thenReturn(java.util.Optional.of(role0));
        role0.setId((long) 31);

        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        Role role2 = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(role2));
        role2.setId((long) 33);

        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));
    }



    @Test
    @WithUserDetails(value = "bachelor@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookExperimentWithWrongId() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("14:00");
        step1.setEnd("2020-06-18");
        //outside office hours
        step1.setEndHour("15:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        experiment.setId((long) 66);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        //roles
        Role role0 = new Role("Masterstudent");
        when(roleService.findByName("Masterstudent")).thenReturn(java.util.Optional.of(role0));
        role0.setId((long) 31);

        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        Role role2 = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(role2));
        role2.setId((long) 33);

        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/{id}", 6).flashAttr("experiment", experiment))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }


    @Test
    @WithUserDetails(value = "bachelor@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookExperimentWithNoExperimentType() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("14:00");
        step1.setEnd("2020-06-18");
        //outside office hours
        step1.setEndHour("15:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        //roles
        Role role0 = new Role("Masterstudent");
        when(roleService.findByName("Masterstudent")).thenReturn(java.util.Optional.of(role0));
        role0.setId((long) 31);

        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        Role role2 = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(role2));
        role2.setId((long) 33);

        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-book-custom"));
    }


    @Test
    @WithUserDetails(value = "bachelor@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookExperimentWithNoStepTypes() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", null, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("14:00");
        step1.setEnd("2020-06-18");
        //outside office hours
        step1.setEndHour("15:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setExperimentType(experimentType);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        //roles
        Role role0 = new Role("Masterstudent");
        when(roleService.findByName("Masterstudent")).thenReturn(java.util.Optional.of(role0));
        role0.setId((long) 31);

        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        Role role2 = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(role2));
        role2.setId((long) 33);

        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));
    }


    @Test
    @WithUserDetails(value = "bachelor@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookExperimentWithWrongStepTypeSize() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        StepType stepType2 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType2.setId((long) 159);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        stepTypes.add(stepType2);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("14:00");
        step1.setEnd("2020-06-18");
        //outside office hours
        step1.setEndHour("15:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setExperimentType(experimentType);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        //roles
        Role role0 = new Role("Masterstudent");
        when(roleService.findByName("Masterstudent")).thenReturn(java.util.Optional.of(role0));
        role0.setId((long) 31);

        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        Role role2 = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(role2));
        role2.setId((long) 33);

        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));
    }

    @Test
    @WithUserDetails(value = "bachelor@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookExperimentWithNoNameOfExperimentType() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType();
        experimentType.setStepTypes(stepTypes);
        experimentType.setIsFixedType(false);
        experimentType.setId((long) 60);
        ExperimentType experimentType1 = new ExperimentType();
        experimentType1.setStepTypes(stepTypes);
        experimentType1.setIsFixedType(false);
        experimentType1.setId((long) 160);
        experimentType1.setExpname("Custom0");
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType1);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("14:00");
        step1.setEnd("2020-06-18");
        //outside office hours
        step1.setEndHour("15:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        experiment.setExperimentType(experimentType);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        //roles
        Role role0 = new Role("Masterstudent");
        when(roleService.findByName("Masterstudent")).thenReturn(java.util.Optional.of(role0));
        role0.setId((long) 31);

        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        Role role2 = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(role2));
        role2.setId((long) 33);

        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);

        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }


    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void problemWithHardAfterContinuity() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("13:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("14:00");
        step1.setEnd("2020-06-18");
        step1.setEndHour("15:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));
    }

    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void problemWithSoftAtLeastAfterContinuity() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Soft (at least)", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("13:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("14:00");
        step1.setEnd("2020-06-18");
        step1.setEndHour("15:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));
    }

    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void problemWithSoftAtMostAfterContinuity() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Soft (at most)", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("12:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("16:30");
        step1.setEnd("2020-06-18");
        step1.setEndHour("17:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));
    }


    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void problemWithHardBeforeContinuity() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "Before"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "Before"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("13:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("14:00");
        step1.setEnd("2020-06-18");
        step1.setEndHour("15:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));
    }

    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void problemWithSoftAtLeastBeforeContinuity() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Soft (at least)", "Before"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "Before"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("13:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("14:00");
        step1.setEnd("2020-06-18");
        step1.setEndHour("15:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));
    }

    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void problemWithSoftAtMostBeforeContinuity() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Soft (at most)", "Before"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "Before"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("17:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("11:30");
        step1.setEnd("2020-06-18");
        step1.setEndHour("12:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));
    }

    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookExperimentFixedTimeEqualProblem() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(1, 0, "Soft (at least)", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType", true, "Equal", 1, 0);
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        // fixed time is not 1 hour
        step1.setStartHour("15:30");
        step1.setEnd("2020-06-18");
        step1.setEndHour("16:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));
    }

    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookExperimentFixedTimeAtLeastProblem() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(1, 0, "Soft (at least)", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType", true, "At least", 1, 0);
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("15:30");
        step1.setEnd("2020-06-18");
        step1.setEndHour("16:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));
    }

    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookExperimentFixedTimeAtMostProblem() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(1, 0, "Soft (at least)", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType", true, "At most", 0, 5);
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        // fixed time is not 1 hour
        step1.setStartHour("15:30");
        step1.setEnd("2020-06-18");
        step1.setEndHour("16:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));
    }


    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookExperimentFixedTimeEqualOK() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(1, 0, "Soft (at least)", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType", true, "Equal", 0, 30);
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        // fixed time is not 1 hour
        step1.setStartHour("15:30");
        step1.setEnd("2020-06-18");
        step1.setEndHour("16:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }

    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookExperimentFixedTimeAtLeastOK() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(1, 0, "Soft (at least)", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType", true, "At least", 0, 10);
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("15:30");
        step1.setEnd("2020-06-18");
        step1.setEndHour("16:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }

    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookExperimentFixedTimeAtMostOK() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(1, 0, "Soft (at least)", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType", true, "At most", 1, 0);
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        // fixed time is not 1 hour
        step1.setStartHour("15:30");
        step1.setEnd("2020-06-18");
        step1.setEndHour("16:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }


    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookExperimentOnWeekendProblem() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(1, 0, "Soft (at least)", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType", true, "Equal", 1, 0);
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-20");
        step.setStartHour("09:30");
        step.setEnd("2020-06-20");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-20");
        // fixed time is not 1 hour
        step1.setStartHour("15:30");
        step1.setEnd("2020-06-20");
        step1.setEndHour("16:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-20");
        experiment.setEndDate("2020-06-20");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        //roles
        Role role0 = new Role("Masterstudent");
        when(roleService.findByName("Masterstudent")).thenReturn(java.util.Optional.of(role0));
        role0.setId((long) 31);

        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        Role role2 = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(role2));
        role2.setId((long) 33);

        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));
    }

    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookExperimentInPastProblem() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(1, 0, "Soft (at least)", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType", true, "Equal", 1, 0);
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-05-15");
        step.setStartHour("09:30");
        step.setEnd("2020-05-15");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-05-15");
        // fixed time is not 1 hour
        step1.setStartHour("15:00");
        step1.setEnd("2020-05-15");
        step1.setEndHour("16:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-05-15");
        experiment.setEndDate("2020-05-15");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        //roles
        Role role0 = new Role("Masterstudent");
        when(roleService.findByName("Masterstudent")).thenReturn(java.util.Optional.of(role0));
        role0.setId((long) 31);

        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        Role role2 = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(role2));
        role2.setId((long) 33);

        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));
    }




    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookExperimentInputProblem() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(1, 0, "Soft (at least)", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType", true, "Equal", 1, 0);
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("");
        step.setStartHour("09:30");
        step.setEnd("");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        // fixed time is not 1 hour
        step1.setStartHour("15:30");
        step1.setEnd("2020-06-18");
        step1.setEndHour("16:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));
    }




    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookExperimentOnHolidayProblem() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(1, 0, "Soft (at least)", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType", true, "Equal", 1, 0);
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-12-24");
        step.setStartHour("09:30");
        step.setEnd("2020-12-24");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-12-24");
        // fixed time is not 1 hour
        step1.setStartHour("15:30");
        step1.setEnd("2020-12-24");
        step1.setEndHour("16:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-12-24");
        experiment.setEndDate("2020-12-24");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));
    }


    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookExperimentOverNightProblem() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(1, 0, "Soft (at least)", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType", true, "Equal", 1, 0);
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-17");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        // fixed time is not 1 hour
        step1.setStartHour("15:30");
        step1.setEnd("2020-06-18");
        step1.setEndHour("16:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        //roles
        Role role0 = new Role("Masterstudent");
        when(roleService.findByName("Masterstudent")).thenReturn(java.util.Optional.of(role0));
        role0.setId((long) 31);

        Role role1 = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role1));
        role1.setId((long) 32);

        Role role2 = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(role2));
        role2.setId((long) 33);

        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);

        // Test that experiment was not booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));
    }


    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    public void bookCustomExperiment() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(1, 0, "Soft (at least)", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType", true, "Equal", 1, 0);
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        //set custom experiment
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, false);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        // fixed time is not 1 hour
        step1.setStartHour("15:00");
        step1.setEnd("2020-06-18");
        step1.setEndHour("16:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        //check that experiment was booked
        mockMvc.perform(post("/planning/experiments/book/").flashAttr("experiment", experiment))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Success"))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }

    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //Try to delete an experiment type that has still an experiment in use
    public void DeleteFixedExperiment() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);

        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-04-20");
        step.setStartHour("09:30");
        step.setEnd("2020-04-20");
        step.setEndHour("10:00");
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-04-20");
        step1.setStartHour("14:00");
        step1.setEnd("2020-04-20");
        step1.setEndHour("15:00");
        step1.setDevice(device1);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);
        Mixture mix = new Mixture();
        mix.setId((long) 63);
        PieceOfMixture pom = new PieceOfMixture(mix, "Test", 1);
        pom.setId((long) 64);
        List<PieceOfMixture> pomList = new ArrayList<>();
        pomList.add(pom);

        Experiment experiment = new Experiment();
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-04-20");
        experiment.setEndDate("2020-04-20");
        experiment.setSteps(steps);
        experiment.setUser(user);
        experiment.setPiecesOfMixture(pomList);
        experiment.setId((long) 63);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(experimentService.findById((long) 63)).thenReturn(Optional.of(experiment));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);
        mockMvc.perform(get("/planning/experiments/book/{id}/delete", 63))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Success"))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }

    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //Try to delete an experiment type that has still an experiment in use
    public void deleteCustomExperiment() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);

        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, false);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-04-20");
        step.setStartHour("09:30");
        step.setEnd("2020-04-20");
        step.setEndHour("10:00");
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-04-20");
        step1.setStartHour("14:00");
        step1.setEnd("2020-04-20");
        step1.setEndHour("15:00");
        step1.setDevice(device1);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);


        Mixture mix = new Mixture();
        mix.setId((long) 63);

        PieceOfMixture pom = new PieceOfMixture(mix, "Test", 1);
        pom.setId((long) 64);
        List<PieceOfMixture> pomList = new ArrayList<>();
        pomList.add(pom);

        Experiment experiment = new Experiment();
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-04-20");
        experiment.setEndDate("2020-04-20");
        experiment.setSteps(steps);
        experiment.setUser(user);
        experiment.setPiecesOfMixture(pomList);
        experiment.setId((long) 63);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(experimentService.findById((long) 63)).thenReturn(Optional.of(experiment));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);
        mockMvc.perform(get("/planning/experiments/book/{id}/delete", 63))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.flash().attribute("Status", "Success"))
                .andDo(print())
                .andExpect(view().name("redirect:/planning/"));
    }


    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //Try to delete an experiment type that has still an experiment in use
    public void viewBookFixedExperiment() throws Exception {
        User user = new User("tester", "tester");

        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        role.setId((long) 31);
        mockMvc.perform(get("/planning/experiments/book/fixed"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allExperimentTypes", hasSize(1)))
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));

    }

    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //Try to delete an experiment type that has still an experiment in use
    public void viewBookCustomExperiment() throws Exception {
        User user = new User("tester", "tester");

        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(4, 0, "Hard", "After"), "TestStepType");
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType");
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        // CustomExperiment was created
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, false);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        role.setId((long) 31);
        mockMvc.perform(get("/planning/experiments/book/custom"))
                .andExpect(status().isOk())
                //only custom experiment was created, so size has to be 0
                .andExpect(model().attribute("allExperimentTypes", hasSize(0)))
                .andExpect(view().name("PlanningTool/planning-exp-book-custom"));

    }

    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //Try to delete an experiment type that has still an experiment in use
    public void viewEditCustomExperiment() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(1, 0, "Soft (at least)", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType", true, "Equal", 1, 0);
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("15:30");
        step1.setEnd("2020-06-18");
        step1.setEndHour("16:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        experiment.setId((long) 66);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(experimentService.findById((long) 66)).thenReturn(Optional.of(experiment));
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        mockMvc.perform(get("/planning/experiments/book/{id}", 66))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allExperimentTypes", hasSize(1)))
                .andExpect(model().attribute("allExperiments", hasSize(1)))
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));

    }

    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //Try to delete an experiment type that has still an experiment in use
    public void viewExperimentInfo() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(1, 0, "Soft (at least)", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType", true, "Equal", 1, 0);
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("15:30");
        step1.setEnd("2020-06-18");
        step1.setEndHour("16:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        experiment.setId((long) 66);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(experimentService.findById((long) 66)).thenReturn(Optional.of(experiment));
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        mockMvc.perform(get("/planning/experiments/book/info/{id}", 66))
                .andExpect(status().isOk())
                .andExpect(view().name("PlanningTool/planning-exp-info"));

    }

    @Test
    @WithUserDetails(value = "ondrej.bures@student.uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //Try to delete an experiment type that has still an experiment in use
    public void EditStepThatIsPartOfExperiment() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(1, 0, "Soft (at least)", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType", true, "Equal", 1, 0);
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        step.setId((long) 57);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("15:30");
        step1.setEnd("2020-06-18");
        step1.setEndHour("16:30");
        step1.setDevice(device1);
        step1.setUser(user);
        step1.setId((long) 56);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Mixture mix = new Mixture();
        mix.setId((long) 63);

        PieceOfMixture pom = new PieceOfMixture(mix, "Test", 1);
        pom.setId((long) 64);
        List<PieceOfMixture> pomList = new ArrayList<>();
        pomList.add(pom);


        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        experiment.setPiecesOfMixture(pomList);
        experiment.setId((long) 66);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentService.findAll()).thenReturn(experiments);
        when(stepService.findAll()).thenReturn(steps);
        when(stepService.findById((long) 57)).thenReturn(Optional.of(step));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        mockMvc.perform(post("/planning/").flashAttr("step", step))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/planning/"));

    }


    @Test
    @WithUserDetails(value = "researcher@uantwerpen.be", userDetailsServiceBeanName = "newSecurityService")
    //Try to delete an experiment type that has still an experiment in use
    public void viewExperimentInfoAsResearcher() throws Exception {
        User user = new User("tester", "tester");
        user.setId((long) 40);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("TestDeviceType");
        List<StepType> stepTypes = new ArrayList<>();
        StepType stepType = new StepType(deviceType, new Continuity(1, 0, "Soft (at least)", "After"), "TestStepType");
        stepType.setId((long) 58);
        StepType stepType1 = new StepType(deviceType, new Continuity(0, 0, "No", "After"), "New StepType", true, "Equal", 1, 0);
        stepType1.setId((long) 59);
        stepTypes.add(stepType);
        stepTypes.add(stepType1);
        ExperimentType experimentType = new ExperimentType("TestExperimentType", stepTypes, true);
        experimentType.setId((long) 60);
        List<ExperimentType> experimentTypes = new ArrayList<>();
        experimentTypes.add(experimentType);

        Device device = new Device();
        device.setDeviceType(deviceType);
        device.setDevicename("testdev1");
        device.setId((long) 61);
        Device device1 = new Device();
        device1.setDeviceType(deviceType);
        device1.setDevicename("testdev2");
        device1.setId((long) 62);
        Step step = new Step();
        step.setStepType(stepType);
        step.setStart("2020-06-18");
        step.setStartHour("09:30");
        step.setEnd("2020-06-18");
        step.setEndHour("10:00");
        step.setUser(user);
        step.setDevice(device);
        Step step1 = new Step();
        step1.setStepType(stepType);
        step1.setStart("2020-06-18");
        step1.setStartHour("15:30");
        step1.setEnd("2020-06-18");
        step1.setEndHour("16:00");
        step1.setDevice(device1);
        step1.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step1);

        Experiment experiment = new Experiment();
        experiment.setExperimentname("Exp1");
        experiment.setExperimentType(experimentType);
        experiment.setStartDate("2020-06-18");
        experiment.setEndDate("2020-06-18");
        experiment.setSteps(steps);
        experiment.setUser(user);
        experiment.setId((long) 66);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(experiment);

        Role researcher = new Role("Researcher");
        when(roleService.findByName("Researcher")).thenReturn(java.util.Optional.of(researcher));
        researcher.setId((long) 31);

        Set<User> students = new HashSet<>();
        students.add(user);
        //researcher
        User res = new User("Researcher", "tester");
        res.setId((long) 42);
        //relation
        Relation rel = new Relation();
        rel.setResearcher(res);
        rel.setStudents(students);

        List<Relation> rels = new ArrayList<>();
        rels.add(rel);

        Role role = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(java.util.Optional.of(role));
        when(experimentTypeService.findAll()).thenReturn(experimentTypes);
        when(experimentService.findAll()).thenReturn(experiments);
        when(experimentService.findById((long) 66)).thenReturn(Optional.of(experiment));
        when(stepTypeService.findAll()).thenReturn(stepTypes);
        when(experimentTypeService.findById((long) 60)).thenReturn(Optional.of(experimentType));
        when(relationService.findAll()).thenReturn(rels);
        role.setId((long) 32);

        mockMvc.perform(get("/planning/experiments/book/{id}", 66))
                .andExpect(status().isOk())
                .andExpect(view().name("PlanningTool/planning-exp-book-fixed"));

    }
}
