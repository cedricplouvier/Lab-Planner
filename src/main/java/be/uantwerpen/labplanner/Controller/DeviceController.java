package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.*;
import be.uantwerpen.labplanner.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private StepService stepService;
    @Autowired
    private StepTypeService stepTypeService;
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
    public String showDevices(@ModelAttribute("deviceError") String deviceError, @ModelAttribute("MailSuccess") String MailSuccess, ModelMap model){
        if (!deviceError.trim().equals("")) {
            model.addAttribute(deviceError);
        }
        if (!MailSuccess.trim().equals("")){
            model.addAttribute(MailSuccess);
        }
        model.addAttribute("allDevices", deviceService.findAll());
        return "Devices/list-devices";
    }

    //List of device types
    @PreAuthorize("hasAuthority('Device - Read only - Basic') or hasAuthority('Device - Modify - All') ")
    @RequestMapping(value="/devices/types", method= RequestMethod.GET)
    public String showDeviceTypes(final ModelMap model){
        model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
        return "Devices/list-device-types";
    }

    //Info page for device
    @PreAuthorize("hasAuthority('Device - Read only - Basic') or hasAuthority('Device - Modify - All')")
    @RequestMapping(value ="/device/info/{id}", method= RequestMethod.GET)
    public String viewDeviceInfo(@PathVariable Long id, final ModelMap model) {
        Locale locale = LocaleContextHolder.getLocale();
        Device device = deviceService.findById(id).orElse(null);

        if(device==null){
            model.addAttribute("errorTitle", ResourceBundle.getBundle("messages",locale).getString("error.title.unknown.id"));
            model.addAttribute("errorMessage",ResourceBundle.getBundle("messages",locale).getString("error.device.unknown.id"));
            return "Errors/custom-error";
        }

        model.addAttribute("device",deviceService.findById(id).orElse(null));
        model.addAttribute("files", storageService.loadDir(deviceService.findById(id).orElse(null).getDevicename()).map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                        "serveFile", new String[]{ path.getFileName().toString(),path.getParent().toString()}).build().toUri().toString())
                .collect(Collectors.toList()));
        return "Devices/device-info";
    }

    //Edit
    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value="/devices/{id}", method= RequestMethod.GET)
    public String viewEdiDevice(@PathVariable Long id, final ModelMap model){
        Locale current = LocaleContextHolder.getLocale();
        Device device = deviceService.findById(id).orElse(null);
        if(device==null){
            model.addAttribute("deviceError",ResourceBundle.getBundle("messages",current).getString("error.device.unknown.id"));
            model.addAttribute("MailSuccess","");
            model.addAttribute("allDevices", deviceService.findAll());
            return "Devices/list-devices";
        }
        model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
        model.addAttribute("device",device);
        model.addAttribute("files", storageService.loadDir(Objects.requireNonNull(deviceService.findById(id).orElse(null)).getDevicename()).map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                        "serveFile", new String[]{ path.getFileName().toString(),path.getParent().toString()}).build().toUri().toString())
                .collect(Collectors.toList()));
        return "Devices/device-manage";
    }

    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value="/devices/types/{id}", method= RequestMethod.GET)
    public String viewEdiDeviceType(@PathVariable Long id, final ModelMap model){
        Locale current = LocaleContextHolder.getLocale();
        DeviceType deviceType = deviceTypeService.findById(id).orElse(null);
        if(deviceType==null){
            model.addAttribute("errormessage",ResourceBundle.getBundle("messages",current).getString("error.device.type.unknown.id"));
            model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
            return  "Devices/list-device-types";
        }
        model.addAttribute("deviceTypeObject",deviceType);
        return "Devices/device-type-manage";
    }

    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value="/devices/info/{id}/{deviceid}", method= RequestMethod.GET)
    public String viewEdiDeviceInfo(@PathVariable Long id, @PathVariable Long deviceid, final ModelMap model){
        Locale current = LocaleContextHolder.getLocale();
        DeviceInformation deviceInformation =deviceInformationService.findById(id).orElse(null);
        if(deviceInformation==null){
            model.addAttribute("deviceError",ResourceBundle.getBundle("messages",current).getString("error.device.information.unknown.id"));
            model.addAttribute("MailSuccess","");
            model.addAttribute("allDevices", deviceService.findAll());
            return "Devices/list-devices";        }
        Device device = deviceService.findById(deviceid).orElse(null);
        if(device==null){
            model.addAttribute("deviceError",ResourceBundle.getBundle("messages",current).getString("error.device.unknown.id"));
            model.addAttribute("MailSuccess","");
            model.addAttribute("allDevices", deviceService.findAll());
            return "Devices/list-devices";
        }
        model.addAttribute("deviceInfoObject",deviceInformation);
        model.addAttribute("deviceObject",deviceService.findById(deviceid).orElse(null));
        model.addAttribute("files", storageService.loadDir(Objects.requireNonNull(deviceService.findById(deviceid).orElse(null)).getDevicename()).map(
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
        model.addAttribute("files", null);
        return "Devices/device-manage";
    }

    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value="/devices/types/put", method= RequestMethod.GET)
    public String viewCreateDeviceType(final ModelMap model){
        model.addAttribute("deviceTypeObject",new DeviceType(DeviceType.getDefaultDevicetypename(),false));
        return "Devices/device-type-manage";
    }

    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value="/devices/info/put/{typeid}", method= RequestMethod.GET)
    public String viewCreateDeviceInfo(@Valid DeviceType deviceType, @PathVariable Long typeid, final ModelMap model){
        model.addAttribute("deviceInfoObject",new DeviceInformation(DeviceInformation.getDefaultInformationName(),""));
        model.addAttribute("deviceObject",deviceService.findById(typeid).orElse(null));
        model.addAttribute("files", storageService.loadDir(Objects.requireNonNull(deviceService.findById(typeid).orElse(null)).getDevicename()).map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                        "serveFile", new String[]{ path.getFileName().toString(),path.getParent().toString()}).build().toUri().toString())
                .collect(Collectors.toList()));
        return "Devices/device-info-manage";
    }

    //Save
    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value={"/devices/", "/devices/{id}"}, method= RequestMethod.POST)
    public String addDevice(@Valid Device device, BindingResult result, final ModelMap model){
        Locale current = LocaleContextHolder.getLocale();
        if(result.hasErrors()){
            model.addAttribute("deviceType", deviceTypeService.findAll());
            return "Devices/device-manage";
        }
        if(device.getDeviceType()==null){
            model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
            model.addAttribute("device",device);
            model.addAttribute("errormessage",ResourceBundle.getBundle("messages",current).getString("error.no.devicetype"));
            return "Devices/device-manage";
        }
        if(device.getDevicename().length()==0||Device.getDefaultDevicename().equals(device.getDevicename())){
            model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
            model.addAttribute("device",device);
            model.addAttribute("errormessage",ResourceBundle.getBundle("messages",current).getString("error.invalid.name"));
            return "Devices/device-manage";
        }
        Device tempDevice = deviceService.findByDevicename(device.getDevicename()).orElse(null);
        if(tempDevice!=null&&!device.getId().equals(tempDevice.getId())){
            model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
            model.addAttribute("device",device);
            model.addAttribute("errormessage",ResourceBundle.getBundle("messages",current).getString("error.name.inuse"));
            return "Devices/device-manage";
        }
        deviceService.saveNewDevice(device);
        return "redirect:/devices/"+device.getId();
    }

    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value={"/devices/info/{typeid}","/devices/info/{typeid}/{id}"}, method= RequestMethod.POST)
    public String addDeviceInfo(@Valid DeviceInformation deviceInformation, @PathVariable Long typeid, BindingResult result, final ModelMap model){
        Locale current = LocaleContextHolder.getLocale();

        if(deviceInformation.getInformationName().length()==0||DeviceInformation.getDefaultInformationName().equals(deviceInformation.getInformationName())){

            model.addAttribute("deviceInfoObject",deviceInformation);
            model.addAttribute("deviceObject",deviceService.findById(typeid).orElse(null));
            model.addAttribute("errormessage",ResourceBundle.getBundle("messages",current).getString("error.invalid.name"));
            return "Devices/device-info-manage";
        }

        if(result.hasErrors()){
            model.addAttribute("deviceInfoObject", deviceInformation);
            model.addAttribute("deviceObject",deviceService.findById(typeid).orElse(null));
            model.addAttribute("errormessage",ResourceBundle.getBundle("messages",current).getString("error.invalid.name"));
            return "Devices/device-info-manage";
        }
        deviceInformationService.saveNewDeviceInformation(deviceInformation,typeid);
        return "redirect:/devices/info/"+deviceInformationService.findById(deviceInformation.getId()).get().getId()+"/"+typeid;
    }

    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value={"/devices/types", "/devices/types/{id}"}, method= RequestMethod.POST)
    public String addDeviceType(@Valid DeviceType deviceType, BindingResult result, final ModelMap model){
        Locale current = LocaleContextHolder.getLocale();
        if(result.hasErrors()){
            model.addAttribute("devicetypes", deviceTypeService.findAll());
            return "Devices/device-type-manage";
        }

        if(deviceType.getDeviceTypeName().length()==0||DeviceType.getDefaultDevicetypename().equals(deviceType.getDeviceTypeName())){
            model.addAttribute("NameIsUsed",ResourceBundle.getBundle("messages",current).getString("error.invalid.name"));
            model.addAttribute("devicetypes", deviceTypeService.findAll());
            model.addAttribute("deviceTypeObject",deviceType);
            return "Devices/device-type-manage";
        }

        if(deviceType.getId()==null){
            if(deviceTypeService.findByDevicetypeName( deviceType.getDeviceTypeName()).orElse(null)!=null){
                model.addAttribute("NameIsUsed",ResourceBundle.getBundle("messages",current).getString("error.name.inuse"));
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
        if(tempDeviceType!=null&&!tempDeviceType.getDeviceTypeName().equals(deviceType.getDeviceTypeName())){
            if(deviceTypeService.findByDevicetypeName( deviceType.getDeviceTypeName()).orElse(null)!=null){
                model.addAttribute("NameIsUsed",ResourceBundle.getBundle("messages",current).getString("error.name.inuse"));
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
    public String deleteDevice(@PathVariable Long id, final ModelMap model){
        Locale current = LocaleContextHolder.getLocale();
        Device device = deviceService.findById(id).orElse(null);
        if(device==null){
            model.addAttribute("deviceError",ResourceBundle.getBundle("messages",current).getString("error.device.unknown.id"));
            model.addAttribute("MailSuccess","");
            model.addAttribute("allDevices", deviceService.findAll());
            return "Devices/list-devices";
        }
        List<Step> allSteps = stepService.findAll();
        Boolean isUsed = false;
        for(Step currentStep : allSteps){
            if(currentStep.getDevice().getId()==id){
                isUsed = true;
            }
        }
        if(isUsed){
            model.addAttribute("deviceError", ResourceBundle.getBundle("messages",current).getString("error.device.inuse"));
            model.addAttribute("MailSuccess","");
            model.addAttribute("allDevices", deviceService.findAll());
            return "Devices/list-devices";
        }
        List<DeviceInformation> informations = this.deviceService.findById(id).get().getDeviceInformation();
        if(informations!=null) {
            device.setDeviceInformation(new ArrayList<DeviceInformation>());
            deviceService.saveNewDevice(device);

            for (DeviceInformation information : informations) {
                deviceInformationService.deleteById(Objects.requireNonNull(information.getId()));
            }
        }
        deviceService.delete(id);
        model.clear();
        return "redirect:/devices";
    }

    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value="/devices/types/{id}/delete")
    public String deleteDeviceType(@PathVariable Long id, final ModelMap model){
        Locale current = LocaleContextHolder.getLocale();
        DeviceType deviceType = deviceTypeService.findById(id).orElse(null);
        if(deviceType==null){
            model.addAttribute("errormessage",ResourceBundle.getBundle("messages",current).getString("error.device.type.unknown.id"));
            model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
            return  "Devices/list-device-types";
        }
        List<Device> allDevices = deviceService.findAll();
        Boolean isUsed = false;
        for(Device currentDevice : allDevices){
            if(currentDevice.getDeviceType().getId()==id){
                isUsed = true;
            }
        }
        if(!isUsed) {
            List<StepType> allStepTypes = stepTypeService.findAll();
            for (StepType currentStepType : allStepTypes) {
                if (currentStepType.getDeviceType().getId() == id) {
                    isUsed = true;
                }
            }
        }
        if(isUsed){
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("error.device.type.inuse"));
            model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
            return  "Devices/list-device-types";
        }
        if(isUsed){
            model.addAttribute("allDeviceTypes", deviceTypeService.findAll());
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("error.device.type.inuse"));
            return "Devices/list-device-types";
        }
        deviceTypeService.deleteById(id);
        model.clear();
        return "redirect:/devices";
    }

    @PreAuthorize("hasAuthority('Device - Modify - All')")
    @RequestMapping(value="/devices/info/{id}/{deviceid}/delete")
    public String deleteDeviceInfo(@PathVariable Long id, final ModelMap model, @PathVariable Long deviceid){
        Device device = deviceService.findById(deviceid).get();
        List<DeviceInformation> informations = device.getDeviceInformations();
        informations.remove(deviceInformationService.findById(id).get());
        device.setDeviceInformations(informations);
        deviceService.saveNewDevice(device);
        deviceInformationService.deleteById(id);
        model.clear();

        return "redirect:/devices/"+deviceid;
    }
}
