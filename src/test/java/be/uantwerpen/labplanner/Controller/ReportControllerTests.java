package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Relation;
import be.uantwerpen.labplanner.Model.Report;
import be.uantwerpen.labplanner.Service.ReportService;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class ReportControllerTests {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(this.reportController).build();
    }

    @Test
    //View report list
    public void viewReportTest() throws Exception{

        mockMvc.perform(get("/reports/list"))
                .andExpect(view().name("/Reports/report-list"));
    }

    @Test
    // create valid report
    public void createValidReportTest() throws Exception{
        Report report = new Report("title", "description", null);
        long id = 10;
        report.setId(id);

        mockMvc.perform(get("/reports/put"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("report",instanceOf(Report.class)))
                .andExpect(view().name("Reports/report-form"));

    }
/*
    @Test
    public void addValidReport() throws Exception{
        Report report = new Report("title", "description", null);
        long id = 10;
        report.setId(id);

        mockMvc.perform(post("/reports").flashAttr("report",report))
                .andExpect(status().is(302))
                .andExpect(model().attribute("errormessage",nullValue()))
                .andExpect(view().name("redirect:/home"))
                .andDo(print());

    }

    @Test
    //invalid title
    public void addInValidTitleReport() throws Exception{
        Report report = new Report("", "description", null);
        long id = 10;
        report.setId(id);

        mockMvc.perform(post("/reports").flashAttr("report",report))
                .andExpect(model().attribute("errormessage",notNullValue()))
                .andExpect(view().name("Reports/report-form"))
                .andDo(print());

    }

    @Test
    //invalid description
    public void addInValidDescriptionReport() throws Exception{
        Report report = new Report("titel", "", null);
        long id = 10;
        report.setId(id);

        mockMvc.perform(post("/reports").flashAttr("report",report))
                .andExpect(model().attribute("errormessage",notNullValue()))
                .andExpect(view().name("Reports/report-form"))
                .andDo(print());
    }
*/

    @Test
    // create valid report
    public void addNonValidReportTest() throws Exception{
        Report report = new Report("title", "description", null);
        long id = 10;
        report.setId(id);

        mockMvc.perform(get("/reports/put"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("report",instanceOf(Report.class)))
                .andExpect(view().name("Reports/report-form"));

    }


}
