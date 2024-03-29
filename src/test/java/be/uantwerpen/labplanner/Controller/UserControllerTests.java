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
public class UserControllerTests {

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private StepService stepService;

    @Mock
    private RelationService relationService;

    @Mock
    private PasswordEncoder passwordEncoder;


    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")
    //View User list
    public void ViewUserListTest() throws Exception{
        User user = new User("admin","admin");
        List<User> users = new ArrayList<User>();
        users.add(user);

        when(userService.findAll()).thenReturn(users);
        mockMvc.perform(get("/usermanagement/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("Users/user-list"))
                .andExpect(model().attribute("allUsers", hasSize(1)));

    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")
    public void testPasswordSave()throws Exception{
        User u = new User("test","test");
        u.setId((long) 34);

        mockMvc.perform(post("/password").flashAttr("user",u))
        .andExpect(model().attribute("PWError",notNullValue()));

        u.setPassword("TESTTEST");
        mockMvc.perform(post("/password").flashAttr("user",u))
                .andExpect(model().attribute("PWError",notNullValue()));

        u.setPassword("testtest");
        mockMvc.perform(post("/password").flashAttr("user",u))
                .andExpect(model().attribute("PWError",notNullValue()));

        u.setPassword("TESTtest");
        mockMvc.perform(post("/password").flashAttr("user",u))
                .andExpect(model().attribute("PWError",notNullValue()));

        u.setPassword("TESTtest1  ");
        mockMvc.perform(post("/password").flashAttr("user",u))
                .andExpect(model().attribute("PWError",notNullValue()));

        u.setPassword("TESTtest123");
        mockMvc.perform(post("/password").flashAttr("user",u))
                .andExpect(model().attribute("PWError",nullValue()));

    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")
    public void testPasswordWrongIdSave()throws Exception{
        User u = new User("test","test");
        u.setId((long) 33);

        mockMvc.perform(post("/password").flashAttr("user",u))
                .andExpect(model().attribute("PWError",notNullValue()));




    }


    @Test
    // Show user manage page test
    public void ViewCreateUserTest() throws Exception{
        User user = new User("admin","admin");
        long id = 10;
        user.setId(id);
        Role r = new Role("testRole");
        List<Role> roles = new ArrayList<Role>();
        roles.add(r);

        when(roleService.findAll()).thenReturn(roles);

        mockMvc.perform(get("/usermanagement/users/put"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allRoles",hasSize(1)))
                .andExpect(model().attribute("user",instanceOf(User.class)))
                .andExpect(view().name("Users/user-manage"));

    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")
    public void VieuwChangePasswordTest() throws Exception{
        mockMvc.perform(get("/password"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user",instanceOf(User.class)))
                .andExpect(view().name("Users/password-manage"));
    }


    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")
    //TEst the validity of editing the user page
    public void viewEditUserTest() throws Exception {
        User user = new User("admin","admin");
        long id = 10;
        user.setId(id);
        List<User> users = new ArrayList<User>();
        users.add(user);

        Role r = new Role("test");
        r.setId((long) 15);
        List<Role> roles = new ArrayList<Role>();
        roles.add(r);

        user.setRoles(new HashSet<Role>());

        Role admin = new Role("Administrator");


        when(roleService.findByName("Administrator")).thenReturn(Optional.of(admin));
        when(userService.findById(id)).thenReturn(Optional.of(user));
        when(roleService.findAll()).thenReturn(roles);

        //editing with existing id as input
        mockMvc.perform(get("/usermanagement/users/{id}",10))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allRoles",hasSize(1)))
                .andExpect(view().name("Users/user-manage"))
                .andDo(print());

        //wrong input
        mockMvc.perform(get("/usermanagement/users/{id}","fff"))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }


    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")
    //TEst the validity of editing the user page
    public void viewEditOtherAdminTest() throws Exception {
        User user = new User("admin","admin");
        long id = 10;
        user.setId(id);
        List<User> users = new ArrayList<User>();
        users.add(user);

        Role r = new Role("test");
        r.setId((long) 15);
        List<Role> roles = new ArrayList<Role>();
        roles.add(r);
        Set<Role> rols = new HashSet<Role>();

        Role admin = new Role("Administrator");
        rols.add(admin);
        user.setRoles(rols);


        when(roleService.findByName("Administrator")).thenReturn(Optional.of(admin));
        when(userService.findById(id)).thenReturn(Optional.of(user));
        when(roleService.findAll()).thenReturn(roles);

        //editing with existing id as input
        mockMvc.perform(get("/usermanagement/users/{id}",10))
                .andExpect(status().isOk())
                .andExpect(view().name("Users/user-list"))
                .andDo(print());

    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")
    //TEst the validity of editing the user page
    public void viewEditUserNullTest() throws Exception {
        User user = new User("admin","admin");
        long id = 10;
        user.setId(id);
        List<User> users = new ArrayList<User>();
        users.add(user);

        Role r = new Role("test");
        r.setId((long) 15);
        List<Role> roles = new ArrayList<Role>();
        roles.add(r);

        user.setRoles(new HashSet<Role>());

        Role admin = new Role("Administrator");


        when(roleService.findByName("Administrator")).thenReturn(Optional.of(admin));
        when(userService.findById(id)).thenReturn(Optional.empty());
        when(roleService.findAll()).thenReturn(roles);

        //editing with existing id as input
        mockMvc.perform(get("/usermanagement/users/{id}",10))
                .andExpect(status().isOk())
                .andExpect(view().name("Users/user-list"))
                .andDo(print());
    }


    @Test
    //Add user with non valid name
    public void AddNonValidNameUserTest() throws Exception{
        User user = new User("admin","");
        long id = 10;
        user.setId(id);

        Role r = new Role("test");
        r.setId((long) 15);
        List<Role> roles = new ArrayList<Role>();
        roles.add(r);

        //empty password string
        when(roleService.findAll()).thenReturn(roles);
        mockMvc.perform(post("/usermanagement/users/").flashAttr("user",user))
                .andExpect(status().is(200))
                .andExpect(model().attribute("UserInUse",notNullValue()))
                .andExpect(view().name("Users/user-manage"))
                .andDo(print());

        //empty username string
        user.setUsername("");
        user.setPassword("admin");
        mockMvc.perform(post("/usermanagement/users/").flashAttr("user",user))
                .andExpect(status().is(200))
                .andExpect(model().attribute("UserInUse",notNullValue()))
                .andExpect(view().name("Users/user-manage"))
                .andDo(print());

        //username = null
        user.setUsername(null);
        user.setPassword("admin");
        mockMvc.perform(post("/usermanagement/users/").flashAttr("user",user))
                .andExpect(status().is(200))
                .andExpect(model().attribute("UserInUse",notNullValue()))
                .andExpect(view().name("Users/user-manage"))
                .andDo(print());

        //PW is null
        user.setUsername("admin");
        user.setPassword(null);
        mockMvc.perform(post("/usermanagement/users/").flashAttr("user",user))
                .andExpect(status().is(200))
                .andExpect(model().attribute("UserInUse",notNullValue()))
                .andExpect(view().name("Users/user-manage"))
                .andDo(print());
    }

    @Test
    //add new user with unique name
    public void addNewCorrectUserTest() throws Exception{
        User user = new User("admin","admin");
        user.setEmail("test@test.com");

        user.setUaNumber("2");

        User user2 = new User("Ua","Ua");
        user2.setUaNumber("1");
        user2.setId((long) 789);
        List<User> users = new ArrayList<>();
        users.add(user2);

        when(userService.findAll()).thenReturn(users);
        when(userService.findByUsername("admin")).thenReturn(Optional.empty());
        mockMvc.perform(post("/usermanagement/users/").flashAttr("user",user))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/usermanagement/users"))
                .andDo(print());

    }

    @Test
    //add new user with unique name
    public void addNewUserWrongMailTest() throws Exception{
        User user = new User("admin","admin");
        user.setEmail("test@");

        user.setUaNumber("2");

        User user2 = new User("Ua","Ua");
        user2.setUaNumber("1");
        user2.setId((long) 789);
        List<User> users = new ArrayList<>();
        users.add(user2);

        when(userService.findAll()).thenReturn(users);
        when(userService.findByUsername("admin")).thenReturn(Optional.empty());
        mockMvc.perform(post("/usermanagement/users/").flashAttr("user",user))
                .andExpect(status().is(200))
                .andExpect(model().attribute("UserInUse",notNullValue()))
                .andDo(print());

    }

    @Test
    //add new user with unique name
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")

    public void addUserDoubleUANumber() throws Exception{
        User user = new User("admin","admin");
        user.setUaNumber("1");
        user.setEmail("test@test.com");
        User user2 = new User("Ua","Ua");
        user2.setUaNumber("1");
        user2.setId((long) 789);
        List<User> users = new ArrayList<>();
        users.add(user2);

        when(userService.findAll()).thenReturn(users);
        when(userService.findByUsername("admin")).thenReturn(Optional.empty());
        mockMvc.perform(post("/usermanagement/users/").flashAttr("user",user))
                .andExpect(status().is(200))
                .andExpect(model().attribute("UserInUse",notNullValue()))
                .andDo(print());

    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")
//add new user with unique name
    public void addNewCorrectUserWrongPWTest() throws Exception{
        User user = new User("admin"," admin");
        user.setEmail("test@test.com");
        when(userService.findByUsername("admin")).thenReturn(Optional.empty());
        mockMvc.perform(post("/usermanagement/users/").flashAttr("user",user))
                .andExpect(status().is(200))
                .andExpect(model().attribute("UserInUse",notNullValue()))
                .andDo(print());

    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")

    //add new user with unique name
    public void addNewFalseUserTest() throws Exception{
        User user = new User("admin","admin");
        user.setEmail("test@test.com");
        when(userService.findByUsername("admin")).thenReturn(Optional.of(user));
        mockMvc.perform(post("/usermanagement/users/").flashAttr("user",user))
                .andExpect(status().is(200))
                .andExpect(view().name("Users/user-manage"))
                .andExpect(model().attribute("UserInUse",notNullValue()))
                .andDo(print());

    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")
//add new user with unique name
    public void addNewFalsePasswOrdUserTest() throws Exception{
        User user = new User("admin","admin ");
        user.setEmail("test@test.com");
        when(userService.findByUsername("admin")).thenReturn(Optional.of(user));
        mockMvc.perform(post("/usermanagement/users/").flashAttr("user",user))
                .andExpect(status().is(200))
                .andExpect(view().name("Users/user-manage"))
                .andExpect(model().attribute("UserInUse",notNullValue()))
                .andDo(print());

    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")
// edit existing user without changing name.
    public void EditCorrectUserTest() throws Exception{
        User user = new User("admin","admin");
        long id = 10;
        user.setEmail("test@test.com");
        when(userService.findById(id)).thenReturn(Optional.of(user));
        mockMvc.perform(post("/usermanagement/users/{id}","10").flashAttr("user",user))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/usermanagement/users"))
                .andDo(print());

    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")
// edit existing user without changing name.
    public void EditCorrectUserWrongPWTest() throws Exception{
        User user = new User("admin","admin ");
        long id = 10;
        user.setEmail("test@test.com");
        when(userService.findById(id)).thenReturn(Optional.of(user));
        mockMvc.perform(post("/usermanagement/users/{id}","10").flashAttr("user",user))
                .andExpect(status().is(200))
                .andExpect(model().attribute("UserInUse",notNullValue()))
                .andDo(print());

    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")
// Edit existing user with non unique name
    public void EditUserNonUniqueNameTest() throws Exception{
        User user = new User("admin","admin");
        long id = 10;
        user.setId(id);
        user.setEmail("test@test.com");
        User user2 = new User("admin2","admin2");

        when(userService.findById(id)).thenReturn(Optional.of(user2));
        when(userService.findByUsername("admin")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/usermanagement/users/{id}","10").flashAttr("user",user))
                .andExpect(status().is(200))
                .andExpect(model().attribute("UserInUse",notNullValue()))
                .andExpect(view().name("Users/user-manage"))
                .andDo(print());

    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")
// Edit existing user with unique name
    public void EditUserUniqueNameTest() throws Exception{
        User user = new User("admin","admin");
        long id = 10;
        user.setId(id);
        user.setEmail("test@test.com");
        User user2 = new User("admin2","admin2");

        when(userService.findById(id)).thenReturn(Optional.of(user2));
        when(userService.findByUsername("admin")).thenReturn(Optional.empty());

        mockMvc.perform(post("/usermanagement/users/{id}","10").flashAttr("user",user))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/usermanagement/users"))
                .andDo(print());

    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")
    // Edit existing user with unique name
    public void EditUserUniqueNameWrongPasswordTest() throws Exception{
        User user = new User("admin","admin ");
        long id = 10;
        user.setId(id);
        user.setEmail("test@test.com");
        User user2 = new User("admin2","admin2");

        when(userService.findById(id)).thenReturn(Optional.of(user2));
        when(userService.findByUsername("admin")).thenReturn(Optional.empty());

        mockMvc.perform(post("/usermanagement/users/{id}","10").flashAttr("user",user))
                .andExpect(status().is(200))
                .andExpect(model().attribute("UserInUse",notNullValue()))
                .andDo(print());

    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")

    //test for deleting
    public void DeleteUserRelationTest() throws Exception {
        User user = new User("admin", "admin");
        long id = 10;
        user.setId(id);

        User user2 = new User("student", "student");
        long id2 = 11;
        user2.setId(id2);
        Set<User> students = new HashSet<>();
        students.add(user2);

        Relation relation = new Relation();
        relation.setResearcher(user);
        relation.setStudents(students);

        List<Relation> relations = new ArrayList<>();
        relations.add(relation);


        Step step = new Step();
        step.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);


        //User still in a relations
        when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/usermanagement/users/{id}/delete", "10"))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(model().attribute("inUseError", notNullValue()))
                .andExpect(view().name("Users/user-list"));

    }

        @Test
        @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")

        //test for deleting
    public void DeleteUserTest() throws Exception{
        User user = new User("admin","admin");
        long id = 10;
        user.setId(id);

        User user2 = new User("student","student");
        long id2 = 11;
        user2.setId(id2);
        user2.setRoles(new HashSet<>());
        Set<User> students = new HashSet<>();
        students.add(user2);

            User user3 = new User("student","student");
            long id3 = 12;
            user3.setId(id3);
            user3.setRoles(new HashSet<>());

        Relation relation = new Relation();
        relation.setResearcher(user2);
        relation.setStudents(students);

        List<Relation> relations = new ArrayList<>();
        relations.add(relation);

        user.setRoles(new HashSet<>());

        Role admin = new Role("Administrator");
        when(roleService.findByName("Administrator")).thenReturn(Optional.of(admin));

        Step step = new Step();
        step.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);

        //admin deletes himself tesµ

         when(userService.findById(id)).thenReturn(Optional.of(user));
         when(userService.findById(id2)).thenReturn(Optional.of(user2));
            when(userService.findById(id3)).thenReturn(Optional.of(user3));

            when(relationService.findAll()).thenReturn(relations);

            when(stepService.findAll()).thenReturn(steps);
         mockMvc.perform(get("/usermanagement/users/{id}/delete","34"))
                    .andExpect(status().is(200))
                    .andDo(print());




        //User is in Use
        mockMvc.perform(get("/usermanagement/users/{id}/delete","10"))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(model().attribute("inUseError",notNullValue()))

                .andExpect(view().name("Users/user-list"));

        //User is in relation
        mockMvc.perform(get("/usermanagement/users/{id}/delete",id2))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(model().attributeDoesNotExist())
                .andExpect(view().name("Users/user-list"));

        //User is in relation
            mockMvc.perform(get("/usermanagement/users/{id}/delete",id3))
                    .andExpect(status().is(302))
                    .andDo(print())
                    .andExpect(model().attributeDoesNotExist())
                    .andExpect(view().name("redirect:/usermanagement/users"));

        //wrong url input
        mockMvc.perform(get("/usermanagement/users/{id}/delete","ff"))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value="ruben.joosen@student.uantwerpen.be",userDetailsServiceBeanName="newSecurityService")

    //test for deleting
    public void DeleteAdminTest() throws Exception {
        User user = new User("admin", "admin");
        long id = 10;
        user.setId(id);

        User user2 = new User("student", "student");
        long id2 = 11;
        user2.setId(id2);
        Set<User> students = new HashSet<>();
        students.add(user2);

        Relation relation = new Relation();
        relation.setResearcher(user2);
        relation.setStudents(students);

        List<Relation> relations = new ArrayList<>();
        relations.add(relation);

        Role admin = new Role("Administrator");
        Set<Role> rols = new HashSet<>();
        rols.add(admin);
        user.setRoles(rols);


        Step step = new Step();
        step.setUser(user);
        List<Step> steps = new ArrayList<>();
        steps.add(step);

        //admin deletes himself tesµ
        when(roleService.findByName("Administrator")).thenReturn(Optional.of(admin));
        when(userService.findById(id)).thenReturn(Optional.of(user));
        when(stepService.findAll()).thenReturn(steps);
        mockMvc.perform(get("/usermanagement/users/{id}/delete", id))
                .andExpect(status().is(200))
                .andDo(print());


    }


    }
