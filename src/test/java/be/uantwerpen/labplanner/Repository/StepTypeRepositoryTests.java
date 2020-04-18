package be.uantwerpen.labplanner.Repository;
import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Continuity;
import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Model.StepType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class StepTypeRepositoryTests {
    @Autowired
    private StepTypeRepository stepTypeRepository;
    @Autowired
    private DeviceTypeRepository deviceTypeRepository;
    @Autowired
    private ContinuityRepository continuityRepository;

    @Test
    public void testSaveStepType(){
        //New StepType config
        Iterator<Continuity> iterator = continuityRepository.findAll().iterator();
        Continuity continuity = iterator.next();
        DeviceType deviceType = deviceTypeRepository.findByDeviceTypeName("Autosaw").get();
        StepType stepType = new StepType();
        stepType.setDeviceType(deviceType);
        stepType.setContinuity(continuity);
        stepType.setStepTypeName("A StepType");

        long precount=stepTypeRepository.count();
        //Verify steptype gets an id from repository
        assertNull(stepType.getId());
        stepTypeRepository.save(stepType);
        assertNotNull(stepType.getId());

        StepType st1= stepTypeRepository.findStepTypeById(stepType.getId()).orElse(null);
        assertNotNull(st1);//found in repository
        //check some fields
        assertEquals(stepType.getStepTypeName(),st1.getStepTypeName());
        assertEquals(stepType.getContinuity(),st1.getContinuity());

        //update StepTypeName
        st1.setStepTypeName("Other StepType");
        stepTypeRepository.save(st1);
        StepType st2 = stepTypeRepository.findStepTypeById(stepType.getId()).orElse(null);
        assertEquals(st2.getStepTypeName(),st1.getStepTypeName());
        //update Continuity
        Continuity continuity1 = iterator.next();
        st2.setContinuity(continuity1);
        stepTypeRepository.save(st2);
        StepType st3= stepTypeRepository.findStepTypeById(stepType.getId()).orElse(null);
        assertEquals(st3.getContinuity(),st2.getContinuity());
        //update DeviceType
        st3.setDeviceType(deviceTypeRepository.findByDeviceTypeName("Gyratory").get());
        stepTypeRepository.save(st3);
        StepType st4 = stepTypeRepository.findStepTypeById(stepType.getId()).orElse(null);
        assertEquals(st4.getDeviceType(),st3.getDeviceType());

        //Added 1 Steptype to repository
        Iterable<StepType> stepTypes = stepTypeRepository.findAll();
        int count = 0;
        for (StepType stept : stepTypes){
            count++;
        }
        assertEquals(count,precount+1);


    }
}
