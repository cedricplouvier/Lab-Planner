package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Service.DeviceService;
import be.uantwerpen.labplanner.Service.DeviceTypeService;
import be.uantwerpen.labplanner.Service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
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
    private StorageService storageService;

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

    @RequestMapping(value ="/devices/info/{id}", method= RequestMethod.GET)
    public String viewDeviceInfo(@PathVariable Long id, final ModelMap model) throws IOException {
        model.addAttribute("device",deviceService.findById(id).orElse(null));
        model.addAttribute("files", storageService.loadDir(deviceService.findById(id).orElse(null).getDeviceType().getDeviceTypeName()).map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                        "serveFile", new String[]{ path.getFileName().toString(),path.getParent().toString()}).build().toUri().toString())
                .collect(Collectors.toList()));
        return "/Devices/device-info";
    }
}
