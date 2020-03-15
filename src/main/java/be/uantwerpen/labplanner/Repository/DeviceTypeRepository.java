package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.common.model.users.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceTypeRepository extends CrudRepository<DeviceType, Long> {
    Optional<DeviceType> findByDeviceTypeName(String deviceTypeName);
    List<DeviceType> findAll();
}
