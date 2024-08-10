package il.sce.scecafe.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final ResourceLoader resourceLoader;

    @Value("classpath:itemsImages/*")
    private Resource[] resources;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir, ResourceLoader resourceLoader) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.resourceLoader = resourceLoader;
        try {
            Files.createDirectories(this.fileStorageLocation);
            System.out.println("Created directory: " + this.fileStorageLocation.toString());
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("Initializing FileStorageService");
        copyResourcesToStorage();
    }

    private void copyResourcesToStorage() {
        try {
            for (Resource resource : resources) {
                System.out.println("Processing resource: " + resource.getFilename());
                if (resource.exists()) {
                    Path targetLocation = this.fileStorageLocation.resolve(resource.getFilename());
                    Files.copy(resource.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Copied " + resource.getFilename() + " to " + targetLocation);
                } else {
                    System.out.println("Resource does not exist: " + resource.getFilename());
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Could not copy resource files to storage. Please try again!", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
