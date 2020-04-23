package be.uantwerpen.labplanner.Model;
import be.uantwerpen.labplanner.LabplannerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class ExperimentTypeTests {
    private ExperimentType experimentType;

    @Test
    public void testExperimentType(){
        experimentType= new ExperimentType();
        assertNull(experimentType.getExpname());
        assertNull(experimentType.getStepTypes());
        assertNull(experimentType.getId());

        experimentType.setExpname("NewExperiment");
        assertEquals(experimentType.getExpname(),"NewExperiment");

        List<StepType> stepTypeList = new ArrayList<StepType>();
        StepType stepType = new StepType();
        stepTypeList.add(stepType);
        experimentType.setStepTypes(stepTypeList);
        assertEquals(experimentType.getStepTypes(),stepTypeList);

        long id = (long) 10;
        experimentType.setId(id);
        assertEquals(experimentType.getId(),id);

    }
}
