package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Repository.DeviceRepository;
import be.uantwerpen.labplanner.Repository.StepRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class StepServiceTests {
    @Autowired
    private StepService stepService;
    @Autowired
    private StepRepository stepRepository;
    @Autowired
    private DeviceRepository deviceRepository;

    @Test
    public void testGetStepById() {
        Device device = deviceRepository.findByDevicename("Autosaw 1").get();
        Step step = new Step();
        step.setDevice(device);
        stepRepository.save(step);
        Step fetchedStep = stepService.findById(step.getId()).orElse(null);
        assertEquals(fetchedStep.getDevice(),step.getDevice());
    }
    @Test
    public void testGetAllSteps(){
        List<Step> fetchedSteps = stepService.findAll();
        assertNotNull(fetchedSteps);
    }
    @Test
    public void testSaveSomeAttributes(){
        Device device = deviceRepository.findByDevicename("Autosaw 2").get();
        Step step = new Step();
        step.setDevice(device);
        stepRepository.save(step);
        step.setComment("New comment");
        stepService.saveSomeAttributes(step);
        Step step2 = stepService.findById(step.getId()).get();
        assertEquals(step.getComment(),step2.getComment());
    }
    @Test
    public void testDeleteById(){
        Step step = new Step();
        Device device = deviceRepository.findByDevicename("Autosaw 1").get();
        step.setDevice(device);
        stepRepository.save(step);
        List<Step> fetchedSteps = stepService.findAll();
        stepService.delete(step.getId());
        List<Step> fetchedSteps2= stepService.findAll();
        assertEquals(fetchedSteps.size()-1,fetchedSteps2.size());
    }
}
