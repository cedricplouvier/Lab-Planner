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

    public void saveNewStepType(StepType stepType) {
        StepType tempStep = stepType.getId() == null ? null : stepTypeRepository.findById(stepType.getId()).orElse(null);
        if (tempStep != null) {
            tempStep.setDeviceType(stepType.getDeviceType());
            tempStep.setStepTypeName(stepType.getStepTypeName());
            tempStep.setFixedTimeType(stepType.getFixedTimeType());
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
        this.stepTypeRepository.deleteById(id);
    }
}
