package be.uantwerpen.labplanner;

import be.uantwerpen.labplanner.Controller.PrivilegeController;
import be.uantwerpen.labplanner.Service.OwnPrivilegeService;
import be.uantwerpen.labplanner.common.model.users.Privilege;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class LabplannerApplicationTests {
    @Test
    public void contextLoads() {
    }

    @Test
    public void extraCheck(){
        int id = 0;
        assertEquals(id,0);
    }

    @Mock
    private RoleService roleService;

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
        List<Privilege> privileges = new ArrayList<>();
        privileges.add(p1);
        when(privilegeService.findAll()).thenReturn(privileges);
        mockMvc.perform(get("/usermanagement/privileges"))
                .andExpect(status().isOk())
                .andExpect(view().name("/Privileges/privilege-list"))
                .andExpect(model().attribute("allPrivileges", hasSize(1)));
    }


}