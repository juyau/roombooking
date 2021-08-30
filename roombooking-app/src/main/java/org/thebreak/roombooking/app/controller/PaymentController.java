package org.thebreak.roombooking.app.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thebreak.roombooking.app.feign.PaymentFeign;
import org.thebreak.roombooking.common.response.ResponseResult;


import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@Slf4j
//@OpenAPIDefinition(info = @Info(title = "Booking Controller", description = "Controller for booking operations"))
@RequestMapping(value = "api/v1/payment")
public class PaymentController {
    @Autowired
    private PaymentFeign paymentFeign;

    @GetMapping(value = "/getToken")
    public ResponseResult<Map<String, String>> getToken() {
        return paymentFeign.getToken();
    }

    @GetMapping(value = "/pay/{nonce}")
    public ResponseResult<?> makePayment(@RequestParam String bookingId, @RequestParam int amount, @PathVariable String nonce) {
        // return responseResult directly from the service, so that it can get the message from braintree if errors occur;
        return paymentFeign.makePayment(nonce, bookingId, amount);
    }

}
