package be.uantwerpen.labplanner.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.stream.Stream;

import be.uantwerpen.labplanner.Exception.StorageException;
import be.uantwerpen.labplanner.Exception.StorageFileNotFoundException;
import be.uantwerpen.labplanner.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

	private final Path rootLocation;

	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public void store(MultipartFile file,String dir,String filename) {

		Path finalPath = Paths.get(rootLocation.toString()+"/"+dir);
		File tmpDir = new File(finalPath.resolve(filename).toString());
		boolean exists = tmpDir.exists();
		if(exists){
			tmpDir.delete();
		}
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				throw new StorageException(
						"Cannot store file with relative path outside current directory "
								+ filename);
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, finalPath.resolve(filename),
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
	}

	@Override
	public Stream<Path> loadDir(String fileLocation) {
		Path location = Paths.get(rootLocation.toString()+ "/"+ fileLocation+"/");
		try {
			return Files.walk(location, 1)
					.filter(path -> !path.equals(location))
					.map(path -> location.getFileName().resolve(path.getFileName()))
					.distinct();
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}
	}

	@Override
	public void delete(String filename) {
		Path finalPath = Paths.get(rootLocation.toString()+"/"+filename);
		File f= new File(finalPath.toString());
		f.delete();
	}


	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException("Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}



}
