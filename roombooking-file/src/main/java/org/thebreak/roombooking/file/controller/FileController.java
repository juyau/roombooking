package org.thebreak.roombooking.file.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thebreak.roombooking.common.response.ResponseResult;
import org.thebreak.roombooking.file.service.AWSFileService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "api/v1/file")
public class FileController {

    @Autowired
    private AWSFileService fileService;

    @PostMapping("/uploadImage")
    public ResponseResult<Map<String, String>> uploadFile(@RequestPart MultipartFile file){
        String uploadFile = fileService.uploadImage(file);
        Map<String, String> urlMap = new HashMap<>();
        urlMap.put("url", uploadFile);
        return ResponseResult.success(urlMap);
    }

    @DeleteMapping ("/deleteImage")
    public ResponseResult<?> deleteFile(@RequestParam String url){
        fileService.deleteImage(url);
        return ResponseResult.success();
    }

    @GetMapping ("/listImages")
    public ResponseResult<List<String>> listImages(){

        List<String> list = fileService.listImages();
        return ResponseResult.success(list);
    }

}
