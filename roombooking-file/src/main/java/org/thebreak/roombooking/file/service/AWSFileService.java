package org.thebreak.roombooking.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AWSFileService {
    String uploadImage(MultipartFile file);
    void deleteImage(String url);
    List<String> listImages();

}
