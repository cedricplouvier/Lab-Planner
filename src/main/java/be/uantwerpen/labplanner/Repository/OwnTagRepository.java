package be.uantwerpen.labplanner.Repository;



import java.util.List;
import java.util.Optional;

import be.uantwerpen.labplanner.Model.OwnTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnTagRepository extends CrudRepository<OwnTag, Long> {
    List<OwnTag> findAll();

    Optional<OwnTag> findByName(String name);
}
