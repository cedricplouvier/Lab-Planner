package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Service.StepService;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; //belangrijke imports
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class HomeControllerTests {

    @Mock
    private StepService stepService;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private HomeController mockHomeController;
    private MockMvc mockMvc;

    @Autowired
    private HomeController wiredHomeController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(this.mockHomeController).build();
    }

    @Test
    public void showUsermanagementPageTest() throws Exception {

        mockMvc.perform(get("/usermanagement"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/usermanagement/users"))
                .andDo(print());
    }

    @Test
    public void showStockmanagementPageTest() throws Exception{

        mockMvc.perform(get("/stockmanagement"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/products"));
    }

    @Test
    public void showPlanningtoolPageTest() throws Exception {
        mockMvc.perform(get("/planningtool"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/calendar/weekly"));
    }

    @Test
    public void showDevicemanagementPageTest() throws Exception{
        mockMvc.perform(get("/devicemanagement"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/devices"))
                .andDo(print());
    }

    @Test
    @WithUserDetails("Cedric")
    public void showStepsHomePageTest() throws Exception{

        User testuser = new User("admin","admin");
        Step step = new Step();
        step.setUser(testuser);
        List<Step> steps = new ArrayList<>();
        steps.add(step);

        when(stepService.findAll()).thenReturn(steps);

        mockMvc.perform(get(("/"),("/home")))
                .andExpect(status().isOk())
                .andExpect(view().name("homepage"))
                .andExpect(model().attribute("currentUser","Cedric"));
    }

}

