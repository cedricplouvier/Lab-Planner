package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.Step;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StepRepository extends CrudRepository <Step,Long> {
     //void save(Step tempStep);

     //Step findByUserName(String userName);

     List<Step> findAll();
}
