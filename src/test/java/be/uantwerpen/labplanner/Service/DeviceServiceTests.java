package be.uantwerpen.labplanner.Service;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Repository.DeviceRepository;
import be.uantwerpen.labplanner.Repository.DeviceRepository;
import be.uantwerpen.labplanner.Repository.DeviceTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class DeviceServiceTests {

    @Autowired
    DeviceService deviceService;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    DeviceTypeRepository deviceTypeRepository;

    @Test
    public void testGetById(){
        Device device = new Device();
        device.setDevicename("device");
        //Set variables
        device.setComment("test");

        DeviceType d1 = new DeviceType("devicetype",false);
        deviceTypeRepository.save(d1);
        device.setDeviceType(d1);        deviceRepository.save(device);
        Device fetched = deviceService.findById(device.getId()).orElse(null);
        assertNotNull(fetched);
    }
    @Test
    public void testSaveNew(){
        Device device = new Device();
        device.setDevicename("testdevice2");
        //Set variables
        device.setComment("test");
        DeviceType d1 = new DeviceType("testdevicetype2",false);
        deviceTypeRepository.save(d1);
        device.setDeviceType(d1);
        deviceService.saveNewDevice(device);
        Device fetched = deviceService.findById(device.getId()).orElse(null);
        assertNotNull(fetched);
    }
    @Test
    public void testSaveChange(){
        Device device = new Device();
        device.setDevicename("testdevice");
        //Set variables
        device.setComment("test");
        DeviceType d1 = new DeviceType("testdevicetype",false);
        deviceTypeRepository.save(d1);
        device.setDeviceType(d1);
        deviceService.save(device);
        Device fetched = deviceService.findById(device.getId()).orElse(null);
        assertNotNull(fetched);

        device.setComment("changed");
        deviceService.saveNewDevice(fetched);
        Device updateFetch = deviceService.findById(device.getId()).orElse(null);

        assertEquals(updateFetch.getId(),fetched.getId());
    }
    @Test
    public void testGetByName(){
        Device device = new Device();
        device.setDevicename("test");
        //Set variables
        device.setComment("test");

        DeviceType d1 = new DeviceType("devicetype",false);
        deviceTypeRepository.save(d1);
        device.setDeviceType(d1);
        deviceRepository.save(device);
        Device fetched = deviceService.findByDevicename("test").orElse(null);
        assertNotNull(fetched);
    }

    @Test
    public void testDeleteById(){
        Device device = new Device();
        device.setDevicename("halt");
        //Set variables
        device.setComment("test");
        DeviceType d1 = new DeviceType("devicetype2",false);
        deviceTypeRepository.save(d1);
        device.setDeviceType(d1);

        deviceService.save(device);
        List<Device> fetchedList  = deviceService.findAll();
        deviceService.deleteById(device.getId());
        List<Device> fetchedList2  = deviceService.findAll();
        assertEquals(fetchedList.size()-1, fetchedList2.size());
    }


    @Test
    public void testGetAll(){
        List<Device> list = deviceService.findAll();
         assertNotNull(list);
    }


}
