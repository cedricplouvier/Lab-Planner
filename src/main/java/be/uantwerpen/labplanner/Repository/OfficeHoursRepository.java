package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.OfficeHours;
import be.uantwerpen.labplanner.Model.PieceOfMixture;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfficeHoursRepository extends CrudRepository<OfficeHours, Long> {
    List<OfficeHours> findAll();

    Optional<OfficeHours> findById(Long aLong);



}
