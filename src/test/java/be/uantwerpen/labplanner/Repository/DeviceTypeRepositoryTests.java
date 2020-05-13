package be.uantwerpen.labplanner.Repository;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceInformation;
import be.uantwerpen.labplanner.Model.DeviceType;
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
public class DeviceTypeRepositoryTests {

    @Autowired
    private DeviceTypeRepository deviceTypeRepository;

    @Autowired
    private DeviceInformationRepository deviceInformationRepository;

    @Test
    public void testDeviceTypeSave(){
        //create Device
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("devicetype");
        //Set variables
        deviceType.setColor("test");
        deviceType.setOvernightuse(true);
        deviceType.setDevicePictureName("picturename");
        DeviceInformation i1 = new DeviceInformation("info","information");
        deviceInformationRepository.save(i1);
        List<DeviceInformation> info = new ArrayList<DeviceInformation>();
        info.add(i1);
        deviceType.setDeviceInformation(info);

        long precount = deviceTypeRepository.count();

        //save product & varify id
        assertNull(deviceType.getId());
        deviceTypeRepository.save(deviceType);
        assertNotNull(deviceType.getId());

        //retrieve Role from database.
        DeviceType fetchedDevice = deviceTypeRepository.findById(deviceType.getId()).orElse(null);
        assertNotNull(fetchedDevice);

        //and the fetched Role should equal the real Role
        assertEquals(fetchedDevice.getDeviceTypeName(),deviceType.getDeviceTypeName());
        assertEquals(fetchedDevice.getId(),deviceType.getId());
        assertEquals(fetchedDevice.getDeviceInformation().size(),info.size());
        assertEquals(fetchedDevice.getColor(),deviceType.getColor());
        assertEquals(fetchedDevice.getDevicePictureName(),deviceType.getDevicePictureName());
        assertEquals(fetchedDevice.getOvernightuse(),deviceType.getOvernightuse());

        //update name & desciption
        fetchedDevice.setDeviceTypeName("testname");
        DeviceInformation i2 = new DeviceInformation("newinfo","newinformation");
        deviceInformationRepository.save(i2);
        List<DeviceInformation> info2 = new ArrayList<DeviceInformation>();
        info2.add(i2);
        fetchedDevice.setDeviceInformation(info2);
        fetchedDevice.setColor("#123456");
        fetchedDevice.setDevicePictureName("newpicturename");
        deviceTypeRepository.save(fetchedDevice);

        DeviceType fetchedUpdated = deviceTypeRepository.findById(fetchedDevice.getId()).orElse(null);


        assertEquals(fetchedUpdated.getDeviceTypeName(),fetchedDevice.getDeviceTypeName());
        assertEquals(fetchedUpdated.getId(),fetchedDevice.getId());
        assertEquals(fetchedUpdated.getDeviceInformation().size(),fetchedDevice.getDeviceInformation().size());
        assertEquals(fetchedUpdated.getColor(),fetchedDevice.getColor());
        assertEquals(fetchedUpdated.getDevicePictureName(),fetchedDevice.getDevicePictureName());
        assertEquals(fetchedUpdated.getOvernightuse(),fetchedDevice.getOvernightuse());

        assertEquals(deviceTypeRepository.count(),precount+1);

        int count = 0;
        for (DeviceType p : deviceTypeRepository.findAll()) {
            count++;
        }

        assertEquals(count,precount+1);

        //delete user and check if still in database
        deviceTypeRepository.deleteById(fetchedUpdated.getId());

        assertEquals(deviceTypeRepository.count(),precount);
        assertNull(deviceTypeRepository.findById(fetchedUpdated.getId()).orElse(null));

        //check for delete if nnot exists
        assertThrows(EmptyResultDataAccessException.class,()->{deviceTypeRepository.deleteById(fetchedUpdated.getId());});

    }




}
