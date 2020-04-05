package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.Composition;
import be.uantwerpen.labplanner.Model.Mixture;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompositionRepository extends CrudRepository<Composition, Long> {
    List<Composition> findAll();

    Optional<Composition> findById(Long aLong);

}