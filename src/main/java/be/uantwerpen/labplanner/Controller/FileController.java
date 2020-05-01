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
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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


	@PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
	@PostMapping("/upload/productimage/{productId}")
	public String handleProductImageUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, @PathVariable Long productId) throws Exception{
		OwnProduct temp =  productService.findById( productId).orElse(null);
		Locale current = LocaleContextHolder.getLocale();

		if(temp!=null) {
			//append productid to filename to make sure file is unique for each product
			String fileContentType = file.getContentType();
			List<String> extensions = new ArrayList<>();
			extensions.add("image/png"); extensions.add("image/jpeg"); extensions.add("image.jpg");
			if(extensions.contains(fileContentType)){
				String filename = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)+productId.toString();
				storageService.store(file,"images",filename);
				temp.setImageName(filename);
				productService.save(temp);
			}
			else{
				redirectAttributes.addFlashAttribute("error", ResourceBundle.getBundle("messages",current).getString("type.error"));
				return "redirect:/products";
			}

		}

		redirectAttributes.addFlashAttribute("success", ResourceBundle.getBundle("messages",current).getString("image.success"));
		return "redirect:/products";
	}

	@PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
	@PostMapping("/upload/mixtureImage/{mixtureId}")
	public String handleMixtureImageUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, @PathVariable Long mixtureId) throws Exception {
		Mixture temp =  mixtureService.findById( mixtureId).orElse(null);
		Locale current = LocaleContextHolder.getLocale();

		if(temp!=null) {
			String fileContentType = file.getContentType();
			List<String> extensions = new ArrayList<>();
			extensions.add("image/png"); extensions.add("image/jpeg"); extensions.add("image.jpg");
			if(extensions.contains(fileContentType)) {
				//append productid to filename to make sure file is unique for each product
				String filename = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1) + mixtureId.toString();
				storageService.store(file, "images", filename);
				temp.setImage(filename);
				mixtureService.save(temp);
			}
			else{
				redirectAttributes.addFlashAttribute("error", ResourceBundle.getBundle("messages",current).getString("type.error"));
				return "redirect:/products";
			}

		}
		redirectAttributes.addFlashAttribute("success", ResourceBundle.getBundle("messages",current).getString("image.success"));

		return "redirect:/products";
	}

	@PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
	@PostMapping("/upload/mixturePdf/{mixtureId}")
	public String handleMixturePdfUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, @PathVariable Long mixtureId) throws Exception{
		Mixture temp =  mixtureService.findById( mixtureId).orElse(null);
		Locale current = LocaleContextHolder.getLocale();

		if(temp!=null) {
			String fileContentType = file.getContentType();
			List<String> extensions = new ArrayList<>();
			extensions.add("application/pdf");
			if(extensions.contains(fileContentType)) {
			//append productid to filename to make sure file is unique for each product
			String filename = file.getOriginalFilename();
			storageService.store(file,"pdfs",filename);
			temp.setDocument(filename);
			mixtureService.save(temp);}
			else{
				redirectAttributes.addFlashAttribute("error", ResourceBundle.getBundle("messages",current).getString("type.error"));
				return "redirect:/products";
			}

		}
		redirectAttributes.addFlashAttribute("success", ResourceBundle.getBundle("messages",current).getString("pdf.success"));

		return "redirect:/products";
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
