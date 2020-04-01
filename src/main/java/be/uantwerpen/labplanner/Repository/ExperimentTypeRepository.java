package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.ExperimentType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExperimentTypeRepository extends CrudRepository<ExperimentType, Long> {
    List<ExperimentType> findAll();
    Optional<ExperimentType> findByName(String ExperimentTypeName);

    @Override
    Optional<ExperimentType> findById(Long id);
}
