package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceInformation;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Repository.DeviceInformationRepository;
import be.uantwerpen.labplanner.Repository.DeviceRepository;
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
    private DeviceRepository deviceRepository;
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
    public void saveNewDeviceInformation(DeviceInformation deviceInformation, Long deviceId) {
        DeviceInformation tempDeviceInformation = deviceInformation.getId() == null?null: deviceInformationRepository.findById( deviceInformation.getId()).orElse(null);
        if (tempDeviceInformation != null){
            tempDeviceInformation.setInformationName(deviceInformation.getInformationName());
            tempDeviceInformation.setInformation(deviceInformation.getInformation());
            for(String file : deviceInformation.getFiles()){
                tempDeviceInformation.addFile(file);
            }
            deviceInformationRepository.save(tempDeviceInformation);
        }
        else{
            Device tempDevice = deviceRepository.findById( deviceId).orElse(null);
            List<DeviceInformation> info = tempDevice.getDeviceInformation();
            info.add(deviceInformation);
            deviceInformationRepository.save(deviceInformation);
            deviceRepository.save(tempDevice);
        }
    }
    private boolean exists(Long id) {
        DeviceInformation deviceInformation = (DeviceInformation)this.deviceInformationRepository.findById(id).orElse((DeviceInformation) null);
        return deviceInformation != null;
    }
}
