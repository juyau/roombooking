package org.thebreak.roombooking.app.controller;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thebreak.roombooking.common.response.ResponseResult;

@RestController
@CrossOrigin
@Slf4j
@OpenAPIDefinition(info = @Info(title = "Room Controller", description = "Controller for Room operations"))
@RequestMapping(value = "api/v1/test")
public class TestController {


    @GetMapping
    public ResponseResult<String> getTestString() {
        return ResponseResult.success("Hello from roombooking server...");
    }

}
