package org.thebreak.roombooking.app.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.thebreak.roombooking.common.response.ResponseResult;

import java.util.Map;


@FeignClient(value = "payment", url = "http://" + "${bookingFeign.payment-host-name}" +":8084/api/v1/payment")
public interface PaymentFeign {

    @GetMapping("/getToken")
    ResponseResult<Map<String, String>> getToken();

    @GetMapping("/pay/{nonce}")
    ResponseResult<?> makePayment(@PathVariable String nonce,
                     @RequestParam String bookingId,
                     @RequestParam int amount );
}
