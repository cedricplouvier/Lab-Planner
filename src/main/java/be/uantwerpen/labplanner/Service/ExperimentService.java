package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.DeviceInformation;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Model.Experiment;
import be.uantwerpen.labplanner.Repository.ExperimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ExperimentService {
    @Autowired
    private ExperimentRepository experimentRepository;
    public ExperimentService(){}

    public List<Experiment> findAll(){return this.experimentRepository.findAll();}
    public Optional<Experiment> findById(Long id) {
        return this.experimentRepository.findById(id);
    }

    public Optional<Experiment> findByExperimentName(String ExperimentName) {
        return this.experimentRepository.findByExperimentname(ExperimentName);
    }
    public void delete(Long id){experimentRepository.deleteById(id);}
    public void saveExperiment(Experiment experiment){
        Experiment tempExperiment = experiment.getId() == null?null: experimentRepository.findById( experiment.getId()).orElse(null);

        if (tempExperiment != null){
            tempExperiment.setUser(experiment.getUser());
            tempExperiment.setExperimentType(experiment.getExperimentType());
            tempExperiment.setExperimentname(experiment.getExperimentname());
            tempExperiment.setStartDate(experiment.getStartDate());
            experimentRepository.save(tempExperiment);
        }
        else{
            experimentRepository.save(experiment);
        }
    }
}
