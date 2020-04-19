package be.uantwerpen.labplanner.Service;
import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Continuity;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Model.StepType;
import be.uantwerpen.labplanner.Repository.ContinuityRepository;
import be.uantwerpen.labplanner.Repository.DeviceTypeRepository;
import be.uantwerpen.labplanner.Repository.StepTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class StepTypeServiceTests {
    @Autowired
    private StepTypeService stepTypeService;
    @Autowired
    private DeviceTypeRepository deviceTypeRepository;
    @Autowired
    private ContinuityRepository continuityRepository;
    @Autowired
    private StepTypeRepository stepTypeRepository;

    @Test
    public void testGetStepTypeById(){
        Iterator<Continuity> iterator = continuityRepository.findAll().iterator();
        Continuity continuity = iterator.next();
        DeviceType deviceType = deviceTypeRepository.findByDeviceTypeName("Autosaw").get();
        StepType stepType = new StepType(deviceType,continuity,"New steptype");
        stepTypeRepository.save(stepType);
        StepType fetchedStepType = stepTypeService.findById(stepType.getId()).orElse(null);
        assertEquals(fetchedStepType.getDeviceType(),stepType.getDeviceType());
    }

    @Test
    public void testFindByStepTypeName(){
        Iterator<Continuity> iterator = continuityRepository.findAll().iterator();
        Continuity continuity = iterator.next();
        DeviceType deviceType = deviceTypeRepository.findByDeviceTypeName("Autosaw").get();
        StepType stepType = new StepType(deviceType,continuity,"New steptype");
        stepTypeRepository.save(stepType);
        StepType fetchedStepType = stepTypeService.findStepTypeByName(stepType.getStepTypeName()).orElse(null);
        assertEquals(fetchedStepType.getDeviceType(),stepType.getDeviceType());
    }

    @Test
    public void testSaveNewStepType(){
        Iterator<Continuity> iterator = continuityRepository.findAll().iterator();
        Continuity continuity = iterator.next();
        DeviceType deviceType = deviceTypeRepository.findByDeviceTypeName("Autosaw").get();
        StepType stepType = new StepType(deviceType,continuity,"New steptype");
        stepTypeRepository.save(stepType);
        stepType.setStepTypeName("Different StepType");
        stepTypeService.saveNewStepType(stepType);
        StepType stepType1 = stepTypeService.findById(stepType.getId()).orElse(null);
        assertEquals(stepType1.getStepTypeName(),stepType.getStepTypeName());
    }

    @Test
    public void testDeleteById(){
        Iterator<Continuity> iterator = continuityRepository.findAll().iterator();
        Continuity continuity = iterator.next();
        DeviceType deviceType = deviceTypeRepository.findByDeviceTypeName("Autosaw").get();
        StepType stepType = new StepType(deviceType,continuity,"New steptype");
        stepTypeRepository.save(stepType);
        List<StepType> fetchedStepTypes = stepTypeService.findAll();
        stepTypeService.delete(stepType.getId());
        List<StepType> fetchedStepTypes1= stepTypeService.findAll();
        assertEquals(fetchedStepTypes.size()-1,fetchedStepTypes1.size());
    }

    @Test
    public void testFindAll(){
        List<StepType> stepTypes = stepTypeService.findAll();
        assertNotNull(stepTypes);
    }
}
