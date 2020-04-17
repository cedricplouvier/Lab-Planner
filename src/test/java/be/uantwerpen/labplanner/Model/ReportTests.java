package be.uantwerpen.labplanner.Model;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.common.model.users.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class ReportTests {

    private Report report;


    @Test
    public void TestReport() throws  Exception{

        report = new Report();

        assertEquals(report.getTitle(), "");
        assertEquals(report.getDescription(), "");
        assertNull(report.getCreator());
        assertNull(report.getId());

        String title = "title";
        report.setTitle(title);
        assertEquals(report.getTitle(), title);

        String description = "description";
        report.setDescription(description);
        assertEquals(report.getDescription(), description);

        Long id = (long ) 5;
        report.setId(id);
        assertEquals(report.getId(), id);


        User user = new User();
        report.setCreator(user);
        assertEquals(report.getCreator(), user);






    }
}
