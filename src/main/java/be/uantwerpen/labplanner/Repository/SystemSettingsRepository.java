package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.OfficeHours;
import be.uantwerpen.labplanner.Model.SystemSettings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemSettingsRepository extends CrudRepository<SystemSettings, Long> {
    List<SystemSettings> findAll();

    Optional<SystemSettings> findById(Long aLong);



}
