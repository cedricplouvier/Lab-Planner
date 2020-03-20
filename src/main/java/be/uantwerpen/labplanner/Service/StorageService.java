package be.uantwerpen.labplanner.Service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public interface StorageService {
	void store(MultipartFile file,String dir,String filename);
	Stream<Path> loadDir(String fileLocation);
	void delete(String filename);
	Path load(String filename);
	Resource loadAsResource(String filename);
}
