package com.salesianos.dam.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;


public interface StorageService {

    void init();

    String storeOriginal(MultipartFile file) throws IOException, Exception;

    String storeImageResized(MultipartFile file,int width) throws IOException, Exception;

    String storeVideoResized(MultipartFile file,int width) throws IOException, Exception;

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteFile(Path filePath) throws IOException;

    void deleteAll();

}