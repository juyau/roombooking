package org.thebreak.roombooking.email.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thebreak.roombooking.model.bo.EmailBO;
import org.thebreak.roombooking.common.response.ResponseResult;
import org.thebreak.roombooking.email.service.EmailService;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "api/v1/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/send")
    public ResponseResult<?> sendHtmlEmail(@RequestBody EmailBO email) {
        emailService.sendSimpleTextEmail(email);
        return ResponseResult.success();
    }

}
