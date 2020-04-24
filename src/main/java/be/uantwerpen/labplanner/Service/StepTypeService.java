package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.StepType;
import be.uantwerpen.labplanner.Repository.ContinuityRepository;
import be.uantwerpen.labplanner.Repository.StepTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StepTypeService {
    @Autowired
    private StepTypeRepository stepTypeRepository;
    @Autowired
    private ContinuityRepository continuityRepository;

    public List<StepType> findAll(){return this.stepTypeRepository.findAll();}
    public void save(StepType stepType){continuityRepository.save(stepType.getContinuity());this.stepTypeRepository.save(stepType);}
    public Optional<StepType> findStepTypeByName(String name){
        return stepTypeRepository.findStepTypeByName(name);
    }
    public Optional<StepType> findById(Long id){return stepTypeRepository.findById(id);}
    public void saveNewStepType(StepType stepType){
        StepType tempStep = stepType.getId() == null?null: stepTypeRepository.findById( stepType.getId()).orElse(null);
        if (tempStep != null){
            tempStep.setDeviceType(stepType.getDeviceType());
            tempStep.setStepTypeName(stepType.getStepTypeName());
            continuityRepository.save(stepType.getContinuity());
            tempStep.setContinuity(stepType.getContinuity());
            stepTypeRepository.save(tempStep);
        } else{
            continuityRepository.save(stepType.getContinuity());
            stepTypeRepository.save(stepType);
        }
    }
    public void delete(Long id){this.stepTypeRepository.deleteById(id);}
}