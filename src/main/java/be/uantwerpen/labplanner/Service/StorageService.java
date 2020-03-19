package be.uantwerpen.labplanner.Service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public interface StorageService {
	//will be used in next story to upload files
	void store(MultipartFile file,String dir);
	Optional<String> getFileExtension(String filename);
	Stream<Path> loadDir(String fileLocation);
	public void delete(String filename);
	Path load(String filename);

	Resource loadAsResource(String filename);

	//will be used in next story to delete files
	//	public void deleteAll();
}
