package be.uantwerpen.labplanner.Model;


import be.uantwerpen.labplanner.Exception.StorageException;
import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceInformation;
import be.uantwerpen.labplanner.Model.DeviceType;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class DeviceInformationTests {

    private DeviceInformation deviceInformation;

    @Test
    public void TestDevice() throws Exception{
        deviceInformation = new DeviceInformation();

        assertEquals(deviceInformation.getInformationName(),DeviceInformation.getDefaultInformationName());
        assertEquals(deviceInformation.getInformation(),DeviceInformation.getDefaultInformationName());
        assertNull(deviceInformation.getId());
        assertEquals(deviceInformation.getFiles(),new ArrayList<String>());




        String name = "name";
        deviceInformation.setInformationName(name);
        assertEquals(deviceInformation.getInformationName(), name);

        String info = "info";
        deviceInformation.setInformation(info);
        assertEquals(deviceInformation.getInformation(), info);

        List files = new ArrayList();
        files.add("1");
        files.add("2");
        deviceInformation.setFiles(files);
        assertEquals(deviceInformation.getFiles(),files);

        assertEquals(Device.getDefaultDevicename(),"default_devicename");
        Long id = (long) 5;
        deviceInformation.setId(id);
        assertEquals(deviceInformation.getId(),id);




    }


}
