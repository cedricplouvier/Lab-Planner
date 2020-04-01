package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Model.StepType;
import be.uantwerpen.labplanner.Repository.StepTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StepTypeService {
    @Autowired
    private StepTypeRepository stepTypeRepository;

    public List<StepType> findAll(){return this.stepTypeRepository.findAll();}
    public void save(StepType stepType){this.stepTypeRepository.save(stepType);}
    public void saveNewStepType(StepType stepType){
        StepType tempStep = stepType.getId() == null?null: stepTypeRepository.findById( stepType.getId()).orElse(null);
        if (tempStep != null){
            tempStep.setDeviceType(stepType.getDeviceType());
            tempStep.setStepTypeName(stepType.getStepTypeName());
            stepTypeRepository.save(tempStep);
        } else{
            stepTypeRepository.save(stepType);
        }
    }
    public void delete(Long id){this.stepTypeRepository.deleteById(id);}
}
