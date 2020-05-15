package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Relation;
import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Service.RelationService;
import be.uantwerpen.labplanner.Service.StepService;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import be.uantwerpen.labplanner.common.service.users.UserService;
import com.mysql.cj.x.protobuf.Mysqlx;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.swing.text.html.Option;
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
public class RegisterControllerTests {

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private StepService stepService;

    @Mock
    private RelationService relationService;

    @Mock
    private EmailController emailController;

    @Mock
    private PasswordEncoder passwordEncoder;


    @InjectMocks
    private RegisterController registerController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(registerController).build();
    }

    @Test
    public void ShowRegistredUsersTest() throws Exception{
        User u = new User("test","test");
        Set<User> users = new HashSet<>();
        users.add(u);
        registerController.setRegistredUsers(users);

        mockMvc.perform(get("/registrations"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allRegistrations",hasSize(1)));
    }

    @Test
    public void acceptRegistredUSerTests() throws Exception{
        User u = new User("test","test");
        u.setUaNumber("1");
        u.setEmail("test@test.test");
        Set<User> users = new HashSet<>();
        users.add(u);
        registerController.setRegistredUsers(users);
        mockMvc.perform(get("/registrations/add/{ua}","1"))
                .andExpect(status().is(302));
    }

    @Test
    public void DeclineRegistredUSerTests() throws Exception{
        User u = new User("test","test");
        u.setUaNumber("1");
        u.setEmail("test@test.test");
        Set<User> users = new HashSet<>();
        users.add(u);
        registerController.setRegistredUsers(users);
        mockMvc.perform(get("/registrations/decline/{ua}","1"))
                .andExpect(status().is(302));
    }

    @Test
    // Show user manage page test
    public void ViewRegisterUserTest() throws Exception{
        Role r = new Role("testRole");
        List<Role> roles = new ArrayList<Role>();
        roles.add(r);

        when(roleService.findAll()).thenReturn(roles);

        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allRoles",hasSize(1)))
                .andExpect(model().attribute("user",instanceOf(User.class)))
                .andExpect(view().name("register-manage"))
                .andDo(print());

    }

    @Test
    //Add user with non valid name
    public void AddValidTest() throws Exception {
        Role r = new Role("test");
        r.setId((long) 15);
        List<Role> roles = new ArrayList<Role>();
        roles.add(r);

        Set<Role> rolSet = new HashSet<>();
        rolSet.add(r);

        User user = new User("admin", "Ruben1","ruben.joosen@gmail.com","Ruben","Joosen","20164473","","",rolSet,null,null);

        List<User> users = new ArrayList<>();
        users.add(user);
        //empty password string
      //  when(userService.findAll()).thenReturn(users);

        when(roleService.findAll()).thenReturn(roles);
        mockMvc.perform(post("/register").flashAttr("user", user))
                .andExpect(status().is(302))
                .andExpect(model().attribute("UserInUse", nullValue()))
                .andExpect(view().name("redirect:/login?registered"))
                .andDo(print());

    }

    @Test
    //Add user with non valid name
    public void AddNonValidNameUserTest() throws Exception {
        User user = new User("admin", "");
        long id = 10;
        user.setId(id);

        Role r = new Role("test");
        r.setId((long) 15);
        List<Role> roles = new ArrayList<Role>();
        roles.add(r);

        //empty password string
        when(roleService.findAll()).thenReturn(roles);
        mockMvc.perform(post("/register").flashAttr("user", user))
                .andExpect(status().is(200))
                .andExpect(model().attribute("UserInUse", notNullValue()))
                .andExpect(view().name("register-manage"))
                .andDo(print());

    }

    @Test
    //Add user with non valid name
    public void AddNonValidUANumberTest() throws Exception {
        Role r = new Role("test");
        r.setId((long) 15);
        List<Role> roles = new ArrayList<Role>();
        roles.add(r);

        Set<Role> rolSet = new HashSet<>();
        rolSet.add(r);

        User user = new User("admin", "Ruben1","ruben.joosen@gmail.com","Ruben","Joosen","45","","",rolSet,null,null);

        List<User> users = new ArrayList<>();
        users.add(user);
        //empty password string
        //  when(userService.findAll()).thenReturn(users);

        when(roleService.findAll()).thenReturn(roles);
        mockMvc.perform(post("/register").flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(model().attribute("UserInUse", notNullValue()))
                .andExpect(view().name("register-manage"))
                .andDo(print());

    }

    @Test
    //Add user with non valid name
    public void AddDoubleUANumberTest() throws Exception {
        Role r = new Role("test");
        r.setId((long) 15);
        List<Role> roles = new ArrayList<Role>();
        roles.add(r);

        Set<Role> rolSet = new HashSet<>();
        rolSet.add(r);

        User user = new User("admin", "Ruben1","ruben.joosen@gmail.com","Ruben","Joosen","20164473","","",rolSet,null,null);

        List<User> users = new ArrayList<>();
        users.add(user);
        //empty password string
        when(userService.findAll()).thenReturn(users);

        when(roleService.findAll()).thenReturn(roles);
        mockMvc.perform(post("/register").flashAttr("user", user))
                .andExpect(status().is(200))
                .andExpect(model().attribute("UserInUse", notNullValue()))
                .andExpect(view().name("register-manage"))
                .andDo(print());

    }

    @Test
    //Add user with non valid name
    public void AddDoubleMailTest() throws Exception {
        Role r = new Role("test");
        r.setId((long) 15);
        List<Role> roles = new ArrayList<Role>();
        roles.add(r);

        Set<Role> rolSet = new HashSet<>();
        rolSet.add(r);

        User user = new User("admin", "Ruben1","ruben.joosen@gmail.com","Ruben","Joosen","20164473","","",rolSet,null,null);
        User user2 = new User("admin", "Ruben1","ruben.joosen@gmail.com","Ruben","Joosen","20714473","","",rolSet,null,null);

        List<User> users = new ArrayList<>();
        users.add(user2);

        //empty password string
        when(userService.findAll()).thenReturn(users);

        when(roleService.findAll()).thenReturn(roles);
        mockMvc.perform(post("/register").flashAttr("user", user))
                .andExpect(status().is(200))
                .andExpect(model().attribute("UserInUse", notNullValue()))
                .andExpect(view().name("register-manage"))
                .andDo(print());

    }

    @Test
    //Add user with non valid name
    public void AddWrongPWTest() throws Exception {
        Role r = new Role("test");
        r.setId((long) 15);
        List<Role> roles = new ArrayList<Role>();
        roles.add(r);

        Set<Role> rolSet = new HashSet<>();
        rolSet.add(r);

        User user = new User("admin", "Ru1en","ruben.joosen@gmail.com","Ruben","Joosen","20164473","","",rolSet,null,null);
        User user2 = new User("admin", "Ruben1","ruben.josen@gmail.com","Ruben","Joosen","2014473","","",rolSet,null,null);

        List<User> users = new ArrayList<>();
        users.add(user2);
        //empty password string
        when(userService.findAll()).thenReturn(users);

        when(roleService.findAll()).thenReturn(roles);
        mockMvc.perform(post("/register").flashAttr("user", user))
                .andExpect(status().is(200))
                .andExpect(model().attribute("UserInUse", notNullValue()))
                .andExpect(view().name("register-manage"))
                .andDo(print());

        user.setPassword("rubennn1");
        mockMvc.perform(post("/register").flashAttr("user", user))
                .andExpect(status().is(200))
                .andExpect(model().attribute("UserInUse", notNullValue()))
                .andExpect(view().name("register-manage"));

        user.setPassword("RUBENNN1");
        mockMvc.perform(post("/register").flashAttr("user", user))
                .andExpect(status().is(200))
                .andExpect(model().attribute("UserInUse", notNullValue()))
                .andExpect(view().name("register-manage"));

        user.setPassword("Ruben1  ");
        mockMvc.perform(post("/register").flashAttr("user", user))
                .andExpect(status().is(200))
                .andExpect(model().attribute("UserInUse", notNullValue()))
                .andExpect(view().name("register-manage"));



    }

    @Test
    //Add user with non valid name
    public void AddDoubleUsernameTest() throws Exception {
        Role r = new Role("test");
        r.setId((long) 15);
        List<Role> roles = new ArrayList<Role>();
        roles.add(r);

        Set<Role> rolSet = new HashSet<>();
        rolSet.add(r);

        User user = new User("admin", "Ruben1","ruben.joosen@gmail.com","Ruben","Joosen","20164473","","",rolSet,null,null);

        List<User> users = new ArrayList<>();
        users.add(user);
        //empty password string
        //  when(userService.findAll()).thenReturn(users);

        when(roleService.findAll()).thenReturn(roles);
        when(userService.findByUsername("20164473")).thenReturn(Optional.of(user));
        mockMvc.perform(post("/register").flashAttr("user", user))
                .andExpect(status().is(200))
                .andExpect(model().attribute("UserInUse", notNullValue()))
                .andExpect(view().name("register-manage"));

    }

    @Test
    //Add user with non valid name
    public void AddWrongEmailTest() throws Exception {
        Role r = new Role("test");
        r.setId((long) 15);
        List<Role> roles = new ArrayList<Role>();
        roles.add(r);

        Set<Role> rolSet = new HashSet<>();
        rolSet.add(r);

        User user = new User("admin", "Ruben1","ruben.joosen","Ruben","Joosen","20164473","","",rolSet,null,null);

        List<User> users = new ArrayList<>();
        users.add(user);
        //empty password string
        //  when(userService.findAll()).thenReturn(users);

        when(roleService.findAll()).thenReturn(roles);
        mockMvc.perform(post("/register").flashAttr("user", user))
                .andExpect(status().is(200))
                .andExpect(model().attribute("UserInUse", notNullValue()))
                .andExpect(view().name("register-manage"));

    }
















}
