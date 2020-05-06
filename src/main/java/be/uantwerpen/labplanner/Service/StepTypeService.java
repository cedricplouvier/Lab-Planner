package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.Continuity;
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
    private ContinuityService continuityService;

    public List<StepType> findAll() {
        return this.stepTypeRepository.findAll();
    }

    public void save(StepType stepType) {
        continuityService.save(stepType.getContinuity());
        this.stepTypeRepository.save(stepType);
    }

    public Optional<StepType> findStepTypeByName(String name) {
        return stepTypeRepository.findStepTypeByName(name);
    }

    public Optional<StepType> findById(Long id) {
        return stepTypeRepository.findById(id);
    }

    public void saveStepTypeInCustomExperiment(StepType stepType) {
        StepType tempStep = stepType.getId() == null ? null : stepTypeRepository.findById(stepType.getId()).orElse(null);
        if (tempStep != null) {
            tempStep.setDeviceType(stepType.getDeviceType());
            tempStep.setStepTypeName(stepType.getStepTypeName());
            tempStep.setFixedTimeType(stepType.getFixedTimeType());
            // continuity is already saved and in custom experiment can't be changed
            Continuity customContinuity = continuityService.findById((long) 191).orElse(null);
            tempStep.setContinuity(customContinuity);
            stepTypeRepository.save(tempStep);
        } else {
            stepType.setContinuity(continuityService.findById((long) 191).orElse(null));
            stepTypeRepository.save(stepType);
        }
    }

    public void saveNewStepType(StepType stepType) {
        StepType tempStep = stepType.getId() == null ? null : stepTypeRepository.findById(stepType.getId()).orElse(null);
        if (tempStep != null) {
            tempStep.setDeviceType(stepType.getDeviceType());
            tempStep.setStepTypeName(stepType.getStepTypeName());
            tempStep.setFixedTimeType(stepType.getFixedTimeType());
            //save continuity
            Continuity tmpContinuity = stepType.getContinuity().getId() == null ? null : continuityService.findById(stepType.getContinuity().getId()).orElse(null);
            if (tmpContinuity != null) {
                tmpContinuity.setType(stepType.getContinuity().getType());
                tmpContinuity.setDirectionType(stepType.getContinuity().getDirectionType());
                tmpContinuity.setMinutes(stepType.getContinuity().getMinutes());
                tmpContinuity.setHours(stepType.getContinuity().getHours());
                continuityService.save(tmpContinuity);
            } else {
                continuityService.save(stepType.getContinuity());
                tempStep.setContinuity(stepType.getContinuity());
            }
            stepTypeRepository.save(tempStep);
        } else {
            continuityService.save(stepType.getContinuity());
            stepTypeRepository.save(stepType);
        }
    }

    public void delete(Long id) {
        StepType tmpStepType = this.stepTypeRepository.findById(id).orElse(null);
        //remove continuity if it's possible and it's not default continuity for custom experiment
        if (tmpStepType != null && tmpStepType.getContinuity() != null && tmpStepType.getContinuity().getId() != null && tmpStepType.getContinuity().getId() != 191) {
            Continuity tmpContinuity =tmpStepType.getContinuity();
            tmpStepType.setContinuity(null);
            continuityService.delete(tmpContinuity.getId());
        }
        this.stepTypeRepository.deleteById(id);
    }
}
