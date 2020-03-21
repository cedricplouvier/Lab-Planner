package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends CrudRepository<Device, Long> {
    Optional<Device> findByDevicename(String devicename);
    List<Device> findAll();
    Optional<Device> findByDeviceType(Long id);
}
