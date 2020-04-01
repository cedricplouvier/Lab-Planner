package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Service.OwnPrivilegeService;
import be.uantwerpen.labplanner.common.model.users.Privilege;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;  //belangrijke imports
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class PrivilegeControllerTests {

    @Mock
    private RoleService roleService;

    @Mock
    private OwnPrivilegeService privilegeService;

    @InjectMocks
    private PrivilegeController privilegeController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(privilegeController).build();
    }


    //View user list test
    @Test
    public void viewPrivilegeList() throws Exception{
        Privilege p1 = new Privilege("test");
        List<Privilege> privileges = new ArrayList<>();
        privileges.add(p1);
        when(privilegeService.findAll()).thenReturn(privileges);
        mockMvc.perform(get("/usermanagement/privileges"))
                .andExpect(status().isOk())
                .andExpect(view().name("/Privileges/privilege-list"))
                .andExpect(model().attribute("allPrivileges", hasSize(1)));
    }

    //test for ViewCreatePrivilege
    @Test
    public void CreatePrivilegeTest() throws Exception{
        mockMvc.perform(get("/usermanagement/privileges/put"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("privilege",instanceOf(Privilege.class)))
                .andExpect(view().name("/Privileges/privilege-manage"));
    }

    //test for viewEditPrivilege with wrong path
    @Test
    public void VieuwEditPrivilegeTest()throws Exception{
        Privilege privilege = new Privilege("testPrivilege");
        long id = 10;
        privilege.setId(id);

        when(privilegeService.findById(id)).thenReturn(Optional.of(privilege));

        mockMvc.perform(get("/usermanagement/privileges/{id}","fff"))
                .andExpect(status().is4xxClientError())
                .andDo(print());

        //test with correct path
        mockMvc.perform(get("/usermanagement/privileges/{id}","10"))
                .andExpect(status().is(200))
                .andExpect(view().name("/Privileges/privilege-manage"))
                .andDo(print());
    }



    @Test
    // Test add a privilege with no usefull name!
    //should return Privilege-manage
    public void AddNonValidNamePrivilegeTest() throws Exception{
        long id = 10;

        Privilege p1 = new Privilege(null);
        p1.setId(id);

        //name of privilege is null
        mockMvc.perform(post("/usermanagement/privileges/","70")
                .flashAttr("privilege",p1))
                .andExpect(status().is(200))
                .andExpect(model().attribute("PrivilegeInUse",notNullValue()))
                .andExpect(view().name("/Privileges/privilege-manage"))
                .andDo(print());

        //privilege name is empty string
        p1.setName("  ");
        mockMvc.perform(post("/usermanagement/privileges/","10")
                .flashAttr("privilege",p1))
                .andExpect(status().is(200))
                .andExpect(model().attribute("PrivilegeInUse",notNullValue()))
                .andExpect(view().name("/Privileges/privilege-manage"))
                .andDo(print());

    }


    @Test
    //add new priv with unique name.
    public void addNewCorrectPrivilegeTest() throws Exception{

        Privilege p2 = new Privilege("test");

        when(privilegeService.findByName("test")).thenReturn(Optional.empty());

        mockMvc.perform(post("/usermanagement/privileges/").flashAttr("privilege",p2))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/usermanagement/privileges"))
                .andDo(print());
    }

    @Test
    //add new pri with already existing name
    public void addNewFalsePrivilegeTest() throws Exception{
        long id = 10;

        Privilege p2 = new Privilege("test");

        when(privilegeService.findByName("test")).thenReturn(Optional.of(p2));
        mockMvc.perform(post("/usermanagement/privileges/").flashAttr("privilege",p2))
                .andExpect(status().is(200))
                .andExpect(view().name("/Privileges/privilege-manage"))
                .andExpect(model().attribute("PrivilegeInUse",notNullValue()))
                .andDo(print());
    }

    @Test
    //edit existing priv with same name & existing id.
    public void EditCorrectPrivilegeTest() throws Exception{
        long id = 10;
        Privilege p1 = new Privilege("test");
        p1.setId(id);

        when(privilegeService.findById(id)).thenReturn(Optional.of(p1));
        mockMvc.perform(post("/usermanagement/privileges/{id}","10").flashAttr("privilege",p1))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/usermanagement/privileges"))
                .andDo(print());
    }


    @Test
    //edit existing priv with  non unique name & existing id.
    public void EditPrivilegeNonUniqueNameTest() throws Exception{
        long id = 10;
        Privilege p1 = new Privilege("test");
        p1.setId(id);
        Privilege p2 = new Privilege("test2");
        when(privilegeService.findById(id)).thenReturn(Optional.of(p2));
        when(privilegeService.findByName("test")).thenReturn(Optional.of(p1));

        mockMvc.perform(post("/usermanagement/privileges/{id}","10").flashAttr("privilege",p1))
                .andExpect(status().is(200))
                .andExpect(model().attribute("PrivilegeInUse",notNullValue()))
                .andExpect(view().name("/Privileges/privilege-manage"))
                .andDo(print());
    }

    @Test
    //edit existing priv with new non unique name & existing id.
    public void EditPrivilegeUniqueNameTest() throws Exception{
        long id = 10;
        Privilege p1 = new Privilege("test");
        p1.setId(id);
        Privilege p2 = new Privilege("test2");

        when(privilegeService.findById(id)).thenReturn(Optional.of(p2));
        when(privilegeService.findByName("test")).thenReturn(Optional.empty());

        mockMvc.perform(post("/usermanagement/privileges/{id}","10").flashAttr("privilege",p1))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/usermanagement/privileges"))
                .andDo(print());
    }

    @Test
    public void deleteTest() throws Exception{
        long id = 10;
        Role role = new Role("rol");
        Privilege p1 = new Privilege("test");
        p1.setId(id);

        List<Privilege> privileges = new ArrayList<>();
        privileges.add(p1);

        role.setPrivileges(privileges);
        List<Role> roles = new ArrayList<>();
        roles.add(role);


        //Privilege is in Use
        when(roleService.findAll()).thenReturn(roles);
        mockMvc.perform(get("/usermanagement/privileges/{id}/delete","10"))
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(model().attribute("inUseError",notNullValue()))

                .andExpect(view().name("Privileges/privilege-list"));

        //Privilege is not in Use
        when(roleService.findAll()).thenReturn(roles);
        mockMvc.perform(get("/usermanagement/privileges/{id}/delete","11"))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(model().attributeDoesNotExist())
                .andExpect(view().name("redirect:/usermanagement/privileges"));


        mockMvc.perform(get("/usermanagement/privileges/{id}/delete","ff"))
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }









}
