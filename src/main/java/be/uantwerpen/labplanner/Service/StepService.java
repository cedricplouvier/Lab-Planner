package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Repository.StepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StepService {
    @Autowired
    private StepRepository stepRepository;
    public StepService(){}

    public List<Step> findAll() {
        return this.stepRepository.findAll();
    }

    public Optional<Step> findById(Long id){
        return this.stepRepository.findById(id);
    }



    public void save(Step step) {
            stepRepository.save(step);
    }
    public void saveSomeAttributes(Step step) {
        Step tempStep = step.getId() == null?null: stepRepository.findById( step.getId()).orElse(null);
        if (tempStep != null){
            tempStep.setDevice(step.getDevice());
            tempStep.setStart(step.getStart());
            tempStep.setEnd(step.getEnd());
            tempStep.setStartHour(step.getStartHour());
            tempStep.setEndHour(step.getEndHour());
            tempStep.setUser(step.getUser());
            tempStep.setComment(step.getComment());
            tempStep.setStepType(step.getStepType());
            tempStep.setMixture(step.getMixture());
            tempStep.setAmount(step.getAmount());
            stepRepository.save(tempStep);
        } else{
            stepRepository.save(step);
        }
    }

    public void delete(Long id) {
        this.stepRepository.deleteById(id);
    }


}
