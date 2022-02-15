package com.salesianos.dam.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;


public interface StorageService {

    void init();

    String storeOriginal(MultipartFile file) throws IOException, Exception;

    String storeResized(MultipartFile file,int width) throws IOException, Exception;

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteFile(String filename);

    void deleteAll();

}