package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceInformation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceInformationRepository extends CrudRepository<DeviceInformation, Long> {
    Optional<DeviceInformation> findByInformationName(String deviceInformationName);
    List<DeviceInformation> findAll();
}
