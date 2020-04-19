package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Experiment;
import be.uantwerpen.labplanner.Model.Relation;
import be.uantwerpen.labplanner.Model.Report;
import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Service.ExperimentService;
import be.uantwerpen.labplanner.Service.RelationService;
import be.uantwerpen.labplanner.Service.ReportService;
import be.uantwerpen.labplanner.Service.StepService;
import be.uantwerpen.labplanner.common.model.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class HomeControllerTests {

    @Mock
    private StepService stepService;

    @Mock
    private ExperimentService experimentService;

    @Mock
    private RelationService relationService;

    @Mock
    private ReportService reportService;

    @InjectMocks
    private HomeController mockHomeController;
    private MockMvc mockMvc;

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
    public void showStockmanagementPageTest() throws Exception {

        mockMvc.perform(get("/stockmanagement"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/products"));
    }

    @Test
    public void showCalendarPageTest() throws Exception {
        mockMvc.perform(get("/calendar"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/calendar/weekly"));
    }

    @Test
    public void showPlanningtoolPageTest() throws Exception {
        mockMvc.perform(get("/planningtool"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/planning/"));
    }

    @Test
    public void showDevicemanagementPageTest() throws Exception {
        mockMvc.perform(get("/devicemanagement"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/devices"))
                .andDo(print());
    }

    @Test
    @WithUserDetails("Cedric")
    public void showStepsHomePageTest() throws Exception {

        long ID = 33;
        long ID2 = 66;

        User testuser = new User("Cedric", "PW");
        User user2 = new User();
        testuser.setId(ID);
        Step step = new Step();
        Step step2 = new Step();
        step.setUser(testuser);
        step.setId(ID);
        step2.setUser(user2);
        step2.setId(ID2);
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step2);

        Experiment exp = new Experiment();
        exp.setSteps(steps);
        exp.setUser(testuser);
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(exp);

        testuser.setId(ID);

        Set<User> students = new HashSet<>();
        Relation relation = new Relation();
        relation.setResearcher(testuser);
        relation.setStudents(students);
        List<Relation> relations = new ArrayList<>();
        relations.add(relation);

        List<Report> reports = new ArrayList<>();
        students.add(user2);

        when(stepService.findAll()).thenReturn(steps);
        when(experimentService.findAll()).thenReturn(experiments);
        when(relationService.findAll()).thenReturn(relations);
        when(reportService.findAll()).thenReturn(reports);

        mockMvc.perform(get(("/"), ("/home")))
                .andExpect(status().isOk())
                .andExpect(view().name("homepage"))
                .andExpect(model().attribute("currentUser", "Cedric"))
                .andExpect(model().attribute("userSteps", hasSize(1)))
                .andExpect(model().attribute("studentSteps",hasSize(1)))
                .andExpect(model().attribute("userExperiments", hasSize(1)))
                .andExpect(model().attribute("studentExperiments",hasSize(0)));
    }

}

