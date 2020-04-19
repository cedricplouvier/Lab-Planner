package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.Step;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StepRepository extends CrudRepository <Step,Long> {

     Optional<Step> findByDevice(Device device);
     List<Step> findAll();
}
