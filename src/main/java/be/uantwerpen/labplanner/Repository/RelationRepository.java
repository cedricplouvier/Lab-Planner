package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.Relation;
import be.uantwerpen.labplanner.common.model.users.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RelationRepository extends CrudRepository<Relation, Long> {
    List<Relation> findAll();

Optional<Relation> findByResearcher(User researcher);




}
