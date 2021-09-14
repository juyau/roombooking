package org.thebreak.roombooking.email.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thebreak.roombooking.common.response.ResponseResult;
import org.thebreak.roombooking.email.model.EmailBO;
import org.thebreak.roombooking.email.service.EmailService;

@RestController
@CrossOrigin
@OpenAPIDefinition(info = @Info(title = "Email Controller", description = "Controller for email service"))
@RequestMapping(value = "api/v1/email")
public class EmailController {
    // test text
    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/sendText")
    public ResponseResult<?> sendTextEmail(@RequestBody EmailBO email) {
        emailService.sendSimpleTextEmail(email);
        return ResponseResult.success();
    }

    @PostMapping(value = "/sendHtml")
    public ResponseResult<?> sendHtmlEmail(@RequestBody EmailBO email) {
        emailService.sendHtmlTextEmail(email);
        return ResponseResult.success();
    }

}
