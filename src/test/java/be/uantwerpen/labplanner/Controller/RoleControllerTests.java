package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Service.OwnPrivilegeService;
import be.uantwerpen.labplanner.common.model.users.Privilege;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import be.uantwerpen.labplanner.common.service.users.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; //belangrijke imports
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class RoleControllerTests {

    @Mock
    private RoleService roleService;

    @Mock
    private OwnPrivilegeService privilegeService;

    @Mock
    private UserService userService;

    @InjectMocks
    private RoleController roleController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(this.roleController).build();
    }

    @Test
    //View Role list
    public void ViewRoleListTest() throws Exception{
        Role role = new Role("testrol");
        List<Role> roles = new ArrayList<Role>();
        roles.add(role);

        when(roleService.findAll()).thenReturn(roles);
        mockMvc.perform(get("/usermanagement/roles"))
                .andExpect(status().isOk())
                .andExpect(view().name("/Roles/role-list"))
                .andExpect(model().attribute("allRoles", hasSize(1)));

    }

    @Test
    // Show role manage page test
    public void ViewCreateRoleTest() throws Exception{
        Role role = new Role("testrol");
        List<Role> roles = new ArrayList<Role>();
        roles.add(role);
        Privilege p = new Privilege("test");
        List<Privilege> privileges = new ArrayList<>();
        privileges.add(p);

        when(privilegeService.findAll()).thenReturn(privileges);

        mockMvc.perform(get("/usermanagement/roles/put"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allPrivileges",hasSize(1)))
                .andExpect(model().attribute("role",instanceOf(Role.class)))
                .andExpect(view().name("/Roles/role-manage"));

    }

   @Test
    //TEst the validity of editing the role page
   public void viewEditRoleTest() throws Exception {
       Role role = new Role("testrol");
       long id = 10;
       role.setId(id);
       List<Role> roles = new ArrayList<Role>();
       roles.add(role);
       Privilege p = new Privilege("test");
       p.setId((long) 15);
       List<Privilege> privileges = new ArrayList<Privilege>();
       privileges.add(p);

       when(roleService.findById(id)).thenReturn(Optional.of(role));
       when(privilegeService.findAll()).thenReturn(privileges);

       //editing with existing id as input
       mockMvc.perform(get("/usermanagement/roles/{id}",10))
               .andExpect(status().isOk())
               .andExpect(model().attribute("allPrivileges",hasSize(1)))
               .andExpect(view().name("/Roles/role-manage"))
                .andDo(print());

        //wrong input
       mockMvc.perform(get("/usermanagement/roles/{id}","fff"))
               .andExpect(status().is4xxClientError())
               .andDo(print());

   }

   @Test
    //Ad role with non valid name
    public void AddNonValidNameRoleTest() throws Exception{
       Role role = new Role("  ");
       long id = 10;
       role.setId(id);

       Privilege p = new Privilege("test");
       p.setId((long) 15);
       List<Privilege> privileges = new ArrayList<Privilege>();
       privileges.add(p);

       when(privilegeService.findAll()).thenReturn(privileges);

       //empty string name
       mockMvc.perform(post("/usermanagement/roles/").flashAttr("role",role))
               .andExpect(status().is(200))
               .andExpect(model().attribute("roleInUse",notNullValue()))
               .andExpect(view().name("/Roles/role-manage"))
               .andDo(print());

       //null as name
       role.setName(null);
       mockMvc.perform(post("/usermanagement/roles/").flashAttr("role",role))
               .andExpect(status().is(200))
               .andExpect(model().attribute("roleInUse",notNullValue()))
               .andExpect(view().name("/Roles/role-manage"))
               .andDo(print());
   }

   @Test
    //add new role with unique name
    public void addNewCorrectRoleTest() throws Exception{
       Role role = new Role("testrole");

       when(roleService.findByName("testrole")).thenReturn(Optional.empty());
       mockMvc.perform(post("/usermanagement/roles/").flashAttr("role",role))
               .andExpect(status().is(302))
               .andExpect(view().name("redirect:/usermanagement/roles"))
               .andDo(print());

   }

    @Test
    //add new role with unique name
    public void addNewFalseRoleTest() throws Exception{
        Role role = new Role("testrole");

        when(roleService.findByName("testrole")).thenReturn(Optional.of(role));
        mockMvc.perform(post("/usermanagement/roles/").flashAttr("role",role))
                .andExpect(status().is(200))
                .andExpect(view().name("/Roles/role-manage"))
                .andExpect(model().attribute("roleInUse",notNullValue()))
                .andDo(print());

    }

    @Test
    // edit existing role with same name & existing id.
    public void EditCorrectRoleTest() throws Exception{
        Role role = new Role("testrole");
        long id = 10;

        when(roleService.findById(id)).thenReturn(Optional.of(role));
        mockMvc.perform(post("/usermanagement/roles/{id}","10").flashAttr("role",role))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/usermanagement/roles"))
                .andDo(print());

    }

    @Test
    // Edit existing role with non unique name
    public void EditRoleNonUniqueNameTest() throws Exception{
        Role role = new Role("testrole");
        long id = 10;
        role.setId((long) 10);

        Role role2 = new Role(("testrole2"));

        when(roleService.findById(id)).thenReturn(Optional.of(role2));
        when(roleService.findByName("testrole")).thenReturn(Optional.of(role));

        mockMvc.perform(post("/usermanagement/roles/{id}","10").flashAttr("role",role))
                .andExpect(status().is(200))
                .andExpect(model().attribute("roleInUse",notNullValue()))
                .andExpect(view().name("/Roles/role-manage"))
                .andDo(print());

    }

    @Test
    // Edit existing role with unique name
    public void EditRoleUniqueNameTest() throws Exception{
        Role role = new Role("testrole");
        long id = 10;
        role.setId(id);

        Role role2 = new Role(("testrole2"));

        when(roleService.findById(id)).thenReturn(Optional.of(role2));
        when(roleService.findByName("testrole")).thenReturn(Optional.empty());

        mockMvc.perform(post("/usermanagement/roles/{id}","10").flashAttr("role",role))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/usermanagement/roles"))
                .andDo(print());

    }

    @Test
    //test for deleting
    public void DeleteRoleTest() throws Exception{
        Role role = new Role("testrole");
        long id = 10;
        role.setId(id);

        Set<Role> roles = new HashSet<Role>();
        roles.add(role);

        User user = new User("admin","admin");
        user.setRoles(roles);
        List<User> users = new ArrayList<>();
        users.add(user);

        //Role is in Use
        when(userService.findAll()).thenReturn(users);
        mockMvc.perform(get("/usermanagement/roles/{id}/delete","10"))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(model().attribute("inUseError",notNullValue()))

                .andExpect(view().name("/Roles/role-list"));

        //Role is not in Use
        when(userService.findAll()).thenReturn(users);
        mockMvc.perform(get("/usermanagement/roles/{id}/delete","11"))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(model().attributeDoesNotExist())
                .andExpect(view().name("redirect:/usermanagement/roles"));

        //wrong url input
        mockMvc.perform(get("/usermanagement/roles/{id}/delete","ff"))
                .andExpect(status().is4xxClientError())
                .andDo(print());




    }



}
