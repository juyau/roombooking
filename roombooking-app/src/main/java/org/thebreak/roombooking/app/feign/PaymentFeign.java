package org.thebreak.roombooking.app.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "roombooking-payment/api/v1/email/")
public interface PaymentFeign {

    @GetMapping("getToken")
    void getToken();

    @GetMapping("pay/{nonce}")
    void makePayment(@PathVariable String nonce,
                     @RequestParam String bookingId,
                     @RequestParam int amount );
}
