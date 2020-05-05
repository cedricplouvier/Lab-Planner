package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Repository.DeviceRepository;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.repository.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    public DeviceService() {
    }

    public List<Device> findAll() {
        return this.deviceRepository.findAll();
    }

    public Optional<Device> findById(Long id) {
        return this.deviceRepository.findById(id);
    }

    public Optional<Device> findByDevicename(String devicename) {
        return this.deviceRepository.findByDevicename(devicename);
    }

    public Device save(Device device) {
        return (Device)this.deviceRepository.save(device);
    }
    public void saveNewDevice(Device device) {
        if(device.getId()!=null) {
            Device tempDevice = (Device) this.deviceRepository.findById(device.getId()).orElse(null);
            if (tempDevice != null) {
                tempDevice.setDeviceType(device.getDeviceType());
                tempDevice.setDevicename(device.getDevicename());
                tempDevice.setComment(device.getComment());
                deviceRepository.save(tempDevice);
            } else {
                deviceRepository.save(device);
            }
        }else{
            deviceRepository.save(device);

        }
    }

    public void delete(Long id){
        if (this.exists(id)) {
            this.deviceRepository.deleteById(id);

        }
        System.out.println("ok");
        };
    public Boolean deleteById(Long id) {
        if (this.exists(id)) {
            this.deviceRepository.deleteById(id);
            return !this.exists(id);
        } else {
            return false;
        }
    }

    private Boolean exists(Long id) {
        Device device = (Device)this.deviceRepository.findById(id).orElse((Device)null);
        return device != null;
    }
}
