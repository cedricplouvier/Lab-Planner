package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Repository.StepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StepService {
    @Autowired
    private StepRepository stepRepository;
    public StepService(){}

    public List<Step> findAll() {
        return this.stepRepository.findAll();
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
            stepRepository.save(tempStep);
        } else{
            stepRepository.save(step);
        }
    }

    public void delete(Long id) {
        this.stepRepository.deleteById(id);
    }


}
