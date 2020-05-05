package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.Composition;
import be.uantwerpen.labplanner.Model.Continuity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContinuityRepository extends CrudRepository<Continuity, Long> {

    List<Continuity> findAll();

    Optional<Continuity> findById(Long aLong);
}
