package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceInformation;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Repository.DeviceInformationRepository;
import be.uantwerpen.labplanner.Repository.DeviceTypeRepository;
import be.uantwerpen.labplanner.common.model.users.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class DeviceInformationService {
    @Autowired
    private DeviceInformationRepository deviceInformationRepository;
    @Autowired
    private DeviceTypeRepository deviceTypeRepository;
    public DeviceInformationService() {
    }

    public Optional<DeviceInformation> findByInforationName(String deviceInformationName) {
        return this.deviceInformationRepository.findByInformationName(deviceInformationName);
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

    public void addFile(String filename,Long id){
        DeviceInformation tempDeviceInformation = deviceInformationRepository.findById( id).orElse(null);
        if (tempDeviceInformation != null){
            tempDeviceInformation.addFile(filename);
            deviceInformationRepository.save(tempDeviceInformation);
        }
    }
    public void removeFile(String filename,Long id){
        DeviceInformation tempDeviceInformation = deviceInformationRepository.findById( id).orElse(null);
        if (tempDeviceInformation != null){
            List<String> files = tempDeviceInformation.getFiles();
            files.remove(filename);
            tempDeviceInformation.setFiles(files);
            deviceInformationRepository.save(tempDeviceInformation);
        }
    }
    public void saveNewDeviceInformation(DeviceInformation deviceInformation, Long deviceTypeId) {
        DeviceInformation tempDeviceInformation = deviceInformation.getId() == null?null: deviceInformationRepository.findById( deviceInformation.getId()).orElse(null);
        if (tempDeviceInformation != null){
            System.out.println(deviceInformation.getFiles().size());
            tempDeviceInformation.setInformationName(deviceInformation.getInformationName());
            tempDeviceInformation.setInformation(deviceInformation.getInformation());
            tempDeviceInformation.setFiles(deviceInformation.getFiles());
            deviceInformationRepository.save(tempDeviceInformation);
        }
        else{
            DeviceType tempDeviceType = deviceTypeRepository.findById( deviceTypeId).orElse(null);
            List<DeviceInformation> info = tempDeviceType.getDeviceInformation();
            info.add(deviceInformation);
            deviceInformationRepository.save(deviceInformation);
            deviceTypeRepository.save(tempDeviceType);
        }
    }
    private boolean exists(Long id) {
        DeviceInformation deviceInformation = (DeviceInformation)this.deviceInformationRepository.findById(id).orElse((DeviceInformation) null);
        return deviceInformation != null;
    }
}
