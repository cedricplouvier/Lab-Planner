package be.uantwerpen.labplanner.Services;

import Model.Step;
import be.uantwerpen.labplanner.Repository.StepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StepService {
    @Autowired
    private StepRepository stepRepository;
    public Iterable<Step> findAll() {
        return this.stepRepository.findAll();
    }

    public Iterable<Step> findAllByUserName(String User) {
        return this.stepRepository.findAllByUserName(User);
    }

    public void save(Step step) {
            stepRepository.save(step);
    }

    public void delete(Long id) {
        this.stepRepository.deleteById(id);
    }

    public Step findByUserName(String userName) {
        return stepRepository.findByUserName(userName);
    }

}
