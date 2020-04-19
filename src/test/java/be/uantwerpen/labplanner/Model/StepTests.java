package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.common.model.users.User;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class StepTests {
    private Step step;

    @Test
    public void TestStep() throws Exception{
        step = new Step();
        assertNull(step.getStart());
        assertNull(step.getStartHour());
        assertNull(step.getEnd());
        assertNull(step.getEndHour());
        assertNull(step.getUser());
        assertNull(step.getDevice());
        assertNull(step.getStepType());
        assertNull(step.getId());

        String startdate="2020-04-16";
        step.setStart(startdate);
        assertEquals(step.getStart(),startdate);

        String enddate="2020-04-16";
        step.setEnd(enddate);
        assertEquals(step.getEnd(),enddate);

        String starthour = "14:00";
        step.setStartHour(starthour);
        assertEquals(step.getStartHour(),starthour);

        String endhour = "16:00";
        step.setEndHour(endhour);
        assertEquals(step.getEndHour(),endhour);

        User user = new User();
        step.setUser(user);
        assertEquals(step.getUser(),user);

        StepType stepType = new StepType();
        step.setStepType(stepType);
        assertEquals(step.getStepType(),stepType);

        Device device = new Device();
        step.setDevice(device);
        assertEquals(step.getDevice(),device);

        Long id = (long) 10;
        step.setId(id);
        assertEquals(step.getId(),id);

    }
}
