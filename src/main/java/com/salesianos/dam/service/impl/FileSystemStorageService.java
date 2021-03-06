package com.salesianos.dam.service.impl;

import com.salesianos.dam.config.StorageProperties;
import com.salesianos.dam.errors.exception.StorageException;
import com.salesianos.dam.service.StorageService;
import com.salesianos.dam.utils.MediaTypeUrlResource;
import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.IVSize;
import io.github.techgnious.dto.ImageFormats;
import io.github.techgnious.dto.ResizeResolution;
import io.github.techgnious.dto.VideoFormats;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.salesianos.dam.errors.exception.FileNotFoundException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ws.schild.jave.encode.EncodingAttributes;

import javax.annotation.PostConstruct;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {


    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @PostConstruct
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }



    @Override
    public String storeOriginal(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String newFilename = "";

        try {
            if (file.isEmpty())
                throw new StorageException("El fichero subido est?? vac??o");

            newFilename = filename;
            while(Files.exists(rootLocation.resolve(newFilename))) {
                String extension = StringUtils.getFilenameExtension(newFilename);
                String name = newFilename.replace("."+extension,"");

                String suffix = Long.toString(System.currentTimeMillis());
                suffix = suffix.substring(suffix.length()-6);
                newFilename = name + "_" + suffix + "." + extension;

            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, rootLocation.resolve(newFilename),
                        StandardCopyOption.REPLACE_EXISTING);
            }



        } catch (IOException ex) {
            throw new StorageException("Error en el almacenamiento del fichero: " + newFilename, ex);
        }

        return newFilename;

    }


    @Override
    public String storeImageResized(MultipartFile file,int width) throws Exception {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        String extension = StringUtils.getFilenameExtension(filename);
        String name = filename.replace("."+extension,"");

        IVCompressor compressor = new IVCompressor();
        IVSize customRes=new IVSize();
        customRes.setWidth(width);
        customRes.setHeight(width);

        byte[] inputS =  compressor.resizeImageWithCustomRes(file.getBytes(), ImageFormats.JPEG,customRes);

        ByteArrayInputStream bis = new ByteArrayInputStream(inputS);


        try {

            if (file.isEmpty())
                throw new StorageException("El fichero subido est?? vac??o");


            while(Files.exists(rootLocation.resolve(filename))) {


                String suffix = Long.toString(System.currentTimeMillis());
                suffix = suffix.substring(suffix.length()-6);

                filename = name + "_" + suffix + "." + extension;
            }

            try (InputStream inputStream = bis) {
                Files.copy(inputStream, rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }



        } catch (IOException ex) {
            throw new StorageException("Error en el almacenamiento del fichero: " + filename, ex);
        }


        return filename;

    }



    @Override
    public String storeVideoResized(MultipartFile file, int width) throws IOException, Exception {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        String extension = StringUtils.getFilenameExtension(filename);
        String name = filename.replace("."+extension,"");

        IVCompressor compressor = new IVCompressor();
        IVSize customRes = new IVSize();
        customRes.setWidth(width);
        customRes.setHeight(width);
        byte[] inputS = compressor.reduceVideoSizeWithCustomRes(file.getBytes(), VideoFormats.MP4, customRes);

        ByteArrayInputStream bis = new ByteArrayInputStream(inputS);

        while(Files.exists(rootLocation.resolve(filename))) {


            String suffix = Long.toString(System.currentTimeMillis());
            suffix = suffix.substring(suffix.length()-6);

            filename = name + "_" + suffix + "." + extension;
        }

        try {

            if (file.isEmpty())
                throw new StorageException("El fichero subido est?? vac??o");


            try (InputStream inputStream = bis) {
                Files.copy(inputStream, rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }



        } catch (IOException ex) {
            throw new StorageException("Error en el almacenamiento del fichero: " + filename, ex);
        }


        return filename;
    }

    @Override
    public String createUri(String fileName){
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileName)
                .toUriString();
    }


    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Error al leer los ficheros almacenados", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {

        try {
            Path file = load(filename);
            MediaTypeUrlResource resource = new MediaTypeUrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new FileNotFoundException(
                        "Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteFile(Path filePath) throws IOException {

        MediaTypeUrlResource mediaTypeUrlResource = new MediaTypeUrlResource(filePath.toUri());

        // Instancio como mediaTypeResource el path que me pasan por par??metro
        // para comprobar si ??s v??lido y si no lanzar una excepci??n.
        try {
            if (mediaTypeUrlResource.exists() || mediaTypeUrlResource.isReadable()) {
                Files.delete(filePath);
            } else {
                throw new FileNotFoundException(
                        "No se ha podido leer el archivo: " + filePath);
            }
        }catch (MalformedURLException e) {
            throw new FileNotFoundException("No se ha podido leer el archivo: " + filePath, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}