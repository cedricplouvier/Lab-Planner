package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceInformation;
import be.uantwerpen.labplanner.Repository.DeviceInformationRepository;
import be.uantwerpen.labplanner.common.model.users.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class DeviceInformationService {
    @Autowired
    private DeviceInformationRepository deviceInformationRepository;

    public DeviceInformationService() {
    }



    public List<DeviceInformation> findAll() {
        return this.deviceInformationRepository.findAll();
    }

    public Optional<DeviceInformation> findById(Long id) {
        return this.deviceInformationRepository.findById(id);
    }
    public DeviceInformation save(DeviceInformation deviceInformation) {
        return (DeviceInformation)this.deviceInformationRepository.save(deviceInformation);
    }

    public Boolean deleteById(Long id) {
        if (this.exists(id)) {
            this.deviceInformationRepository.deleteById(id);
            return !this.exists(id);
        } else {
            return false;
        }
    }
    public void saveSomeAttributes(DeviceInformation deviceInformation) {
        DeviceInformation tempDeviceInformation = deviceInformation.getId() == null?null: deviceInformationRepository.findById( deviceInformation.getId()).orElse(null);
        if (tempDeviceInformation != null){
            tempDeviceInformation.setInformationName(deviceInformation.getInformationName());
            tempDeviceInformation.setInformation(deviceInformation.getInformation());
            tempDeviceInformation.setFiles(deviceInformation.getFiles());
            deviceInformationRepository.save(tempDeviceInformation);
        }
        else{
            deviceInformationRepository.save(deviceInformation);
        }
    }
    private boolean exists(Long id) {
        DeviceInformation deviceInformation = (DeviceInformation)this.deviceInformationRepository.findById(id).orElse((DeviceInformation) null);
        return deviceInformation != null;
    }
}
