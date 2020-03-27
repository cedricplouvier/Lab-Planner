package be.uantwerpen.labplanner.Controller;


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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;   //belangrijke imports
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;  //belangrijke imports
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PrivilegeControllerTests {

    @Mock
    private PrivilegeRepository privilegeRepository;

    @InjectMocks
    private PrivilegeService privilegeService;

    @InjectMocks
    private PrivilegeController privilegeController;
    private MockMvc mockMvc;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(privilegeController).build();
    }

    @Test
    public void viewPrivilegeList() throws Exception{
        Privilege p1 = new Privilege("test");
        List<Privilege> privs = new ArrayList<>();
        privs.add(p1);
        when(privilegeController.populatePrivileges()).thenReturn(privs);
        when(privilegeService.findAll()).thenReturn(privs);
        mockMvc.perform(get("/usermanagement/privileges")).andDo(());
    }




}
