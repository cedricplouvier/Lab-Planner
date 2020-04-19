package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceInformation;
import be.uantwerpen.labplanner.Model.DeviceType;
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
    @Autowired
    private StorageService storageService;
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
                .andExpect(view().name("/Devices/list-device-types"))
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

//        when(deviceService.findById((long) 10)).thenReturn(Optional.of(device));
//        mockMvc.perform(get("/device/info/{id}",10))
//                .andExpect(status().isOk())
//                .andExpect(view().name("Devices/device-info"))
//                .andExpect(model().attribute("device", device));
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

//        mockMvc.perform(get("/devices/info/put/{id}",10))
//                .andExpect(status().isOk())
//                .andExpect(model().attribute("deviceInfoObject", instanceOf(DeviceInformation.class)))
//                .andExpect(model().attribute("deviceTypeObject",Optional.of(deviceType)))
//                .andExpect(view().name("Devices/device-info-manage"));

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

        when(deviceTypeService.findAll()).thenReturn(devicetypes);

//        //editing with existing id as input
//        mockMvc.perform(get("/devices/types/{id}",10))
//                .andExpect(status().isOk())
//                .andExpect(model().attribute("deviceTypeObject",instanceOf(DeviceType.class)))
//                .andExpect(view().name("Devices/device-type-manage"))
//                .andDo(print());

        //wrong input
        mockMvc.perform(get("/devices/types/{id}","fff"))
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }


//    @Test
//    //TEst the validity of editing the device Information page
//    public void viewEditDeviceInformationTest() throws Exception {
//        Role role = new Role("testrol");
//        long id = 10;
//        role.setId(id);
//        List<Role> roles = new ArrayList<Role>();
//        roles.add(role);
//        Privilege p = new Privilege("test");
//        p.setId((long) 15);
//        List<Privilege> privileges = new ArrayList<Privilege>();
//        privileges.add(p);
//
//        when(roleService.findById(id)).thenReturn(Optional.of(role));
//        when(privilegeService.findAll()).thenReturn(privileges);
//
//        //editing with existing id as input
//        mockMvc.perform(get("/usermanagement/roles/{id}",10))
//                .andExpect(status().isOk())
//                .andExpect(model().attribute("allPrivileges",hasSize(1)))
//                .andExpect(view().name("/Roles/role-manage"))
//                .andDo(print());
//
//        //wrong input
//        mockMvc.perform(get("/usermanagement/roles/{id}","fff"))
//                .andExpect(status().is4xxClientError())
//                .andDo(print());
//
//    }
//
//
//
//    @PreAuthorize("hasAuthority('Device - Modify - All')")
//    @RequestMapping(value="/devices/info/{id}/{typeid}", method= RequestMethod.GET)
//    public String viewEdiDeviceInfo(@PathVariable Long id, @PathVariable Long typeid, final ModelMap model){
//        model.addAttribute("deviceInfoObject",deviceInformationService.findById(id).orElse(null));
//        model.addAttribute("deviceTypeObject",deviceTypeService.findById(typeid).orElse(null));
//        model.addAttribute("files", storageService.loadDir(Objects.requireNonNull(deviceTypeService.findById(typeid).orElse(null)).getDeviceTypeName()).map(
//                path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
//                        "serveFile", new String[]{ path.getFileName().toString(),path.getParent().toString()}).build().toUri().toString())
//                .collect(Collectors.toList()));
//        return "Devices/device-info-manage";
//    }
//   @Test
//    //Ad role with non valid name
//    public void AddNonValidNameRoleTest() throws Exception{
//       Role role = new Role("  ");
//       long id = 10;
//       role.setId(id);
//
//       Privilege p = new Privilege("test");
//       p.setId((long) 15);
//       List<Privilege> privileges = new ArrayList<Privilege>();
//       privileges.add(p);
//
//       when(privilegeService.findAll()).thenReturn(privileges);
//
//       //empty string name
//       mockMvc.perform(post("/usermanagement/roles/").flashAttr("role",role))
//               .andExpect(status().is(200))
//               .andExpect(model().attribute("roleInUse",notNullValue()))
//               .andExpect(view().name("/Roles/role-manage"))
//               .andDo(print());
//
//       //null as name
//       role.setName(null);
//       mockMvc.perform(post("/usermanagement/roles/").flashAttr("role",role))
//               .andExpect(status().is(200))
//               .andExpect(model().attribute("roleInUse",notNullValue()))
//               .andExpect(view().name("/Roles/role-manage"))
//               .andDo(print());
//   }
//
//
//
//    @PreAuthorize("hasAuthority('Device - Modify - All')")
//    @RequestMapping(value="/devices/{id}/delete")
//    public String deleteDevice(@PathVariable Long id, final ModelMap
//            model){ deviceService.deleteById(id);
//        model.clear();
//        return "redirect:/devices";
//    }
//
//    @Test
//    //test for deleting
//    public void DeleteDeviceTest() throws Exception{
//        Role role = new Role("testrole");
//        long id = 10;
//        role.setId(id);
//
//        Set<Role> roles = new HashSet<Role>();
//        roles.add(role);
//
//        User user = new User("admin","admin");
//        user.setRoles(roles);
//        List<User> users = new ArrayList<>();
//        users.add(user);
//
//        //Role is in Use
//        when(userService.findAll()).thenReturn(users);
//        mockMvc.perform(get("/usermanagement/roles/{id}/delete","10"))
//                .andExpect(status().is(200))
//                .andDo(print())
//                .andExpect(model().attribute("inUseError",notNullValue()))
//
//                .andExpect(view().name("/Roles/role-list"));
//
//        //Role is not in Use
//        when(userService.findAll()).thenReturn(users);
//        mockMvc.perform(get("/usermanagement/roles/{id}/delete","11"))
//                .andExpect(status().is(302))
//                .andDo(print())
//                .andExpect(model().attributeDoesNotExist())
//                .andExpect(view().name("redirect:/usermanagement/roles"));
//
//        //wrong url input
//        mockMvc.perform(get("/usermanagement/roles/{id}/delete","ff"))
//                .andExpect(status().is4xxClientError())
//                .andDo(print());
//
//
//
//
//    }
//
//    @PreAuthorize("hasAuthority('Device - Modify - All')")
//    @RequestMapping(value="/devices/types/{id}/delete")
//    public String deleteDeviceType(@PathVariable Long id, final ModelMap model){
//        List<Device> allDevices = deviceService.findAll();
//        Boolean isUsed = false;
//        for(Device currentDevice : allDevices){
//            if(currentDevice.getDeviceType().getId()==id){
//                isUsed = true;
//            }
//        }
//        if(isUsed){
//            model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
//            model.addAttribute("errormessage", "There are devices of type "+deviceTypeService.findById(id).orElse(null).getDeviceTypeName());
//            return "Devices/list-device-types";
//        }
//        deviceTypeService.deleteById(id);
//        model.clear();
//        return "redirect:/devices";
//    }
//
//
//    @Test
//    //test for deleting
//    public void DeleteDeviceTypeTest() throws Exception{
//        Role role = new Role("testrole");
//        long id = 10;
//        role.setId(id);
//
//        Set<Role> roles = new HashSet<Role>();
//        roles.add(role);
//
//        User user = new User("admin","admin");
//        user.setRoles(roles);
//        List<User> users = new ArrayList<>();
//        users.add(user);
//
//        //Role is in Use
//        when(userService.findAll()).thenReturn(users);
//        mockMvc.perform(get("/usermanagement/roles/{id}/delete","10"))
//                .andExpect(status().is(200))
//                .andDo(print())
//                .andExpect(model().attribute("inUseError",notNullValue()))
//
//                .andExpect(view().name("/Roles/role-list"));
//
//        //Role is not in Use
//        when(userService.findAll()).thenReturn(users);
//        mockMvc.perform(get("/usermanagement/roles/{id}/delete","11"))
//                .andExpect(status().is(302))
//                .andDo(print())
//                .andExpect(model().attributeDoesNotExist())
//                .andExpect(view().name("redirect:/usermanagement/roles"));
//
//        //wrong url input
//        mockMvc.perform(get("/usermanagement/roles/{id}/delete","ff"))
//                .andExpect(status().is4xxClientError())
//                .andDo(print());
//
//
//
//
//    }
//    @PreAuthorize("hasAuthority('Device - Modify - All')")
//    @RequestMapping(value="/devices/info/{id}/{typeid}/delete")
//    public String deleteDeviceInfo(@PathVariable Long id, final ModelMap model, @PathVariable Long typeid){
//        DeviceType deviceType = deviceTypeService.findById(typeid).get();
//        List<DeviceInformation> informations = deviceType.getDeviceInformation();
//        informations.remove(deviceInformationService.findById(id).get());
//        deviceType.setDeviceInformation(informations);
//        deviceTypeService.saveNewDeviceType(deviceType);
//        deviceInformationService.deleteById(id);
//        model.clear();
//
//        return "redirect:/devices/types/"+typeid;
//    }
//
//    @Test
//    //test for deleting
//    public void DeleteInformationTest() throws Exception{
//        Role role = new Role("testrole");
//        long id = 10;
//        role.setId(id);
//
//        Set<Role> roles = new HashSet<Role>();
//        roles.add(role);
//
//        User user = new User("admin","admin");
//        user.setRoles(roles);
//        List<User> users = new ArrayList<>();
//        users.add(user);
//
//        //Role is in Use
//        when(userService.findAll()).thenReturn(users);
//        mockMvc.perform(get("/usermanagement/roles/{id}/delete","10"))
//                .andExpect(status().is(200))
//                .andDo(print())
//                .andExpect(model().attribute("inUseError",notNullValue()))
//
//                .andExpect(view().name("/Roles/role-list"));
//
//        //Role is not in Use
//        when(userService.findAll()).thenReturn(users);
//        mockMvc.perform(get("/usermanagement/roles/{id}/delete","11"))
//                .andExpect(status().is(302))
//                .andDo(print())
//                .andExpect(model().attributeDoesNotExist())
//                .andExpect(view().name("redirect:/usermanagement/roles"));
//
//        //wrong url input
//        mockMvc.perform(get("/usermanagement/roles/{id}/delete","ff"))
//                .andExpect(status().is4xxClientError())
//                .andDo(print());
//
//
//
//
//    }
}
