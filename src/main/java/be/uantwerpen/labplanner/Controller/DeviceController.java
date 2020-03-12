package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Service.DeviceService;
import be.uantwerpen.labplanner.Service.DeviceTypeService;
import be.uantwerpen.labplanner.common.model.stock.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sun.nio.ch.IOUtil;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.Optional;


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
    public String viewDeviceInfo(@PathVariable Long id, final ModelMap model) throws IOException {
        model.addAttribute("device",deviceService.findById(id).orElse(null));
        byte[] image = deviceService.findById(id).get().getDeviceType().getDevicePicture();
        if(image == null  ){
            String fileName = "static/image/placeholder.jpg";
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            File file = new File(classLoader.getResource(fileName).getFile());
            BufferedImage bImage = ImageIO.read(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "jpg", bos );
            byte [] data = bos.toByteArray();
            byte[] encode = Base64.getEncoder().encode(data);
            model.addAttribute("image", new String(encode, "UTF-8"));
        }else{
            byte[] encode = Base64.getEncoder().encode(image);
            model.addAttribute("image", new String(encode, "UTF-8"));
        }
        return "/Devices/device-info";
    }
}
