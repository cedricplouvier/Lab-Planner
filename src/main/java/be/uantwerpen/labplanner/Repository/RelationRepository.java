package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.Relation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RelationRepository extends CrudRepository<Relation, Long> {
    List<Relation> findAll();

    Optional<Relation> findByResearcherID(long id);

    Optional<Relation> findById(long id);



}
