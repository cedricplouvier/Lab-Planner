package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.LabplannerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class StepTypeTests {
    private StepType stepType;

    @Test
    public void TestStepType() throws Exception{
        stepType = new StepType();
        assertNull(stepType.getDeviceType());
        assertNull(stepType.getContinuity());
        assertNull(stepType.getStepTypeName());
        assertNull(stepType.getId());

        DeviceType deviceType = new DeviceType();
        stepType.setDeviceType(deviceType);
        assertEquals(stepType.getDeviceType(),deviceType);

        Continuity continuity = new Continuity();
        stepType.setContinuity(continuity);
        assertEquals(stepType.getContinuity(),continuity);

        stepType.setStepTypeName("newStepType");
        assertEquals(stepType.getStepTypeName(),"newStepType");

        long id = (long) 10;
        stepType.setId(id);
        assertEquals(stepType.getId(),id);
    }
}
