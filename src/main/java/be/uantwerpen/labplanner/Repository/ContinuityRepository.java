package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.ContinuityAli;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContinuityRepository extends CrudRepository<ContinuityAli, Long> {
}
