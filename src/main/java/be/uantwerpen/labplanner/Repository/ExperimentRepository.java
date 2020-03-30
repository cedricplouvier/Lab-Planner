package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.Experiment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ExperimentRepository extends CrudRepository<Experiment, Long> {
    List<Experiment> findAll();
    Optional<Experiment> findByExperimentname(String Experimentname);
}
