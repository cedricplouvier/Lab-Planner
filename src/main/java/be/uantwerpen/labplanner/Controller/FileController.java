package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Exception.StorageFileNotFoundException;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Model.Mixture;
import be.uantwerpen.labplanner.Model.OwnProduct;
import be.uantwerpen.labplanner.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.ResourceBundle;


@Controller
public class FileController {
	@Autowired
	private StorageService storageService;
	@Autowired
	private DeviceInformationService deviceInformationService;
	@Autowired
	private DeviceTypeService deviceTypeService;
	@Autowired
	private OwnProductService productService;
	@Autowired
	private MixtureService mixtureService;
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

	@PostMapping("/upload/typeimage/{typeid}")
	public String handleTypeImageUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, @PathVariable Long typeid) {
		DeviceType tempDeviceType =  deviceTypeService.findById( typeid).orElse(null);
		if(tempDeviceType!=null) {
			String filename = tempDeviceType.getDeviceTypeName()+"."+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
			storageService.store(file,"images",filename);
			tempDeviceType.setDevicePictureName(filename);
			deviceTypeService.saveNewDeviceType(tempDeviceType);

		}else{
		}

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
		storageService.store(file,deviceTypeService.findById(typeid).orElse(null).getDeviceTypeName(),file.getOriginalFilename());
		deviceInformationService.addFile(file.getOriginalFilename(),infoid);
		redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
		return "redirect:/devices/info/"+infoid+"/"+typeid;
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}


	@PostMapping("/upload/productimage/{productId}")
	public String handleProductImageUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, @PathVariable Long productId) {
		OwnProduct temp =  productService.findById( productId).orElse(null);
		if(temp!=null) {
			//append productid to filename to make sure file is unique for each product
			String filename = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)+productId.toString();
			storageService.store(file,"images",filename);
			temp.setImageName(filename);
			productService.save(temp);

		}else{
		}

		return "redirect:/products/"+productId;
	}

	@PostMapping("/upload/mixtureImage/{mixtureId}")
	public String handleMixtureImageUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, @PathVariable Long mixtureId) {
		Mixture temp =  mixtureService.findById( mixtureId).orElse(null);
		if(temp!=null) {
			//append productid to filename to make sure file is unique for each product
			String filename = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)+mixtureId.toString();
			storageService.store(file,"images",filename);
			temp.setImage(filename);
			mixtureService.save(temp);

		}else{
		}

		return "redirect:/mixtures/"+mixtureId;
	}

		//when upload is too big.
	@ControllerAdvice
	public class FileUploadExceptionAdvice {

		@ExceptionHandler(MaxUploadSizeExceededException.class)
		public String handleMaxSizeException(
				MaxUploadSizeExceededException exc,
				HttpServletRequest request,
				HttpServletResponse response) {
			Locale current = LocaleContextHolder.getLocale();
			FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);

			if (outputFlashMap != null){
				outputFlashMap.put("error", ResourceBundle.getBundle("messages",current).getString("valid.size"));

			}
			return "redirect:/products";


		}
	}

}
