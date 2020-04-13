package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Service.DeviceService;
import be.uantwerpen.labplanner.Service.DeviceTypeService;
import be.uantwerpen.labplanner.Service.StepService;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; //belangrijke imports
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class StatisticsControllerTests {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;


    @Mock
    private DeviceService deviceService;

    @Mock
    private DeviceTypeService deviceTypeService;

    @Mock
    private StepService stepService;

    @InjectMocks
    private StatisticsController statisticsController;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(this.statisticsController)
                .apply(springSecurity(springSecurityFilterChain)).build();

        /*mockMvc = MockMvcBuilders
                    .webAppContextSetup(context)
                    .apply(springSecurity())
                    .build();

         */

    }

    @Test
    public void showStatisticsPageTest() throws Exception{

        List<Device> devices = new ArrayList<>();
        List<Step> steps = new ArrayList<>();

        when(deviceService.findAll()).thenReturn(devices);
        when(stepService.findAll()).thenReturn(steps);
        mockMvc.perform(get("/statistics/statistics").with(user("test").password("test")
                //Testing on the authorities is not working
                .authorities(new SimpleGrantedAuthority("Statistics Access"))))
                .andExpect(status().isOk())
                .andExpect(model().attribute("dev1", notNullValue()))
                .andExpect(model().attribute("selectedDev",instanceOf(Device.class)))
                .andExpect(model().attribute("dev2", notNullValue()))
                .andExpect(model().attribute("dev3", notNullValue()))
                .andExpect(model().attribute("dev4", notNullValue()))
                .andExpect(model().attribute("dev5", notNullValue()))
                .andExpect(view().name("/Statistics/statistics"))
                .andDo(print());
    }

    @Test
    public void submitTest() throws Exception{
        long id = 10;
        List<Step> steps = new ArrayList<>();
        int[] totalHoursSelectedDevice = new int[]{10,10,10,10,10,10,10,10,10,10,10,10};;
        int highestAbsoluteValueHours=10;
        int test=11;
        int testValue = 1;
        Device dev = new Device();
        dev.setId(id);
        DeviceType devType = new DeviceType();
        devType.setId(id);
        dev.setDevicename("testDevive");
        dev.setDeviceType(devType);
        List<Device> devices = new ArrayList<>();
        devices.add(dev);

        when(stepService.findAll()).thenReturn(steps);

        mockMvc.perform(get("/statistics/statistics/submit").flashAttr("test",test)
                .flashAttr("totalHoursSelectedDevice", totalHoursSelectedDevice)
                .flashAttr("highestAbsoluteValueHours",highestAbsoluteValueHours)
                .flashAttr("selectedDev",dev)
                .flashAttr("selectedDevices", devices)
                .flashAttr("testValue", testValue)
                .with(user("test").password("test")
                .authorities(new SimpleGrantedAuthority("Statistics Access"))))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/statistics/statistics"))
                .andDo(print());

    }

    @Test
    public void ClearListTest() throws Exception{
        mockMvc.perform(get("/statistics/statistics/clearList")
                .with(user("test").password("test")
                        .authorities(new SimpleGrantedAuthority("Statistics Access"))))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/statistics"))
                .andDo(print());
    }

    @Test
    public void getSelectedyearTest() throws Exception {
        mockMvc.perform(get("/statistics/statistics/getSelectedYear")
                .with(user("test").password("test")
                        .authorities(new SimpleGrantedAuthority("Statistics Access"))))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/statistics/statistics/refreshYear"))
                .andDo(print());
    }

    @Test
    public void getSelectedGraphTypeTest() throws Exception {
        mockMvc.perform(get("/statistics/statistics/getSelectedGraphType")
                .with(user("test").password("test")
                        .authorities(new SimpleGrantedAuthority("Statistics Access"))))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/statistics/statistics/refreshYear"))
                .andDo(print());
    }
    @Test
    public void refreshYearTest() throws Exception {
        mockMvc.perform(get("/statistics/statistics/refreshYear")
                .with(user("test").password("test")
                        .authorities(new SimpleGrantedAuthority("Statistics Access"))))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/statistics/statistics"))
                .andDo(print());
    }
}
