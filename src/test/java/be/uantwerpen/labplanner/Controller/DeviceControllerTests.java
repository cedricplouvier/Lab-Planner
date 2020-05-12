package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.*;
import be.uantwerpen.labplanner.Service.*;
import be.uantwerpen.labplanner.common.model.users.Privilege;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import be.uantwerpen.labplanner.common.service.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.InstanceOf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class DeviceControllerTests {

    @Mock
    private DeviceService deviceService;

    @Mock
    private DeviceTypeService deviceTypeService;

    @Mock
    private DeviceInformationService deviceInformationService;
    @Mock
    private StorageService storageService;
    @Mock
    private StepService stepService;
    @Mock
    private StepTypeService stepTypeService;
    @InjectMocks
    private DeviceController deviceController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(this.deviceController).build();
    }

    @Test
    //View Role list
    public void ViewDeviceListTest() throws Exception{
        Device device = new Device();
        device.setDevicename("device");
        //Set variables
        device.setComment("test");

        DeviceType d1 = new DeviceType("devicetype",false);
        deviceTypeService.save(d1);
        device.setDeviceType(d1);
        List<Device> devices = new ArrayList<Device>();
        devices.add(device);

        when(deviceService.findAll()).thenReturn(devices);
        mockMvc.perform(get("/devices"))
                .andExpect(status().isOk())
                .andExpect(view().name("Devices/list-devices"))
                .andExpect(model().attribute("allDevices", hasSize(1)));
    }

    @Test
    //View device type list
    public void ViewDeviceTypeListTest() throws Exception{
        //create Device
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("devicetype");
        //Set variables
        deviceType.setColor("test");
        deviceType.setOvernightuse(true);
        deviceType.setDevicePictureName("picturename");
        List<DeviceType> devicetypes = new ArrayList<DeviceType>();
        devicetypes.add(deviceType);

        when(deviceTypeService.findAll()).thenReturn(devicetypes);
        mockMvc.perform(get("/devices/types"))
                .andExpect(status().isOk())
                .andExpect(view().name("Devices/list-device-types"))
                .andExpect(model().attribute("allDeviceTypes", hasSize(1)));
    }



    @Test
    //View device information page
    public void ViewDeviceInformationTest() throws Exception{
        DeviceInformation deviceInformation = new DeviceInformation();
        deviceInformation.setInformationName("deviceInformation2");
        //Set variables
        deviceInformation.setInformation("test");
        List files = new ArrayList();
        files.add("1");
        files.add("2");
        deviceInformation.setFiles(files);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("devicetype2");
        List deviceinformations = new ArrayList();
        deviceinformations.add(deviceInformation);
        deviceType.setDeviceInformation(deviceinformations);
        //Set variables
        deviceType.setColor("test");
        deviceType.setOvernightuse(true);
        deviceType.setDevicePictureName("picturename");
        Device device = new Device();
        device.setDevicename("device2");
        //Set variables
        device.setComment("test");
        device.setId((long) 10);
        device.setDeviceType(deviceType);
        List<Device> devices = new ArrayList<Device>();
        devices.add(device);

        when(deviceService.findById((long) 10)).thenReturn(Optional.of(device));
        mockMvc.perform(get("/device/info/{id}",10))
                .andExpect(status().isOk())
                .andExpect(view().name("Devices/device-info"))
                .andExpect(model().attribute("device", device));
    }

    @Test
    // Show device creation page test
    public void ViewCreateDeviceTest() throws Exception{
        //create Device
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("devicetype");
        //Set variables
        deviceType.setColor("test");
        deviceType.setOvernightuse(true);
        deviceType.setDevicePictureName("picturename");
        List<DeviceType> devicetypes = new ArrayList<DeviceType>();
        devicetypes.add(deviceType);

        when(deviceTypeService.findAll()).thenReturn(devicetypes);
        mockMvc.perform(get("/devices/put"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allDeviceTypes",hasSize(1)))
                .andExpect(model().attribute("device",instanceOf(Device.class)))
                .andExpect(view().name("Devices/device-manage"));
    }

    @Test
    // Show devicetype creation page test
    public void ViewCreateDeviceTypeTest() throws Exception{
        //create Device
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("devicetype");
        //Set variables
        deviceType.setColor("test");
        deviceType.setOvernightuse(true);
        deviceType.setDevicePictureName("picturename");
        List<DeviceType> devicetypes = new ArrayList<DeviceType>();
        devicetypes.add(deviceType);


        when(deviceTypeService.findAll()).thenReturn(devicetypes);

        mockMvc.perform(get("/devices/types/put"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allDeviceTypes",hasSize(1)))
                .andExpect(view().name("Devices/device-type-manage"));

    }



    @Test
    // Show device information creation page test
    public void ViewCreateDeviceInformationTest() throws Exception{
        //create Device
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("devicetype4");
        //Set variables
        deviceType.setColor("test");
        deviceType.setOvernightuse(true);
        deviceType.setDevicePictureName("picturename");
        deviceType.setId((long) 10);
        List<DeviceType> devicetypes = new ArrayList<DeviceType>();
        when(deviceTypeService.findById(deviceType.getId())).thenReturn(Optional.of(deviceType));
        mockMvc.perform(get("/devices/info/put/{id}",10))
                .andExpect(status().isOk())
                .andExpect(model().attribute("deviceInfoObject", instanceOf(DeviceInformation.class)))
                .andExpect(model().attribute("deviceTypeObject",deviceType))
                .andExpect(view().name("Devices/device-info-manage"));

    }





    @Test
    //TEst the validity of editing the device page
   public void viewEditDeviceTest() throws Exception {
        Device device = new Device();
        device.setDevicename("device");
        //Set variables
        device.setComment("test");

        DeviceType d1 = new DeviceType("devicetype",false);
        List<DeviceType> deviceTypes = new ArrayList<DeviceType>();
        deviceTypes.add(d1);
        device.setDeviceType(d1);
        device.setId((long) 10);
        List<Device> devices = new ArrayList<Device>();
        devices.add(device);

       when(deviceService.findById((long) 10)).thenReturn(Optional.of(device));
       when(deviceTypeService.findAll()).thenReturn(deviceTypes);

       //editing with existing id as input
       mockMvc.perform(get("/devices/{id}",10))
               .andExpect(status().isOk())
               .andExpect(model().attribute("allDeviceTypes",hasSize(1)))
               .andExpect(model().attribute("device",device))
               .andExpect(view().name("Devices/device-manage"))
                .andDo(print());

        //wrong input
       mockMvc.perform(get("/devices/{id}","fff"))
               .andExpect(status().is4xxClientError())
               .andDo(print());

   }




    @Test
    //TEst the validity of editing the deviceType page
    public void viewEditDeviceTypeTest() throws Exception {
        //create Device
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("devicetype");
        //Set variables
        deviceType.setColor("test");
        deviceType.setOvernightuse(true);
        deviceType.setDevicePictureName("picturename");
        deviceType.setId((long) 10);
        List<DeviceType> devicetypes = new ArrayList<DeviceType>();
        devicetypes.add(deviceType);

        when(deviceTypeService.findById((long) 10)).thenReturn(Optional.of(deviceType));

//        //editing with existing id as input
        mockMvc.perform(get("/devices/types/{id}",10))
                .andExpect(status().isOk())
                .andExpect(model().attribute("deviceTypeObject",instanceOf(DeviceType.class)))
                .andExpect(view().name("Devices/device-type-manage"))
                .andDo(print());

        //wrong input
        mockMvc.perform(get("/devices/types/{id}","fff"))
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }


    @Test
    //TEst the validity of editing the device Information page
    public void viewEditDeviceInformationTest() throws Exception {
        DeviceInformation deviceInformation = new DeviceInformation();
        deviceInformation.setInformationName("deviceInformation2");
        //Set variables
        deviceInformation.setInformation("test");
        List files = new ArrayList();
        files.add("1");
        files.add("2");
        deviceInformation.setFiles(files);
        DeviceType deviceType = new DeviceType();
        deviceType.setDeviceTypeName("devicetype2");
        List deviceinformations = new ArrayList();
        deviceinformations.add(deviceInformation);
        deviceType.setDeviceInformation(deviceinformations);
        //Set variables
        deviceType.setColor("test");
        deviceType.setOvernightuse(true);
        deviceType.setDevicePictureName("picturename");
        Device device = new Device();
        device.setDevicename("device2");
        //Set variables
        device.setComment("test");
        device.setId((long) 10);
        deviceInformation.setId((long) 10);
        deviceType.setId((long) 10);
        device.setDeviceType(deviceType);
        List<Device> devices = new ArrayList<Device>();
        devices.add(device);

        when(deviceTypeService.findById((long) 10)).thenReturn(Optional.of(deviceType));
        when(deviceInformationService.findById((long) 10)).thenReturn(Optional.of(deviceInformation));

        //editing with existing id as input
        mockMvc.perform(get("/devices/info/{id}/{typeid}",10,10))
                .andExpect(status().isOk())
                .andExpect(model().attribute("deviceInfoObject",deviceInformation))
                .andExpect(model().attribute("deviceTypeObject",deviceType))
                .andExpect(view().name("Devices/device-info-manage"))
                .andDo(print());

        //wrong input
        mockMvc.perform(get("/devices/info/{id}/{typeid}","fff","fff"))
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }





    @Test
    //test for deleting
    public void DeleteDeviceTest() throws Exception{
        Device device = new Device();
        device.setDevicename("device");
        //Set variables
        device.setComment("test");
        Device device2 = new Device();
        device2.setDevicename("devicetest");
        //Set variables
        device2.setComment("test");
        DeviceType d1 = new DeviceType("devicetype",false);
        List<DeviceType> deviceTypes = new ArrayList<DeviceType>();
        deviceTypes.add(d1);
        device.setDeviceType(d1);
        device.setId((long) 10);
        device2.setId((long) 12);
        List<Device> devices = new ArrayList<Device>();
        devices.add(device);
        devices.add(device2);
        when(stepService.findAll()).thenReturn(new ArrayList<>());
        //Device is in Use
        when(deviceService.findAll()).thenReturn(devices);
        when(deviceService.findById((long) 10)).thenReturn(Optional.of(device));
        when(deviceService.findById((long) 12)).thenReturn(Optional.of(device2));

        mockMvc.perform(get("/devices/{id}/delete",10))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(view().name("redirect:/devices"));

        //wrong url input
        mockMvc.perform(get("/usermanagement/roles/{id}/delete","ff"))
                .andExpect(status().is4xxClientError())
                .andDo(print());




    }

    @Test
    //test for deleting
    public void DeleteDeviceTypeTest() throws Exception{
        Device device = new Device();
        device.setDevicename("device");
        //Set variables
        device.setComment("test");

        DeviceType d1 = new DeviceType("devicetype",false);
        DeviceType d2 = new DeviceType("devicetype2",false);
        d2.setId((long) 11);

        List<DeviceType> deviceTypes = new ArrayList<DeviceType>();
        deviceTypes.add(d1);
        deviceTypes.add(d2);
        device.setDeviceType(d1);
        device.setId((long) 10);
        d1.setId((long) 10);
        List<Device> devices = new ArrayList<Device>();
        devices.add(device);

        //Role is in Use
        when(deviceService.findAll()).thenReturn(devices);
        when(deviceTypeService.findAll()).thenReturn(deviceTypes);
        when(deviceTypeService.findById((long) 10)).thenReturn(Optional.of(d1));
        when(stepTypeService.findAll()).thenReturn(new ArrayList<StepType>());
        mockMvc.perform(get("/devices/types/{id}/delete",10))
                .andExpect(status().is(200))
                .andDo(print())

                .andExpect(view().name("Errors/custom-error"));

        //Role is not in Use
        when(deviceTypeService.findById((long) 11)).thenReturn(Optional.of(d2));
        mockMvc.perform(get("/devices/types/{id}/delete",11))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(view().name("redirect:/devices"));

        //wrong url input
        mockMvc.perform(get("/devices/types/{id}/delete","ff"))
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }
    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value="/devices/info/{id}/{typeid}/delete")
    public String deleteDeviceInfo(@PathVariable Long id, final ModelMap model, @PathVariable Long typeid){
        DeviceType deviceType = deviceTypeService.findById(typeid).get();
        List<DeviceInformation> informations = deviceType.getDeviceInformation();
        informations.remove(deviceInformationService.findById(id).get());
        deviceType.setDeviceInformation(informations);
        deviceTypeService.saveNewDeviceType(deviceType);
        deviceInformationService.deleteById(id);
        model.clear();

        return "redirect:/devices/types/"+typeid;
    }

    @Test
    //test for deleting
    public void DeleteInformationTest() throws Exception{
        Device device = new Device();
        device.setDevicename("device");
        //Set variables
        device.setComment("test");

        DeviceType d1 = new DeviceType("devicetype",false);
        DeviceType d2 = new DeviceType("devicetype2",false);
        d2.setId((long) 11);

        List<DeviceType> deviceTypes = new ArrayList<DeviceType>();
        deviceTypes.add(d1);
        deviceTypes.add(d2);
        device.setDeviceType(d1);
        device.setId((long) 10);
        d1.setId((long) 10);
        List<Device> devices = new ArrayList<Device>();
        devices.add(device);
        DeviceInformation i1 = new DeviceInformation("info","information");
        List<DeviceInformation> deviceInformation = new ArrayList<DeviceInformation>();
        deviceInformation.add(i1);
        d1.setDeviceInformation(deviceInformation);
        i1.setId((long) 10);


        //Role is not in Use
        when(deviceTypeService.findAll()).thenReturn(deviceTypes);
        when(deviceInformationService.findById((long) 10)).thenReturn(Optional.of(i1));
        when(deviceTypeService.findById((long) 10)).thenReturn(Optional.of(d1));

        mockMvc.perform(get("/devices/info/{id}/{typeid}/delete",10,10))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(model().attributeDoesNotExist())
                .andExpect(view().name("redirect:/devices/types/10"));

        //wrong url input
        mockMvc.perform(get("/usermanagement/roles/{id}/delete","ff"))
                .andExpect(status().is4xxClientError())
                .andDo(print());




    }

    //add device tests
    @Test
    public void addCorrectDevicesTest() throws Exception{
        Device device = new Device();
        device.setDevicename("addDevice1");
        //Set variables
        device.setComment("devicecomment");

        DeviceType d1 = new DeviceType("addDeviceType1",false);
        device.setDeviceType(d1);
        d1.setId((long) 13);
        List<DeviceType> deviceTypes = new ArrayList<DeviceType>();
        deviceTypes.add(d1);

        when(deviceService.findByDevicename("addDevice1")).thenReturn(Optional.empty());
        when(deviceTypeService.findAll()).thenReturn(deviceTypes);
        mockMvc.perform(post("/devices/").flashAttr("device",device))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/devices"))
                .andDo(print());

        //When device already exists changes
        device.setId((long) 10);
        when(deviceService.findByDevicename("addDevice1")).thenReturn(Optional.of(device));
        when(deviceTypeService.findAll()).thenReturn(deviceTypes);
        mockMvc.perform(post("/devices/").flashAttr("device",device))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/devices"))
                .andDo(print());

    }

    @Test
    public void addFalseDevicesTest() throws Exception{

        DeviceType d1 = new DeviceType("addDeviceType3",false);
        d1.setId((long) 13);
        List<DeviceType> deviceTypes = new ArrayList<DeviceType>();
        deviceTypes.add(d1);

        // Add correct devicetype
        when(deviceTypeService.findById((long) 13)).thenReturn(Optional.of(d1));
        when(deviceTypeService.findByDevicetypeName("addDeviceType3")).thenReturn(Optional.of(d1));
        when(deviceTypeService.findAll()).thenReturn(deviceTypes);
        mockMvc.perform(post("/devices/types").flashAttr("deviceType",d1))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/devices/types/13"))
                .andDo(print());
    }

    @Test
    public void addCorrectDeviceTypesTest() throws Exception{
        Device device = new Device();
        device.setDevicename("addDevice1");
        //Set variables
        device.setComment("devicecomment");

        DeviceType d1 = new DeviceType("addDeviceType1",false);
        device.setDeviceType(d1);
        d1.setId((long) 13);
        List<DeviceType> deviceTypes = new ArrayList<DeviceType>();
        deviceTypes.add(d1);

        when(deviceService.findByDevicename("addDevice1")).thenReturn(Optional.empty());
        when(deviceTypeService.findAll()).thenReturn(deviceTypes);
        mockMvc.perform(post("/devices/").flashAttr("device",device))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/devices"))
                .andDo(print());

        //When device already exists changes
        device.setId((long) 10);
        when(deviceService.findByDevicename("addDevice1")).thenReturn(Optional.of(device));
        when(deviceTypeService.findAll()).thenReturn(deviceTypes);
        mockMvc.perform(post("/devices/").flashAttr("device",device))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/devices"))
                .andDo(print());

    }


    //add device tests
    @Test
    public void addCorrectDeviceInformationTest() throws Exception{
        DeviceType d1 = new DeviceType("devicetype",false);
        DeviceType d2 = new DeviceType("devicetype2",false);
        d2.setId((long) 11);

        List<DeviceType> deviceTypes = new ArrayList<DeviceType>();
        deviceTypes.add(d1);
        deviceTypes.add(d2);
        d1.setId((long) 10);
        DeviceInformation i1 = new DeviceInformation("info","information");
        List<DeviceInformation> deviceInformation = new ArrayList<DeviceInformation>();
        deviceInformation.add(i1);
        d1.setDeviceInformation(deviceInformation);
        i1.setId((long) 10);


        when(deviceInformationService.findAll()).thenReturn(deviceInformation);
        when(deviceInformationService.findById((long) 10)).thenReturn(Optional.of(i1));
        when(deviceTypeService.findById((long) 10)).thenReturn(Optional.of(d1));

        mockMvc.perform(post("/devices/info/{id}/{typeid}",10,10).flashAttr("deviceInformation",i1))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/devices/info/10/10"))
                .andDo(print());

    }
}
