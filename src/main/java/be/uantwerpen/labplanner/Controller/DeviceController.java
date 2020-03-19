package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceInformation;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Service.DeviceInformationService;
import be.uantwerpen.labplanner.Service.DeviceService;
import be.uantwerpen.labplanner.Service.DeviceTypeService;
import be.uantwerpen.labplanner.Service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
    @RequestMapping(value="/devices", method= RequestMethod.GET)
    public String showDevices(final ModelMap model){
        model.addAttribute("allDevices", deviceService.findAll());
        return "/Devices/list-devices";
    }

    @RequestMapping(value="/devices/types", method= RequestMethod.GET)
    public String showDeviceTypes(final ModelMap model){
        model.addAttribute("allDeviceTypes", deviceTypeService.findAll()); return "/Devices/list-device-types";
    }

    @RequestMapping(value ="/device/info/{id}", method= RequestMethod.GET)
    public String viewDeviceInfo(@PathVariable Long id, final ModelMap model) throws IOException {
        model.addAttribute("device",deviceService.findById(id).orElse(null));
        model.addAttribute("files", storageService.loadDir(deviceService.findById(id).orElse(null).getDeviceType().getDeviceTypeName()).map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                        "serveFile", new String[]{ path.getFileName().toString(),path.getParent().toString()}).build().toUri().toString())
                .collect(Collectors.toList()));
        return "/Devices/device-info";
    }
    @RequestMapping(value="/devices/{id}", method= RequestMethod.GET)
    public String viewEdiDevice(@PathVariable Long id, final ModelMap model){
        model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
        model.addAttribute("device",deviceService.findById(id).orElse(null));
        return "/Devices/device-manage";
    }
    @RequestMapping(value="/devices/types/{id}", method= RequestMethod.GET)
    public String viewEdiDeviceType(@PathVariable Long id, final ModelMap model){
        model.addAttribute("deviceTypeObject",deviceTypeService.findById(id).orElse(null));
        model.addAttribute("files", storageService.loadDir(Objects.requireNonNull(deviceTypeService.findById(id).orElse(null)).getDeviceTypeName()).map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                        "serveFile", new String[]{ path.getFileName().toString(),path.getParent().toString()}).build().toUri().toString())
                .collect(Collectors.toList()));

        return "/Devices/device-type-manage";
    }
    @RequestMapping(value="/devices/info/{id}/{typeid}", method= RequestMethod.GET)
    public String viewEdiDeviceInfo(@PathVariable Long id, @PathVariable Long typeid, final ModelMap model){
        model.addAttribute("deviceInfoObject",deviceInformationService.findById(id).orElse(null));
        model.addAttribute("deviceTypeObject",deviceTypeService.findById(typeid).orElse(null));
        return "/Devices/device-info-manage";
    }
    @RequestMapping(value="/devices/put", method= RequestMethod.GET)
    public String viewCreateDevice(final ModelMap model){
        model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
        model.addAttribute("device",new Device(Device.getDefaultDevicename(),deviceTypeService.findAll().get(0)));
        return "/Devices/device-manage";
    }
    @RequestMapping(value="/devices/types/put", method= RequestMethod.GET)
    public String viewCreateDeviceType(final ModelMap model){
        model.addAttribute("deviceTypeObject",new DeviceType(DeviceType.getDefaultDevictypename(),false));
        model.addAttribute("files", null);
        return "/Devices/device-type-manage";
    }
    @RequestMapping(value="/devices/info/put/{typeid}", method= RequestMethod.GET)
    public String viewCreateDeviceInfo( @PathVariable Long typeid, final ModelMap model){
        model.addAttribute("deviceInfoObject",new DeviceInformation(DeviceInformation.getDefaultInformationName(),""));
        model.addAttribute("deviceTypeObject",deviceTypeService.findById(typeid).orElse(null));
        return "/Devices/device-info-manage";
    }
    @RequestMapping(value={"/devices/", "/devices/{id}"}, method= RequestMethod.POST)
    public String addDevice(@Valid Device device, BindingResult result, final ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("deviceType", deviceTypeService.findAll());
            return "/Devices/device-manage";
        }
        Device tempDevice = deviceService.findByDevicename( device.getDevicename()).orElse(null);
        if(tempDevice!=null&&device.getId()!=tempDevice.getId()){
            model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
            model.addAttribute("device",device);
            model.addAttribute("errormessage","The name "+device.getDevicename()+" is already used");
            return "/Devices/device-manage";
        }
        deviceService.saveSomeAttributes(device);
        return "redirect:/devices";
    }

    @RequestMapping(value="/devices/{id}/delete")
    public String deleteDevice(@PathVariable Long id, final ModelMap
            model){ deviceService.delete(id);
        model.clear();
        return "redirect:/devices";
    }
    @RequestMapping(value={"/devices/info","/devices/info/{id}/{typeid}"}, method= RequestMethod.POST)
    public String addDeviceInfo(@Valid DeviceInformation deviceInformation, @PathVariable Long typeid, BindingResult result, final ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("deviceInfoObject", deviceInformationService.findAll());
            return "/Devices/device-info-manage";
        }
        deviceInformationService.saveSomeAttributes(deviceInformation,typeid);
        return "redirect:/devices/types/{typeid}";
    }

    @RequestMapping(value={"/devices/types", "/devices/types/{id}"}, method= RequestMethod.POST)
    public String addDeviceType(@Valid DeviceType deviceType, BindingResult result, final ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("devicetypes", deviceTypeService.findAll());
            return "/Devices/device-type-manage";
        }
        if(deviceType.getId()==null){
            if(deviceTypeService.findByDevicetypeName( deviceType.getDeviceTypeName()).orElse(null)!=null){
                model.addAttribute("NameIsUsed","The name "+deviceType.getDeviceTypeName()+" is already used");
                model.addAttribute("devicetypes", deviceTypeService.findAll());
                model.addAttribute("deviceTypeObject",deviceType);
                return "/Devices/device-type-manage";
            }else{
                deviceTypeService.saveSomeAttributes(deviceType);
                return "redirect:/devices/types";
            }
        }
        //Check if name is not already used else return to the edit page
        DeviceType tempDeviceType = deviceTypeService.findById(deviceType.getId()).orElse(null);
        if(!tempDeviceType.getDeviceTypeName().equals(deviceType.getDeviceTypeName())){
            if(deviceTypeService.findByDevicetypeName( deviceType.getDeviceTypeName()).orElse(null)!=null){
                model.addAttribute("NameIsUsed","The name "+deviceType.getDeviceTypeName()+" is already used");
                model.addAttribute("devicetypes", deviceTypeService.findAll());
                model.addAttribute("deviceTypeObject",tempDeviceType);
                return "/Devices/device-type-manage";
            }
        }
        deviceTypeService.saveSomeAttributes(deviceType);
        return "redirect:/devices/types";
    }
}
