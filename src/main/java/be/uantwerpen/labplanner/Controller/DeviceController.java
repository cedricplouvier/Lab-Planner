package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Service.DeviceService;
import be.uantwerpen.labplanner.Service.DeviceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class DeviceController {
    //Services
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceTypeService deviceTypeService;

    //Populate
    @ModelAttribute("allDevices")
    public Iterable<Device> populateDevices() {
        return this.deviceService.findAll();
    }
    @ModelAttribute("allDeviceTypes")
    public Iterable<DeviceType> populateDeviceTypes() { return this.deviceTypeService.findAll(); }

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
}
