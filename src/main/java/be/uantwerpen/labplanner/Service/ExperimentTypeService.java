package be.uantwerpen.labplanner.Service;

        import be.uantwerpen.labplanner.Model.ExperimentType;
        import be.uantwerpen.labplanner.Repository.ExperimentTypeRepository;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.util.List;
        import java.util.Optional;

@Service
public class ExperimentTypeService {
    @Autowired
    private ExperimentTypeRepository experimentTypeRepository;
    public ExperimentTypeService(){}
    public List<ExperimentType> findAll(){return this.experimentTypeRepository.findAll();}
    //    public Optional<ExperimentType> findByExperimentTypeName(String ExperimentTypeName){
//        return this.experimentTypeRepository.findByName(ExperimentTypeName);
//    }
    public Optional<ExperimentType> findById(Long id){
        return this.experimentTypeRepository.findById(id);
    }
    public void delete(Long id){experimentTypeRepository.deleteById(id);}
    public void saveExperimentType(ExperimentType experimentType){
        ExperimentType tempExperimentType = experimentType.getId() == null?null: experimentTypeRepository.findById( experimentType.getId()).orElse(null);
        if(tempExperimentType!=null){
            tempExperimentType.setStepTypes(experimentType.getStepTypes());
            tempExperimentType.setExpname(experimentType.getExpname());
            experimentTypeRepository.save(tempExperimentType);
        }
        else experimentTypeRepository.save(experimentType);
    }

}
