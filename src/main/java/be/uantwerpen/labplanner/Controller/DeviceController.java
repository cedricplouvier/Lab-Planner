package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceInformation;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Service.DeviceInformationService;
import be.uantwerpen.labplanner.Service.DeviceService;
import be.uantwerpen.labplanner.Service.DeviceTypeService;
import be.uantwerpen.labplanner.Service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class DeviceController {
    //Services
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceTypeService deviceTypeService;
    @Autowired
    private DeviceInformationService deviceInformationService;
    @Autowired
    private StorageService storageService;

    //Populate
    @ModelAttribute("allDevices")
    public Iterable<Device> populateDevices() {
        return this.deviceService.findAll();
    }
    @ModelAttribute("allDeviceTypes")
    public Iterable<DeviceType> populateDeviceTypes() { return this.deviceTypeService.findAll(); }
    @ModelAttribute("allDeviceInformations")
    public Iterable<DeviceInformation> populateDeviceInformations() { return this.deviceInformationService.findAll(); }
    //Mappings

    //List of devices
    @PreAuthorize("hasAuthority('Device - Read only - Basic') or hasAuthority('Device - Modify - All')")
    @RequestMapping(value="/devices", method= RequestMethod.GET)
    public String showDevices(final ModelMap model){
        model.addAttribute("allDevices", deviceService.findAll());
        return "Devices/list-devices";
    }

    //List of device types
    @PreAuthorize("hasAuthority('Device - Read only - Basic') or hasAuthority('Device - Modify - All') ")
    @RequestMapping(value="/devices/types", method= RequestMethod.GET)
    public String showDeviceTypes(final ModelMap model){
        model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
        return "/Devices/list-device-types";
    }

    //Info page for device
    @PreAuthorize("hasAuthority('Device - Read only - Basic') or hasAuthority('Device - Modify - All')")
    @RequestMapping(value ="/device/info/{id}", method= RequestMethod.GET)
    public String viewDeviceInfo(@PathVariable Long id, final ModelMap model) throws IOException {
        model.addAttribute("device",deviceService.findById(id).orElse(null));
        model.addAttribute("files", storageService.loadDir(deviceService.findById(id).orElse(null).getDeviceType().getDeviceTypeName()).map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                        "serveFile", new String[]{ path.getFileName().toString(),path.getParent().toString()}).build().toUri().toString())
                .collect(Collectors.toList()));
        return "Devices/device-info";
    }

    //Edit
    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value="/devices/{id}", method= RequestMethod.GET)
    public String viewEdiDevice(@PathVariable Long id, final ModelMap model){
        model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
        model.addAttribute("device",deviceService.findById(id).orElse(null));
        return "Devices/device-manage";
    }

    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value="/devices/types/{id}", method= RequestMethod.GET)
    public String viewEdiDeviceType(@PathVariable Long id, final ModelMap model){
        model.addAttribute("deviceTypeObject",deviceTypeService.findById(id).orElse(null));
        model.addAttribute("files", storageService.loadDir(Objects.requireNonNull(deviceTypeService.findById(id).orElse(null)).getDeviceTypeName()).map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                        "serveFile", new String[]{ path.getFileName().toString(),path.getParent().toString()}).build().toUri().toString())
                .collect(Collectors.toList()));

        return "Devices/device-type-manage";
    }

    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value="/devices/info/{id}/{typeid}", method= RequestMethod.GET)
    public String viewEdiDeviceInfo(@PathVariable Long id, @PathVariable Long typeid, final ModelMap model){
        model.addAttribute("deviceInfoObject",deviceInformationService.findById(id).orElse(null));
        model.addAttribute("deviceTypeObject",deviceTypeService.findById(typeid).orElse(null));
        model.addAttribute("files", storageService.loadDir(Objects.requireNonNull(deviceTypeService.findById(typeid).orElse(null)).getDeviceTypeName()).map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                        "serveFile", new String[]{ path.getFileName().toString(),path.getParent().toString()}).build().toUri().toString())
                .collect(Collectors.toList()));
        return "Devices/device-info-manage";
    }

    //Create new

    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value="/devices/put", method= RequestMethod.GET)
    public String viewCreateDevice(final ModelMap model){
        model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
        model.addAttribute("device",new Device(Device.getDefaultDevicename(),deviceTypeService.findAll().get(0)));
        return "Devices/device-manage";
    }

    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value="/devices/types/put", method= RequestMethod.GET)
    public String viewCreateDeviceType(final ModelMap model){
        model.addAttribute("deviceTypeObject",new DeviceType(DeviceType.getDefaultDevicetypename(),false));
        model.addAttribute("files", null);
        return "Devices/device-type-manage";
    }

    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value="/devices/info/put/{typeid}", method= RequestMethod.GET)
    public String viewCreateDeviceInfo(@Valid DeviceType deviceType, @PathVariable Long typeid, final ModelMap model){
        model.addAttribute("deviceInfoObject",new DeviceInformation(DeviceInformation.getDefaultInformationName(),""));
        model.addAttribute("deviceTypeObject",deviceTypeService.findById(typeid).orElse(null));
        model.addAttribute("files", storageService.loadDir(Objects.requireNonNull(deviceTypeService.findById(typeid).orElse(null)).getDeviceTypeName()).map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                        "serveFile", new String[]{ path.getFileName().toString(),path.getParent().toString()}).build().toUri().toString())
                .collect(Collectors.toList()));
        return "Devices/device-info-manage";
    }

    //Save

    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value={"/devices/", "/devices/{id}"}, method= RequestMethod.POST)
    public String addDevice(@Valid Device device, BindingResult result, final ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("deviceType", deviceTypeService.findAll());
            return "Devices/device-manage";
        }
        Device tempDevice = deviceService.findByDevicename(device.getDevicename()).orElse(null);
        if(tempDevice!=null&&!device.getId().equals(tempDevice.getId())){
            model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
            model.addAttribute("device",device);
            model.addAttribute("errormessage","The name "+device.getDevicename()+" is already used");
            return "Devices/device-manage";
        }
        deviceService.saveNewDevice(device);
        return "redirect:/devices";
    }

    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value={"/devices/info","/devices/info/{id}/{typeid}"}, method= RequestMethod.POST)
    public String addDeviceInfo(@Valid DeviceInformation deviceInformation, @PathVariable Long typeid, BindingResult result, final ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("deviceInfoObject", deviceInformationService.findAll());
            return "Devices/device-info-manage";
        }
        deviceInformationService.saveNewDeviceInformation(deviceInformation,typeid);
        return "redirect:/devices/info/"+deviceInformationService.findByInforationName(deviceInformation.getInformationName()).get().getId()+"/"+typeid;
    }

    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value={"/devices/types", "/devices/types/{id}"}, method= RequestMethod.POST)
    public String addDeviceType(@Valid DeviceType deviceType, BindingResult result, final ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("devicetypes", deviceTypeService.findAll());
            return "Devices/device-type-manage";
        }
        if(deviceType.getId()==null){
            if(deviceTypeService.findByDevicetypeName( deviceType.getDeviceTypeName()).orElse(null)!=null){
                model.addAttribute("NameIsUsed","The name "+deviceType.getDeviceTypeName()+" is already used");
                model.addAttribute("devicetypes", deviceTypeService.findAll());
                model.addAttribute("deviceTypeObject",deviceType);
                return "Devices/device-type-manage";
            }else{
                deviceTypeService.saveNewDeviceType(deviceType);
                return "redirect:/devices/types/"+deviceTypeService.findByDevicetypeName(deviceType.getDeviceTypeName()).get().getId();
            }
        }
        //Check if name is not already used else return to the edit page
        DeviceType tempDeviceType = deviceTypeService.findById(deviceType.getId()).orElse(null);
        if(!tempDeviceType.getDeviceTypeName().equals(deviceType.getDeviceTypeName())){
            if(deviceTypeService.findByDevicetypeName( deviceType.getDeviceTypeName()).orElse(null)!=null){
                model.addAttribute("NameIsUsed","The name "+deviceType.getDeviceTypeName()+" is already used");
                model.addAttribute("devicetypes", deviceTypeService.findAll());
                model.addAttribute("deviceTypeObject",tempDeviceType);
                return "Devices/device-type-manage";
            }
        }
        deviceTypeService.saveNewDeviceType(deviceType);
        return "redirect:/devices/types/"+deviceTypeService.findByDevicetypeName(deviceType.getDeviceTypeName()).get().getId();
    }

    //Delete

    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value="/devices/{id}/delete")
    public String deleteDevice(@PathVariable Long id, final ModelMap
            model){ deviceService.deleteById(id);
        model.clear();
        return "redirect:/devices";
    }

    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value="/devices/types/{id}/delete")
    public String deleteDeviceType(@PathVariable Long id, final ModelMap model){
        List<Device> allDevices = deviceService.findAll();
        Boolean isUsed = false;
        for(Device currentDevice : allDevices){
            if(currentDevice.getDeviceType().getId()==id){
                isUsed = true;
            }
        }
        if(isUsed){
            model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
            model.addAttribute("errormessage", "There are devices of type "+deviceTypeService.findById(id).orElse(null).getDeviceTypeName());
            return "Devices/list-device-types";
        }
        deviceTypeService.deleteById(id);
        model.clear();
        return "redirect:/devices";
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
}
