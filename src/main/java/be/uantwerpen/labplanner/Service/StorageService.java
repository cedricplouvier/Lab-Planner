package be.uantwerpen.labplanner.Service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void store(MultipartFile file);

	Stream<Path> loadAll();
	Stream<Path> loadDir(String fileLocation);

	Path load(String filename);

	Resource loadAsResource(String filename);

	void deleteAll();

}
