package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.DeviceInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class DeviceInformationService {
    @Autowired
    private DeviceInformationService deviceInformationService;

    public DeviceInformationService() {
    }

    public List<DeviceInformation> findAll() {
        return this.deviceInformationService.findAll();
    }

    public Optional<DeviceInformation> findById(Long id) {
        return this.deviceInformationService.findById(id);
    }

    public DeviceInformation save(DeviceInformation deviceInformation) {
        return (DeviceInformation)this.deviceInformationService.save(deviceInformation);
    }

    public Boolean deleteById(Long id) {
        if (this.exists(id)) {
            this.deviceInformationService.deleteById(id);
            return !this.exists(id);
        } else {
            return false;
        }
    }

    private boolean exists(Long id) {
        DeviceInformation deviceInformation = (DeviceInformation)this.deviceInformationService.findById(id).orElse((DeviceInformation) null);
        return deviceInformation != null;
    }
}
