package org.thebreak.roombooking.payment.controller;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.thebreak.roombooking.common.response.PageResult;
import org.thebreak.roombooking.common.response.ResponseResult;
import org.thebreak.roombooking.payment.model.TotalAndCountVO;
import org.thebreak.roombooking.payment.model.TypeAndRangeBO;
import org.thebreak.roombooking.payment.model.Payment;
import org.thebreak.roombooking.payment.service.PaymentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@Slf4j
@OpenAPIDefinition(info = @Info(title = "Payment Controller", description = "Controller for payment"))
@RequestMapping(value = "api/v1/payment")
public class PaymentController {
    // test payment
    @Autowired
    private PaymentService paymentService;

    @GetMapping(value = "/getToken")
    public ResponseResult<Map<String, String>> getToken() {
        String token = paymentService.getToken(null);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return ResponseResult.success(tokenMap);
    }

    @GetMapping(value = "/pay/{nonce}")
    public ResponseResult<?> makePayment(@RequestParam String bookingId, @RequestParam int amount, @PathVariable String nonce, @RequestParam int type) {
        // return responseResult directly from the service, so that it can get the message from braintree if errors occur;
        return paymentService.makePayment(bookingId, amount, nonce, type);
    }

    @GetMapping(value = "/getPageByType")
    @Operation(summary = "get paged payments by type, if type is not provided, get all payments.",
            description = "Get paged list of payments, default is page 1 and size 10 if not provided.")
    public ResponseResult<PageResult<Payment>> findPaymentsByType(@RequestParam @Nullable Integer type, int page, int size){
        Page<Payment> paymentPage = paymentService.findPaymentsByType(type, page, size);

        // map the list content to VO list
        List<Payment> bookingList = paymentPage.getContent();
        // assemble pageResult
        PageResult<Payment> pageResult = new PageResult<>(paymentPage, bookingList);
        return ResponseResult.success(pageResult);
    };

    @PostMapping(value = "/getTotalAmountByTypeAndDateRange")
    @Operation(summary = "get payment counts and totalAmount by type, if type is not provided, list that of all types.",
            description = "get payment counts and totalAmount by type.")
    public List<TotalAndCountVO> findTotalAmountByTypeAndDateRange(@RequestBody TypeAndRangeBO amountBO){
        return paymentService.findTotalAmountByTypeAndDateRange(amountBO.getType(),amountBO.getStart(), amountBO.getEnd());
    }

    @PostMapping(value = "/getPageByTypeAndDateRange")
    @Operation(summary = "get paged payments by type and date range, if type is not provided, get payment of all types in the range.",
            description = "if type is not provided, get payment of all types in the range.")
    public ResponseResult<PageResult<Payment>> findPageByTypeAndDateRange(@RequestBody TypeAndRangeBO BO, @RequestParam @Nullable Integer page, @Nullable Integer size){
        Page<Payment> paymentPage = paymentService.findPaymentsByTypeAndCreatedAtBetween(BO.getType(),BO.getStart(), BO.getEnd(), page, size);
        List<Payment> list = paymentPage.getContent();
        PageResult<Payment> pageResult = new PageResult<>(paymentPage, list);
        return ResponseResult.success(pageResult);
    }

}
