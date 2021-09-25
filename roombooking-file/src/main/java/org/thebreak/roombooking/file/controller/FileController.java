package org.thebreak.roombooking.file.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thebreak.roombooking.common.response.ResponseResult;
import org.thebreak.roombooking.file.service.AWSFileService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@OpenAPIDefinition(info = @Info(title = "File Controller", description = "Controller for S3 file operation"))
@RequestMapping(value = "api/v1/file")
public class FileController {

    @Autowired
    private AWSFileService fileService;

    @PostMapping("/uploadImage")
    @Operation(summary = "upload file to S3",
            description = "Multipart file")
    public ResponseResult<Map<String, String>> uploadFile(@RequestPart MultipartFile file){
        String uploadFile = fileService.uploadImage(file);
        Map<String, String> urlMap = new HashMap<>();
        urlMap.put("url", uploadFile);
        return ResponseResult.success(urlMap);
    }

    @DeleteMapping ("/deleteImage")
    @Operation(summary = "delete file from S3",
            description = "provide full url of the file")
    public ResponseResult<?> deleteFile(@RequestParam String url){
        fileService.deleteImage(url);
        return ResponseResult.success();
    }

    @GetMapping ("/listImages")
    @Operation(summary = "list all images from S3",
            description = "list all images")
    public ResponseResult<List<String>> listImages(){
        List<String> list = fileService.listImages();
        return ResponseResult.success(list);
    }

}
