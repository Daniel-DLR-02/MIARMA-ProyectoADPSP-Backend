package com.salesianos.dam.service.impl;

import com.salesianos.dam.config.StorageProperties;
import com.salesianos.dam.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public String store(MultipartFile file) throws Exception {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        String extension = StringUtils.getFilenameExtension(filename);
        String name = filename.replace("."+extension,"");

        BufferedImage img = ImageIO.read(file.getInputStream());

        BufferedImage escaleImg = simpleResizeImage(img , 4000);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( escaleImg, extension, baos );

        MultipartFile newImage = new MockMultipartFile(name,baos.toByteArray());

        try {
            // Si el fichero está vacío, excepción al canto
            if (newImage.isEmpty())
                throw new StorageException("El fichero subido está vacío");


            while(Files.exists(rootLocation.resolve(filename))) {
                // Tratamos de generar uno nuevo


                String suffix = Long.toString(System.currentTimeMillis());
                suffix = suffix.substring(suffix.length()-6);

                filename = name + "_" + suffix + "." + extension;
            }

            try (InputStream inputStream = newImage.getInputStream()) {
                Files.copy(inputStream, rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }



        } catch (IOException ex) {
            throw new StorageException("Error en el almacenamiento del fichero: " + filename, ex);
        }


        return filename;

    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        return null;
    }

    @Override
    public void deleteFile(String filename) {

    }

    @Override
    public void deleteAll() {

    }
}