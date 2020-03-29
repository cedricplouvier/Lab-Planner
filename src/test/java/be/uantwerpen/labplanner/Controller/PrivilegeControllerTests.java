package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.Service.OwnPrivilegeService;
import be.uantwerpen.labplanner.common.model.users.Privilege;
import be.uantwerpen.labplanner.common.repository.users.PrivilegeRepository;
import be.uantwerpen.labplanner.common.service.users.PrivilegeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
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

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PrivilegeControllerTests {

    @Mock
    private OwnPrivilegeService privilegeService;

    @InjectMocks
    private PrivilegeController privilegeController;

    private MockMvc mockMvc;

    @Before
    public void setup(){

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(privilegeController).build();
    }


    //View user list test
    @Test
    public void viewPrivilegeList() throws Exception{
        Privilege p1 = new Privilege("test");
        List<Privilege> privs = new ArrayList<>();
        privs.add(p1);
        when(privilegeService.findAll()).thenReturn(privs);
        mockMvc.perform(get("/usermanagement/privileges")).andExpect(status().isOk())
                .andExpect(view().name("/Privileges/privilege-list"))
                .andExpect(model().attribute("allPrivileges", hasSize(1)));
    }

    //test for ViewCreatePrivilege
    @Test
    public void CreatePrivilegeTest() throws Exception{
        mockMvc.perform(get("/usermanagement/privileges/put")).andExpect(status().isOk())
                .andExpect(model().attribute("privilege",instanceOf(Privilege.class)))
                .andExpect(view().name("/Privileges/privilege-manage"));
    }

    //test for viewEditPrivilege with wrong path
    @Test
    public void VieuwEditPrivTest()throws Exception{

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
    public void testAddCorrectPrivilege() throws Exception{
        long id = 10;

        Privilege p1 = new Privilege("test");
        p1.setId(id);
        List<Privilege> privs = new ArrayList<>();
        privs.add(p1);

        when(privilegeService.findById(id)).thenReturn(Optional.of(p1));
        when(privilegeService.findByName("test")).thenReturn(Optional.of(p1));
        when(privilegeService.save(p1)).thenReturn(p1);

        mockMvc.perform(post("/usermanagement/privileges/{id}","10"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/usermanagement/privileges"))
        .andDo(print());
    }

    @Test
    //Test for inserting a correct new privilege.
    public void testNewPrivilege() throws Exception{
        long id = 10;

        Privilege p1 = new Privilege("test");
        p1.setId(null);
        List<Privilege> privs = new ArrayList<>();
        privs.add(p1);

        when(privilegeService.findById(id)).thenReturn(Optional.of(p1));
        when(privilegeService.findByName("test")).thenReturn(Optional.of(p1));
        when(privilegeService.save(p1)).thenReturn(p1);

        mockMvc.perform(post("/usermanagement/privileges/","10"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/usermanagement/privileges"))
                .andDo(print());
    }



    @Test
    public void deleteTest() throws Exception{
        long id = 10;
        when(privilegeService.deleteById(id)).thenReturn(null);
        mockMvc.perform(get("/usermanagement/privileges/{id}/delete","10"))
                .andDo(print())
                .andExpect(view().name("/Privileges/privilege-list"));

    }









}
