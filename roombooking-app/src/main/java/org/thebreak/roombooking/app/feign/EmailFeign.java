package org.thebreak.roombooking.app.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@FeignClient(value = "sendEmail", url = "http://localhost:8085/api/v1/email")
public interface EmailFeign {

    @PostMapping("/sendText")
     void sendTextEmail(@RequestBody Map<String, String> email);

    @PostMapping("/sendHtml")
    void sendHtmlEmail(@RequestBody Map<String, String> email);
}
