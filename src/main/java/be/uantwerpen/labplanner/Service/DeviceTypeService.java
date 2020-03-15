package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.DeviceType;

import be.uantwerpen.labplanner.Repository.DeviceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceTypeService {
    @Autowired
    private DeviceTypeRepository deviceTypeRepository;

    public DeviceTypeService() {
    }

    public List<DeviceType> findAll() {
        return this.deviceTypeRepository.findAll();
    }

    public Optional<DeviceType> findById(Long id) {
        return this.deviceTypeRepository.findById(id);
    }

    public Optional<DeviceType> findByDevicetypeName(String deviceTypeName) {
        return this.deviceTypeRepository.findByDeviceTypeName(deviceTypeName);
    }
    public void saveSomeAttributes(DeviceType deviceType) {
        DeviceType tempDeviceType = deviceType.getId() == null?null:
                deviceTypeRepository.findById( deviceType.getId()).orElse(null);

        if (tempDeviceType != null){
            tempDeviceType.setOvernightuse(deviceType.getOvernightuse());
            tempDeviceType.setDeviceTypeName(deviceType.getDeviceTypeName());
            deviceTypeRepository.save(tempDeviceType);
        }
        else{
            deviceTypeRepository.save(deviceType);
        }
    }
    public DeviceType save(DeviceType deviceType) {
        return (DeviceType)this.deviceTypeRepository.save(deviceType);
    }

    public void delete(Long id){deviceTypeRepository.delete(deviceTypeRepository.findById(id).orElse(null));};
    public Boolean deleteById(Long id) {
        if (this.exists(id)) {
            this.deviceTypeRepository.deleteById(id);
            return !this.exists(id);
        } else {
            return false;
        }
    }

    private Boolean exists(Long id) {
        DeviceType deviceType = (DeviceType)this.deviceTypeRepository.findById(id).orElse((DeviceType) null);
        return deviceType != null;
    }
}
