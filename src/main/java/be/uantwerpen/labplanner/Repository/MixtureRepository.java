package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.Mixture;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MixtureRepository extends CrudRepository<Mixture, Long> {
    List<Mixture> findAll();

    Optional<Mixture> findById(Long aLong);

    Optional<Mixture> findByName(String name);
}
