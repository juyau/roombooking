package org.thebreak.roombooking.payment.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thebreak.roombooking.common.response.ResponseResult;
import org.thebreak.roombooking.payment.service.PaymentService;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@Slf4j
//@OpenAPIDefinition(info = @Info(title = "Booking Controller", description = "Controller for booking operations"))
@RequestMapping(value = "api/v1/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping(value = "/getToken")
//    @Operation(summary = "get token for braintree",
//            description = "get token for braintree")
    public ResponseResult<Map<String, String>> getToken() {
        String token = paymentService.getToken(null);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return ResponseResult.success(tokenMap);
    }

    @GetMapping(value = "/pay/{nonce}")
    @Operation(summary = "get token for braintree",
            description = "get token for braintree")
    public ResponseResult<?> makePayment(@RequestParam String bookingId, @RequestParam int amount, @PathVariable String nonce) {
        // return responseResult directly from the service, so that it can get the message from braintree if errors occur;
        return paymentService.makePayment(bookingId, amount, nonce);
    }

}
