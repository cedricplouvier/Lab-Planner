package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.*;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.repository.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class StepRepositoryTests {
    @Autowired
    private StepRepository stepRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StepTypeRepository stepTypeRepository;

    @Test
    public void testSaveStep(){
        //New Step config
        Device device =deviceRepository.findByDevicename("Autosaw 1").get();
        User user = userRepository.findByUsername("Ali").get();
        Step step= new Step(user,device,"2020-03-18","2020-03-18","11:00","12:00","test comment");
        long precount = stepRepository.count();
        //Verify step gets an id from repository --> its added to repository
        assertNull(step.getId());
        stepRepository.save(step);
        assertNotNull(step.getId());

        Step s1=stepRepository.findById(step.getId()).orElse(null);
        assertNotNull(s1); //found in repository
        //check some fields that should correspond
        assertEquals(step.getDevice(),s1.getDevice());
        assertEquals(step.getId(),s1.getId());

        //update startdate
        s1.setStart("2020-03-17");
        stepRepository.save(s1);
        Step UpdatedStep=stepRepository.findById(s1.getId()).orElse(null);
        assertEquals(s1.getStart(),UpdatedStep.getStart());

        //update enddate
        UpdatedStep.setEnd("2020-03-19");
        stepRepository.save(UpdatedStep);
        Step s2=stepRepository.findById(s1.getId()).orElse(null);
        assertEquals(UpdatedStep.getEnd(),s2.getEnd());

        //update starthour
        s2.setStartHour("10:00");
        stepRepository.save(s2);
        Step s3=stepRepository.findById(s1.getId()).orElse(null);
        assertEquals(s3.getStartHour(),s2.getStartHour());

        //update endhour
        s3.setEndHour("13:00");
        stepRepository.save(s3);
        Step s4=stepRepository.findById(s1.getId()).orElse(null);
        assertEquals(s4.getEndHour(),s3.getEndHour());

        //update comment
        s4.setComment("Updated comment");
        stepRepository.save(s4);
        Step s5=stepRepository.findById(s1.getId()).orElse(null);
        assertEquals(s5.getComment(),s4.getComment());

        //update stepType
        StepType stepType = stepTypeRepository.findStepTypeByName("Autosaw").get();
        s5.setStepType(stepType);
        stepRepository.save(s5);
        Step s6=stepRepository.findById(s1.getId()).orElse(null);
        assertEquals(s5.getStepType(),s6.getStepType());

        //update user
        User user1= userRepository.findByUsername("Ruben").get();
        s6.setUser(user1);
        stepRepository.save(s6);
        Step s7=stepRepository.findById(s1.getId()).orElse(null);
        assertEquals(s7.getUser(),s6.getUser());

        //update device
        Device device1 = deviceRepository.findByDevicename("Autosaw 2").get();
        s7.setDevice(device1);
        stepRepository.save(s7);
        Step s8=stepRepository.findById(s1.getId()).orElse(null);
        assertEquals(s7.getDevice(),s8.getDevice());

        //Only added 1 step and updated that step --> steprepository should only have 1 additional step
        Iterable<Step> steps = stepRepository.findAll();
        int count = 0;
        for (Step stepi : steps){
            count++;
        }
        assertEquals(count,precount+1);
    }
}
