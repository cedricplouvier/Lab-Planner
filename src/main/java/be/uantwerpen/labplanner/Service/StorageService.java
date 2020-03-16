package be.uantwerpen.labplanner.Service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
	//will be used in next story to upload files
	//void store(MultipartFile file);

	Stream<Path> loadDir(String fileLocation);

	Path load(String filename);

	Resource loadAsResource(String filename);

	//will be used in next story to delete files
	//	public void deleteAll();
}
