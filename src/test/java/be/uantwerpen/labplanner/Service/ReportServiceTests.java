package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Mixture;
import be.uantwerpen.labplanner.Model.Report;
import com.sun.org.apache.regexp.internal.RE;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class ReportServiceTests {

    @Autowired
    ReportService reportService;

    @Test
    public void testFindAll(){
        Report report = new Report();
        reportService.save(report);
        List<Report> list = reportService.findAll();
        assertNotNull(list);
    }

    @Test
    public void testFindById(){
        Report report = new Report();
        reportService.save(report);
        Report fetched = reportService.findById(report.getId()).orElse(null);
        assertNotNull(fetched);
    }

    @Test
    public void testDelete(){
        Report report = new Report();
        reportService.save(report);
        List<Report> fetchedList  = reportService.findAll();
        reportService.delete(report);
        List<Report> fetchedList2 = reportService.findAll();
        assertEquals(fetchedList.size()-1, fetchedList2.size());

    }

    @Test
    public void testDeleteById(){
        Report report = new Report();
        reportService.save(report );
        List<Report> fetchedList  = reportService.findAll();
        reportService.deleteById(report.getId());
        List<Report> fetchedList2  = reportService.findAll();
        assertEquals(fetchedList.size()-1, fetchedList2.size());
    }

    @Test
    public void testDeleteByNonexistingId(){
        List<Report> fetchedList  = reportService.findAll();
        Long id = 5974631L;
        reportService.deleteById(id);
        List<Report> fetchedList2  = reportService.findAll();
        assertEquals(fetchedList.size(), fetchedList2.size());
    }
}
