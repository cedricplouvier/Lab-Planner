package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.Composition;
import be.uantwerpen.labplanner.Model.Mixture;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CompositionRepository extends CrudRepository<Composition, Long> {
    List<Composition> findAll();

    Optional<Composition> findById(Long aLong);

}
