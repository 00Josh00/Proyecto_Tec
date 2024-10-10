package pe.authentique.inventario.service;

import jakarta.annotation.PostConstruct;
import org.codehaus.groovy.tools.shell.IO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import pe.authentique.inventario.exception.FileNotFoundException;
import pe.authentique.inventario.exception.StorageException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorateService implements StorageService{

    //obtener la carpeta o directorio o strage del properties
    @Value("${storage.location}")
    private String storageLocation;

    @PostConstruct
    @Override
    public void init() {
        try {
            Files.createDirectories(Paths.get(storageLocation));
        }catch (IOException e){
            throw new StorageException("No se puede inicializar el directorio", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (file.isEmpty())
        {
            throw new StorageException("Error, el archivo esta vacío " + filename);
        }
        try {
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, Paths.get(storageLocation).resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw new StorageException("Error en guardar el archivo " + filename);
        }

        return filename;
    }

    @Override
    public Path load_isil(String filename) {
        return Paths.get(storageLocation).resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load_isil(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()){
                return resource;
            }
            else {
                throw new FileNotFoundException("No se puede leer el archivo: " + filename);
            }
        }catch (MalformedURLException e){
            throw new FileNotFoundException("No se puede leer el archivo: " + filename, e);
        }
    }

    @Override
    public void delete(String filename) {
        Path file = load_isil(filename);
        try
        {
            FileSystemUtils.deleteRecursively(file);
        }catch (IOException ignored){

        }
    }
}