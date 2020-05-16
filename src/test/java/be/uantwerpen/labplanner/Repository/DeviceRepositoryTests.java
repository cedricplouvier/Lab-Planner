package be.uantwerpen.labplanner.Repository;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceInformation;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.common.model.users.Privilege;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.repository.users.PrivilegeRepository;
import be.uantwerpen.labplanner.common.repository.users.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class DeviceRepositoryTests {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceTypeRepository deviceTypeRepository;



    @Test
    public void testDeviceSave(){
        //create Device
        Device device = new Device();
        device.setDevicename("device");
        //Set variables
        device.setComment("test");

        DeviceType d1 = new DeviceType("devicetypetest",false);
        deviceTypeRepository.save(d1);
        device.setDeviceType(d1);

        long precount = deviceRepository.count();

        //save product & varify id
        assertNull(device.getId());
        deviceRepository.save(device);
        assertNotNull(device.getId());

        //retrieve Role from database.
        Device fetchedDevice = deviceRepository.findById(device.getId()).orElse(null);
        assertNotNull(fetchedDevice);

        //and the fetched Role should equal the real Role
        assertEquals(fetchedDevice.getDevicename(),device.getDevicename());
        assertEquals(fetchedDevice.getId(),device.getId());
        assertEquals(fetchedDevice.getDeviceType(),device.getDeviceType());
        assertEquals(fetchedDevice.getComment(),device.getComment());

        //update name & desciption
        fetchedDevice.setDevicename("testname");
        DeviceType d2 = new DeviceType("newDevice",true);
        deviceTypeRepository.save(d2);
        fetchedDevice.setDeviceType(d2);
        fetchedDevice.setComment("newComment");
        deviceRepository.save(fetchedDevice);

        Device fetchedUpdated = deviceRepository.findById(fetchedDevice.getId()).orElse(null);

        assertEquals(fetchedUpdated.getDevicename(),fetchedDevice.getDevicename());
        assertEquals(fetchedUpdated.getDeviceType(),d2);
        assertEquals(fetchedUpdated.getComment(),fetchedDevice.getComment());


        assertEquals(deviceRepository.count(),precount+1);

        int count = 0;
        for (Device p : deviceRepository.findAll()) {
            count++;
        }

        assertEquals(count,precount+1);

        //delete user and check if still in database
        deviceRepository.deleteById(fetchedUpdated.getId());

        assertEquals(deviceRepository.count(),precount);
        assertNull(deviceRepository.findById(fetchedUpdated.getId()).orElse(null));

        //check for delete if nnot exists
        assertThrows(EmptyResultDataAccessException.class,()->{deviceRepository.deleteById(fetchedUpdated.getId());});

    }




}
