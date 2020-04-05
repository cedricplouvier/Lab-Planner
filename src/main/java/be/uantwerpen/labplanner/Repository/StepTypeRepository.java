package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.StepType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StepTypeRepository extends CrudRepository<StepType,Long> {
    List<StepType> findAll();
    Optional<StepType> findStepTypeById(Long id);
}
