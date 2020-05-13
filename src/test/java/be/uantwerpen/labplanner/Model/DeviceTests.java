package be.uantwerpen.labplanner.Model;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.stock.Tag;
import be.uantwerpen.labplanner.common.model.stock.Unit;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class DeviceTests {

    private Device device;

    @Test
    public void TestDevice() throws Exception{
        device = new Device();

        assertEquals(device.getDevicename(),Device.getDefaultDevicename());
        assertNull(device.getDeviceType());
        assertNull(device.getId());
        assertNull(device.getComment());




        String name = "name";
        device.setDevicename(name);
        assertEquals(device.getDevicename(), name);

        String comment = "comment";
        device.setComment(name);
        assertEquals(device.getDevicename(), name);

        DeviceType deviceType = new DeviceType();
        device.setDeviceType(deviceType);
        assertEquals(deviceType.getDeviceTypeName(),deviceType.getDeviceTypeName());

        assertEquals(Device.getDefaultDevicename(),"default_devicename");
        Long id = (long) 5;
        device.setId(id);
        assertEquals(device.getId(),id);

    }
}
