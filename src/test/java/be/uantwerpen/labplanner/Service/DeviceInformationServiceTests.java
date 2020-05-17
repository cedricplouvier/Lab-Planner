package be.uantwerpen.labplanner.Service;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceInformation;
import be.uantwerpen.labplanner.Model.DeviceInformation;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Repository.DeviceInformationRepository;
import be.uantwerpen.labplanner.Repository.DeviceInformationRepository;
import be.uantwerpen.labplanner.Repository.DeviceRepository;
import be.uantwerpen.labplanner.Repository.DeviceTypeRepository;
import org.junit.Before;
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
public class DeviceInformationServiceTests {

    @Autowired
    DeviceInformationService deviceInformationService;

    @Autowired
    DeviceInformationRepository deviceInformationRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Before
    public void init() {
        //create Device
        Device device = new Device();
        device.setDevicename("device");
        //Set variables
        device.setDevicePictureName("test");
        device.setComment("comment");
        deviceRepository.save(device);
    }

    @Test
    public void testGetById(){

        DeviceInformation deviceInformation = new DeviceInformation();
        deviceInformation.setInformationName("deviceInformation1");
        //Set variables
        deviceInformation.setInformation("test");
        List files = new ArrayList();
        files.add("1");
        files.add("2");
        deviceInformation.setFiles(files);

        deviceInformationRepository.save(deviceInformation);
        DeviceInformation fetched = deviceInformationService.findById(deviceInformation.getId()).orElse(null);
        assertNotNull(fetched);
        deviceInformationService.addFile("newFile",fetched.getId());
        DeviceInformation fetched2 = deviceInformationService.findById(deviceInformation.getId()).orElse(null);
        deviceInformationService.removeFile("newFile",deviceInformation.getId());

    }
    @Test
    public void testSaveNew(){
        DeviceInformation deviceInformation = new DeviceInformation();
        deviceInformation.setInformationName("deviceInformation2");
        //Set variables
        deviceInformation.setInformation("test");
        List files = new ArrayList();
        files.add("1");
        files.add("2");
        deviceInformation.setFiles(files);

        deviceInformationService.saveNewDeviceInformation(deviceInformation,deviceRepository.findAll().get(0).getId());
        DeviceInformation fetched = deviceInformationService.findById(deviceInformation.getId()).orElse(null);
        assertNotNull(fetched);
    }
    @Test
    public void testSaveChange(){
        DeviceInformation deviceInformation = new DeviceInformation();
        deviceInformation.setInformationName("deviceInformation3");
        //Set variables
        deviceInformation.setInformation("test");
        List files = new ArrayList();
        files.add("1");
        files.add("2");
        deviceInformation.setFiles(files);

        deviceInformationService.save(deviceInformation);
        DeviceInformation fetched = deviceInformationService.findById(deviceInformation.getId()).orElse(null);
        assertNotNull(fetched);

        deviceInformation.setInformation("changed");
        deviceInformationService.saveNewDeviceInformation(fetched,deviceRepository.findAll().get(0).getId());
        DeviceInformation updateFetch = deviceInformationService.findById(deviceInformation.getId()).orElse(null);

        assertEquals(updateFetch.getId(),fetched.getId());
    }
    @Test
    public void testGetByName(){
        DeviceInformation deviceInformation = new DeviceInformation();
        deviceInformation.setInformationName("deviceInformation4");
        //Set variables
        deviceInformation.setInformation("test");
        List files = new ArrayList();
        files.add("1");
        files.add("2");
        deviceInformation.setFiles(files);

        deviceInformationRepository.save(deviceInformation);
        DeviceInformation fetched = deviceInformationService.findByInforationName("deviceInformation4").orElse(null);
        assertNotNull(fetched);
    }

    @Test
    public void testDeleteById(){
        DeviceInformation deviceInformation = new DeviceInformation();
        deviceInformation.setInformationName("deviceInformation5");
        //Set variables
        deviceInformation.setInformation("test");
        List files = new ArrayList();
        files.add("1");
        files.add("2");
        deviceInformation.setFiles(files);

        deviceInformationService.save(deviceInformation);
        List<DeviceInformation> fetchedList  = deviceInformationService.findAll();
        deviceInformationService.deleteById(deviceInformation.getId());
        List<DeviceInformation> fetchedList2  = deviceInformationService.findAll();
        assertEquals(fetchedList.size()-1, fetchedList2.size());
    }


    @Test
    public void testGetAll(){
        List<DeviceInformation> list = deviceInformationService.findAll();
         assertNotNull(list);
    }


}
