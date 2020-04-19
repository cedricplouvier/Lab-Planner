package be.uantwerpen.labplanner.Repository;
import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Experiment;
import be.uantwerpen.labplanner.Model.ExperimentType;
import be.uantwerpen.labplanner.Model.StepType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class ExperimentTypeRepositoryTests {
    @Autowired
    private ExperimentTypeRepository experimentTypeRepository;
    @Autowired
    private StepTypeRepository stepTypeRepository;
    @Test
    public void testSaveExperimentType(){
        //New ExperimentType configuration
        List<StepType> stepTypeList= new ArrayList<>();
        StepType stepType = stepTypeRepository.findStepTypeByName("Autosaw").get();
        StepType stepType1= stepTypeRepository.findStepTypeByName("Balance").get();
        stepTypeList.add(stepType);
        stepTypeList.add(stepType1);
        ExperimentType experimentType = new ExperimentType();
        experimentType.setStepTypes(stepTypeList);
        experimentType.setExpname("NewExperiment");

        //Verify if experimentType gets an id from repository --> its added to repository
        long precount = experimentTypeRepository.count();
        assertNull(experimentType.getId());
        experimentTypeRepository.save(experimentType);
        assertNotNull(experimentType.getId());

        ExperimentType exp1= experimentTypeRepository.findById(experimentType.getId()).get();
        assertNotNull(exp1);
        //check if fields correspond
        assertEquals(exp1.getStepTypes().size(),experimentType.getStepTypes().size());
        assertEquals(exp1.getExpname(),experimentType.getExpname());

        //update ExperimentType name
        exp1.setExpname("Changed Experiment Type");
        experimentTypeRepository.save(exp1);
        ExperimentType exp2=experimentTypeRepository.findById(exp1.getId()).orElse(null);
        assertEquals(exp2.getExpname(),exp1.getExpname());

        //update StepTypeList
        StepType stepType2= stepTypeRepository.findStepTypeByName("Gyratory").get();
        stepTypeList.add(stepType2);
        exp2.setStepTypes(stepTypeList);
        experimentTypeRepository.save(exp2);
        ExperimentType exp3 = experimentTypeRepository.findById(exp2.getId()).orElse(null);
        assertEquals(exp3.getStepTypes().size(),exp2.getStepTypes().size());

        //Only 1 additional Experiment Type should be added to the repository
        Iterable<ExperimentType> experimentTypes = experimentTypeRepository.findAll();
        int count=0;
        for (ExperimentType expt : experimentTypes){
            count++;
        }
        assertEquals(count,precount+1);

    }
}
