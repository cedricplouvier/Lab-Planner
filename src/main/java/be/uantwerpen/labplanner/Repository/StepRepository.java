package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.Step;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepRepository extends CrudRepository {
     //void save(Step tempStep);

     Step findByUserName(String userName);

     Iterable<Step> findAllByUserName(String user);
}
