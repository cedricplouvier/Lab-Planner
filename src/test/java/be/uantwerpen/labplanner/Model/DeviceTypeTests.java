package be.uantwerpen.labplanner.Model;


import be.uantwerpen.labplanner.Exception.StorageException;
import be.uantwerpen.labplanner.LabplannerApplication;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class DeviceTypeTests {

    private DeviceType deviceType;

    @Test
    public void TestDevice() throws Exception{
        deviceType = new DeviceType();

        assertEquals(deviceType.getDeviceTypeName(),DeviceType.getDefaultDevicetypename());
        assertNull(deviceType.getColor());
        assertNull(deviceType.getId());
        assertFalse(deviceType.getOvernightuse());
        assertNull(deviceType.getDevicePictureName());
        assertNull(deviceType.getDeviceInformation());



        String name = "name";
        deviceType.setDeviceTypeName(name);
        assertEquals(deviceType.getDeviceTypeName(), name);

        String imagename = "image";
        deviceType.setDevicePictureName(imagename);
        assertEquals(deviceType.getDevicePictureName(), imagename);

        deviceType.setOvernightuse(true);
        assertEquals(deviceType.getOvernightuse(),true);

        assertEquals(Device.getDefaultDevicename(),"default_devicename");
        Long id = (long) 5;
        deviceType.setId(id);
        assertEquals(deviceType.getId(),id);

        Files.createDirectories(Paths.get("upload-dir/" + DeviceType.getDefaultDevicetypename()));

        thrown.expect(StorageException.class);
        thrown.expectMessage("Could not initialize storage");

        // act
        new DeviceType();

        deviceType.setDevicePictureName("test");
        deviceType.setDeviceTypeName("newname");
        assertEquals(deviceType.getDeviceTypeName(),"newname");

    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();


}
