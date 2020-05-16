package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.LabplannerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class ExperimentTests {
    private Experiment experiment;

    @Test
    public void testExperimentType(){
        experiment= new Experiment();
        assertNull(experiment.getExperimentname());
        assertNull(experiment.getStartDate());
        assertNull(experiment.getEndDate());
        assertNull(experiment.getUser());
        assertNull(experiment.getSteps());
        assertNull(experiment.getExperimentType());
        assertNull(experiment.getId());

        experiment.setExperimentname("NewExperiment");
        assertEquals(experiment.getExperimentname(),"NewExperiment");

        List<Step> stepList = new ArrayList<Step>();
        Step step = new Step();
        stepList.add(step);
        experiment.setSteps(stepList);
        assertEquals(experiment.getSteps(), stepList);

        long id = (long) 10;
        experiment.setId(id);
        assertEquals(experiment.getId(),id);

    }
}
