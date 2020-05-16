package be.uantwerpen.labplanner.Service;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.DeviceInformation;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Repository.DeviceInformationRepository;
import be.uantwerpen.labplanner.Repository.DeviceTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class DeviceTypeServiceTests {

    @Autowired
    DeviceTypeService deviceTypeService;

    @Autowired
    DeviceTypeRepository deviceTypeRepository;

    @Autowired
    DeviceInformationRepository deviceInformationRepository;

    @Test
    public void testGetById(){
        //create Device
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("test1");
        //Set variables
        deviceType.setColor("test");
        deviceType.setOvernightuse(true);
        deviceTypeRepository.save(deviceType);
        DeviceType fetched = deviceTypeService.findById(deviceType.getId()).orElse(null);
        assertNotNull(fetched);
    }
    @Test
    public void testSaveNew(){
        //create Device
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("test2");
        //Set variables
        deviceType.setColor("test");
        deviceType.setOvernightuse(true);
        deviceTypeService.saveNewDeviceType(deviceType);
        DeviceType fetched = deviceTypeService.findById(deviceType.getId()).orElse(null);
        assertNotNull(fetched);
    }
    @Test
    public void testSaveChange(){
        //create Device
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("test3");
        //Set variables
        deviceType.setColor("test");
        deviceType.setOvernightuse(true);
        deviceTypeService.save(deviceType);
        DeviceType fetched = deviceTypeService.findById(deviceType.getId()).orElse(null);
        assertNotNull(fetched);

        deviceType.setColor("changed");
        deviceTypeService.saveNewDeviceType(fetched);
        DeviceType updateFetch = deviceTypeService.findById(deviceType.getId()).orElse(null);

        assertEquals(updateFetch.getId(),fetched.getId());
    }
    @Test
    public void testGetByName(){
        //create Device
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("test4");
        //Set variables
        deviceType.setColor("test");
        deviceType.setOvernightuse(true);
        deviceTypeRepository.save(deviceType);
        DeviceType fetched = deviceTypeService.findByDevicetypeName("test4").orElse(null);
        assertNotNull(fetched);
    }

    @Test
    public void testDeleteById(){
        //create Device
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("test5");
        //Set variables
        deviceType.setColor("test");
        deviceType.setOvernightuse(true);


        deviceTypeService.save(deviceType);
        List<DeviceType> fetchedList  = deviceTypeService.findAll();
        deviceTypeService.deleteById(deviceType.getId());
        List<DeviceType> fetchedList2  = deviceTypeService.findAll();
        assertEquals(fetchedList.size()-1, fetchedList2.size());
    }


    @Test
    public void testGetAll(){
        List<DeviceType> list = deviceTypeService.findAll();
         assertNotNull(list);
    }


}
