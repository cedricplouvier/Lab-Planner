package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.Continuity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContinuityRepository extends CrudRepository<Continuity, Long> {
}
