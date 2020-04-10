package be.uantwerpen.labplanner.Repository;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Report;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class ReportRepository {

    @Autowired
    private ReportRepository reportRepository;

    @Test
    public void testReportSave(){

        Report report = new Report("test","descriptio", null);
        long precount = reportRepository.count();

        assertNull(report.getId());
        reportRepository.save(report);
        assertNotNull(report.getId());

        Report fetchedReport = reportRepository.findById(report.getId()).orElse(null);
        assertNotNull(fetchedReport);

        //and the fetched relation should equal the real relation
        assertEquals(fetchedReport.getId(),report.getId());
        assertEquals(fetchedReport.getDescription(),report.getDescription());

        //update title and id
        fetchedReport.setTitle("title2");
//



    }
}
