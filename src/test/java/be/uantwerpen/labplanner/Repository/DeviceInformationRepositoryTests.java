package be.uantwerpen.labplanner.Repository;


import be.uantwerpen.labplanner.LabplannerApplication;
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
public class DeviceInformationRepositoryTests {

    @Autowired
    private DeviceInformationRepository deviceInformationRepository;

    @Test
    public void testDeviceInformationSave(){
        //create DeviceInformation
        DeviceInformation deviceInformation = new DeviceInformation();
        deviceInformation.setInformationName("deviceInformation");
        //Set variables
        deviceInformation.setInformation("test");
        List files = new ArrayList();
        files.add("1");
        files.add("2");
        deviceInformation.setFiles(files);


        long precount = deviceInformationRepository.count();

        //save product & varify id
        assertNull(deviceInformation.getId());
        deviceInformationRepository.save(deviceInformation);
        assertNotNull(deviceInformation.getId());

        //retrieve Role from database.
        DeviceInformation fetchedDeviceInformation = deviceInformationRepository.findById(deviceInformation.getId()).orElse(null);
        assertNotNull(fetchedDeviceInformation);

        //and the fetched Role should equal the real Role
        assertEquals(fetchedDeviceInformation.getInformationName(),deviceInformation.getInformationName());
        assertEquals(fetchedDeviceInformation.getId(),deviceInformation.getId());
        assertEquals(fetchedDeviceInformation.getFiles().size(),files.size());
        assertEquals(fetchedDeviceInformation.getInformation(),deviceInformation.getInformation());


        //update name & desciption
        fetchedDeviceInformation.setInformationName("testname");
        List files2 = new ArrayList();
        files.add("3");
        files.add("4");
        deviceInformation.setFiles(files2);
        fetchedDeviceInformation.setInformation("newInfo");
        fetchedDeviceInformation.setFiles(files2);
        deviceInformationRepository.save(fetchedDeviceInformation);

        DeviceInformation fetchedUpdated = deviceInformationRepository.findById(fetchedDeviceInformation.getId()).orElse(null);


        assertEquals(fetchedUpdated.getInformationName(),fetchedDeviceInformation.getInformationName());
        assertEquals(fetchedUpdated.getId(),fetchedDeviceInformation.getId());
        assertEquals(fetchedUpdated.getFiles().size(),files2.size());
        assertEquals(fetchedUpdated.getInformation(),fetchedDeviceInformation.getInformation());


        assertEquals(deviceInformationRepository.count(),precount+1);

        int count = 0;
        for (DeviceInformation p : deviceInformationRepository.findAll()) {
            count++;
        }

        assertEquals(count,precount+1);

        //delete user and check if still in database
        deviceInformationRepository.deleteById(fetchedUpdated.getId());

        assertEquals(deviceInformationRepository.count(),precount);
        assertNull(deviceInformationRepository.findById(fetchedUpdated.getId()).orElse(null));

        //check for delete if nnot exists
        assertThrows(EmptyResultDataAccessException.class,()->{deviceInformationRepository.deleteById(fetchedUpdated.getId());});

    }




}
