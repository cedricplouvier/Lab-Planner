package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Service.DeviceService;
import be.uantwerpen.labplanner.Service.DeviceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;


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

    @RequestMapping(value ="/devices/info/{id}", method= RequestMethod.GET)
    public String viewDeviceInfo(@PathVariable Long id, final ModelMap model){
        model.addAttribute("device",deviceService.findById(id).orElse(null));
        return "/Devices/device-info";
    }
//    @RequestMapping(value = "/upload/db" ,method= RequestMethod.GET)
//    public ResponseEntity uploadToDB(@RequestParam("file") MultipartFile file) {
//        DeviceType deviceType = new DeviceType();
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        try {
//            deviceType.setDevicePicture(file.getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        documentDao.save(doc);
//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/files/download/")
//                .path(fileName).path("/db")
//                .toUriString();
//        return ResponseEntity.ok(fileDownloadUri);
//    }
}
