package be.uantwerpen.labplanner.Controller;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

import be.uantwerpen.labplanner.Exception.StorageFileNotFoundException;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Service.DeviceInformationService;
import be.uantwerpen.labplanner.Service.DeviceTypeService;
import be.uantwerpen.labplanner.Service.FileSystemStorageService;
import be.uantwerpen.labplanner.Service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class FileController {
	@Autowired
	private StorageService storageService;
	@Autowired
	private DeviceInformationService deviceInformationService;
	@Autowired
	private DeviceTypeService deviceTypeService;
	@Autowired
	public FileController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/files/{dir}/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename,@PathVariable String dir) {
		Resource file = storageService.loadAsResource(dir+"/"+filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@PostMapping("/upload/TypeImage/{typeid}")
	public String handleTypeImageUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, @PathVariable Long typeid) {
//		if(storageService.getFileExtension(file.getOriginalFilename()).equals("png"))
		storageService.store(file,"images");
		DeviceType tempDeviceType =  deviceTypeService.findById( typeid).orElse(null);
		tempDeviceType.setDevicePictureName(file.getOriginalFilename());
		deviceTypeService.saveSomeAttributes(tempDeviceType);
		redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
		return "redirect:/devices/types/"+typeid;
	}

	@GetMapping("/file/delete/{typeid}/{infoid}/{filename}")
	public String handleFileDelete( @PathVariable Long infoid, RedirectAttributes redirectAttributes, @PathVariable Long typeid, @PathVariable String filename) {

		storageService.delete(deviceTypeService.findById(typeid).orElse(null).getDeviceTypeName()+"/"+filename);
		deviceInformationService.removeFile(filename,infoid);
		redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + filename+ "!");
		return "redirect:/devices/info/"+infoid+"/"+typeid;
	}
	@PostMapping("/upload/file/{typeid}/{infoid}")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable Long infoid, RedirectAttributes redirectAttributes, @PathVariable Long typeid) {
		storageService.store(file,deviceTypeService.findById(typeid).orElse(null).getDeviceTypeName());
		deviceInformationService.addFile(file.getOriginalFilename(),infoid);
		redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
		return "redirect:/devices/info/"+infoid+"/"+typeid;
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}
