package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.*;
import be.uantwerpen.labplanner.Service.DeviceService;
import be.uantwerpen.labplanner.Service.RelationService;
import be.uantwerpen.labplanner.Service.StepService;
import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import be.uantwerpen.labplanner.common.service.users.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.swing.tree.ExpandVetoException;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; //belangrijke imports
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class EmailControllerTests {

    @Mock
    private StepController stepController;

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private DeviceService deviceService;

    @Mock
    private RoleService roleService;

    @Mock
    private UserService userService;

    @InjectMocks
    private EmailController emailController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(emailController).build();
    }

    @Test
    public void ViewMailTest() throws Exception{
        mockMvc.perform(get("/mail/maintanance/{id}","10"))

                .andExpect(status().is(200))
                .andExpect(view().name("Mail"))
                .andExpect(model().attribute("id", notNullValue()))
                .andDo(print());
    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")
    public void MaintananceMailValidTest() throws Exception{
        Device dev = new Device();
        long id = 10;
        dev.setId(id);

        when(deviceService.findById(id)).thenReturn(Optional.of(dev));

        Role admin = new Role("Administrator");
        admin.setId((long) 32);
        when(roleService.findByName("Administrator")).thenReturn(Optional.of(admin));
        Set<Role> roles = new HashSet<>();
        roles.add(admin);

        User user = new User("test","test");
        user.setId((long)34);
        user.setEmail("ruben.joosen@gmail.com");
        user.setRoles(roles);


        List<User> users = new ArrayList<>();
        users.add(user);

        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(post("/mail/maintanance/{id}",id)
                .param("sourceText","test"))

                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/devices"))
                .andExpect(model().attribute("MailSuccess", notNullValue()))
                .andDo(print());
    }
    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")
    public void MaintananceMailInvalidDeviceTest() throws Exception{
        Device dev = new Device();
        long id = 15;
        dev.setId(id);
        id = 16;

        when(deviceService.findById(id)).thenReturn(Optional.empty());

        Role admin = new Role("Administrator");
        admin.setId((long) 32);
        when(roleService.findByName("Administrator")).thenReturn(Optional.of(admin));
        Set<Role> roles = new HashSet<>();
        roles.add(admin);

        User user = new User("test","test");
        user.setId((long)34);
        user.setEmail("ruben.joosen@gmail.com");
        user.setRoles(roles);


        List<User> users = new ArrayList<>();
        users.add(user);

        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(post("/mail/maintanance/{id}",id)
                .param("sourceText","test"))


                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/devices"))
                .andExpect(model().attribute("deviceError", notNullValue()))
                .andDo(print());
    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")
    public void MaintananceMailInvalidEmailTest() throws Exception{
        Device dev = new Device();
        long id = 15;
        dev.setId(id);

        when(deviceService.findById(id)).thenReturn(Optional.of(dev));

        Role admin = new Role("Administrator");
        admin.setId((long) 32);
        when(roleService.findByName("Administrator")).thenReturn(Optional.of(admin));
        Set<Role> roles = new HashSet<>();
        roles.add(admin);

        User user = new User("test","test");
        user.setId((long)34);
        user.setEmail("ruben.joose");
        user.setRoles(roles);


        List<User> users = new ArrayList<>();
        users.add(user);

        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(post("/mail/maintanance/{id}",id)
                .param("sourceText","test"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/devices"))
                .andExpect(model().attribute("deviceError", notNullValue()))
                .andDo(print());
    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")

    public void testPeriodicMail() throws Exception{
        Role admin = new Role("Adminsitrator");
        admin.setId((long) 32);
        when(roleService.findByName("Administrator")).thenReturn(Optional.of(admin));

        User user = new User("test","test");
        user.setFirstName("first");
        user.setLastName("last");
        user.setEmail("ruben.joosen@student.uantwerpen.be");
        user.setId((long) 10);
        Set<Role> roles = new HashSet<>();
        roles.add(admin);

        user.setRoles(roles);

        List<User> users = new ArrayList<>();
        users.add(user);
        when(userService.findAll()).thenReturn(users);

        Step step = new Step();
        step.setId((long) 5);

        Experiment exp = new Experiment();
        exp.setId((long)55);
        exp.setExperimentname("test Exp");


        Map<Step,User> stepmap = new HashMap<>();
        stepmap.put(step,user);

        Map<Experiment,User> exMap = new HashMap<>();
        exMap.put(exp,user);


        when(stepController.getAddedSteps()).thenReturn(stepmap);
        when(stepController.getEditedSteps()).thenReturn(stepmap);
        when(stepController.getDeletedSteps()).thenReturn(stepmap);

        when(stepController.getAddedExperiments()).thenReturn(exMap);
        when(stepController.getEditedExperiments()).thenReturn(exMap);
        when(stepController.getDeletedExperiments()).thenReturn(exMap);

        emailController.sendPeriodicMail();

    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")

    public void testPeriodicMailNoUpdates() throws Exception{
        Role admin = new Role("Adminsitrator");
        admin.setId((long) 32);
        when(roleService.findByName("Administrator")).thenReturn(Optional.of(admin));

        User user = new User("test","test");
        user.setFirstName("first");
        user.setLastName("last");
        user.setEmail("ruben.joosen@student.uantwerpen.be");
        user.setId((long) 10);
        Set<Role> roles = new HashSet<>();
        roles.add(admin);

        user.setRoles(roles);

        List<User> users = new ArrayList<>();
        users.add(user);
        when(userService.findAll()).thenReturn(users);


        emailController.sendPeriodicMail();

    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")

    public void testLowStockMail() throws Exception{
        Role admin = new Role("Adminsitrator");
        admin.setId((long) 32);
        when(roleService.findByName("Administrator")).thenReturn(Optional.of(admin));

        User user = new User("test","test");
        user.setFirstName("first");
        user.setLastName("last");
        user.setEmail("ruben.joosen@student.uantwerpen.be");
        user.setId((long) 10);
        Set<Role> roles = new HashSet<>();
        roles.add(admin);

        user.setRoles(roles);

        List<User> users = new ArrayList<>();
        users.add(user);
        when(userService.findAll()).thenReturn(users);
        OwnProduct p = new OwnProduct();
        p.setName("test");
        p.setLowStockLevel((double) 10);
        p.setStockLevel((double) 5);
        emailController.sendLowStockEmail(p,user,"test");


    }

}
