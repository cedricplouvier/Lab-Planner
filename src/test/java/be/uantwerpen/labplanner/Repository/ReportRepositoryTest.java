package be.uantwerpen.labplanner.Repository;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Report;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class ReportRepositoryTest {

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
        fetchedReport.setDescription("description2");
        reportRepository.save(fetchedReport);

        Report fetchedUpdated = reportRepository.findById(fetchedReport.getId()).orElse(null);
        assertEquals(fetchedUpdated.getDescription(), fetchedReport.getDescription());
        assertEquals(fetchedUpdated.getTitle(), fetchedReport.getTitle());

        assertEquals(reportRepository.count(),precount+1);

        int count = 0;
        for (Report rep : reportRepository.findAll()){
            count++;
        }

        assertEquals(count, precount+1);

        //delete report
        reportRepository.deleteById(fetchedUpdated.getId());

        //check for delete if not exists
        assertThrows(EmptyResultDataAccessException.class,()->{reportRepository.deleteById(fetchedUpdated.getId());});
//
//
        assertEquals(reportRepository.count(),precount);
        assertNull(reportRepository.findById(fetchedUpdated.getId()).orElse(null));


//



    }
}
