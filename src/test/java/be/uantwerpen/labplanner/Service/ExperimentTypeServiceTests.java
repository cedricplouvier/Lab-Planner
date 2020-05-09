package be.uantwerpen.labplanner.Service;
import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.ExperimentType;
import be.uantwerpen.labplanner.Model.StepType;
import be.uantwerpen.labplanner.Repository.ExperimentTypeRepository;
import be.uantwerpen.labplanner.Repository.StepTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class ExperimentTypeServiceTests {
    @Autowired
    private ExperimentTypeService experimentTypeService;
    @Autowired
    private StepTypeRepository stepTypeRepository;
    @Autowired
    private ExperimentTypeRepository experimentTypeRepository;

    @Test
    public void testFindById(){
        Iterator<StepType> iterator= stepTypeRepository.findAll().iterator();
        List<StepType> stepTypes = new ArrayList<>();
        stepTypes.add(iterator.next());
        stepTypes.add(iterator.next());
        ExperimentType experimentType= new ExperimentType("New ExperimentType",stepTypes,true);
        experimentTypeRepository.save(experimentType);
        ExperimentType exp1 = experimentTypeService.findById(experimentType.getId()).orElse(null);
        assertEquals(exp1.getExpname(),experimentType.getExpname());
    }

    @Test
    public void testSaveExperimentType(){
        Iterator<StepType> iterator= stepTypeRepository.findAll().iterator();
        List<StepType> stepTypes = new ArrayList<>();
        stepTypes.add(iterator.next());
        stepTypes.add(iterator.next());
        ExperimentType experimentType= new ExperimentType("New ExpType",stepTypes,true);
        experimentTypeService.saveExperimentType(experimentType);
        experimentType.setExpname("Other experimenttype");
        experimentTypeService.saveExperimentType(experimentType);
        ExperimentType fetchedExperimentType = experimentTypeService.findById(experimentType.getId()).get();
        assertEquals(experimentType.getExpname(),fetchedExperimentType.getExpname());
    }

    @Test
    public void testDelete(){
        Iterator<StepType> iterator= stepTypeRepository.findAll().iterator();
        List<StepType> stepTypes = new ArrayList<>();
        stepTypes.add(iterator.next());
        stepTypes.add(iterator.next());
        ExperimentType experimentType= new ExperimentType("New ExpType",stepTypes,true);
        experimentTypeRepository.save(experimentType);
        List<ExperimentType> experimentTypes= experimentTypeRepository.findAll();
        experimentTypeService.delete(experimentType.getId());
        List<ExperimentType> experimentTypes1 = experimentTypeRepository.findAll();
        assertEquals(experimentTypes.size()-1,experimentTypes1.size());
    }

    @Test
    public void testFindAll(){
        List<ExperimentType> experimentTypes = experimentTypeService.findAll();
        assertNotNull(experimentTypes);
    }
}
