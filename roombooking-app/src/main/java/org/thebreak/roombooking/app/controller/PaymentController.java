package org.thebreak.roombooking.app.controller;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thebreak.roombooking.app.feign.PaymentFeign;
import org.thebreak.roombooking.app.model.Booking;
import org.thebreak.roombooking.app.model.enums.BookingStatusEnum;
import org.thebreak.roombooking.app.service.BookingService;
import org.thebreak.roombooking.common.exception.CustomException;
import org.thebreak.roombooking.common.response.CommonCode;
import org.thebreak.roombooking.common.response.ResponseResult;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@OpenAPIDefinition(info = @Info(title = "Payment Controller", description = "Controller for payment"))
@RequestMapping(value = "api/v1/app/payment")
public class PaymentController {
    // test payment
    @Autowired
    private PaymentFeign paymentFeign;

    @Autowired
    private BookingService bookingService;


    @GetMapping(value = "/getToken")
    public ResponseResult<Map<String, String>> getToken() throws ExecutionException, InterruptedException {
        return paymentFeign.getToken();
    }

    @GetMapping(value = "/pay/{nonce}")
    public ResponseResult<?> makePayment(@RequestParam String bookingId, @RequestParam int amount, @PathVariable String nonce, @RequestParam int type) {
        // check if booking already closed, for example timeout;
        Booking booking = bookingService.findById(bookingId);
        if(booking.getStatus() == BookingStatusEnum.CLOSED.getCode()){
            CustomException.cast(CommonCode.BOOKING_ALREADY_CLOSED);
        }
        if(booking.getStatus() == BookingStatusEnum.PAID.getCode()){
            CustomException.cast(CommonCode.PAYMENT_ALREADY_MADE);
        }
        // return responseResult directly from the service, so that it can get the message from braintree if errors occur;
        return paymentFeign.makePayment(bookingId, amount, nonce, type);
    }

//    @GetMapping(value = "/getPageByType")
//    @Operation(summary = "get paged payments by type, if type is not provided, get all payments.",
//            description = "Get paged list of payments, default is page 1 and size 10 if not provided.")
//    public ResponseResult<?> findPaymentsByType(@RequestParam @Nullable Integer type, int page, int size){
//        return paymentFeign.findPaymentsByType(type, page, size);
//    };

//    @PostMapping(value = "/getTotalAmountByTypeAndDateRange")
//    @Operation(summary = "get payment counts and totalAmount by type, if type is not provided, list that of all types.",
//            description = "get payment counts and totalAmount by type.")
//    public List<TotalAndCountVO> findTotalAmountByTypeAndDateRange(@RequestBody TypeAndRangeBO amountBO){
//        return paymentService.findTotalAmountByTypeAndDateRange(amountBO.getType(),amountBO.getStart(), amountBO.getEnd());
//    }
//
//    @PostMapping(value = "/getPageByTypeAndDateRange")
//    @Operation(summary = "get paged payments by type and date range, if type is not provided, get payment of all types in the range.",
//            description = "if type is not provided, get payment of all types in the range.")
//    public ResponseResult<PageResult<Payment>> findPageByTypeAndDateRange(@RequestBody TypeAndRangeBO BO, @RequestParam @Nullable Integer page, @Nullable Integer size){
//        Page<Payment> paymentPage = paymentService.findPaymentsByTypeAndCreatedAtBetween(BO.getType(),BO.getStart(), BO.getEnd(), page, size);
//        List<Payment> list = paymentPage.getContent();
//        PageResult<Payment> pageResult = new PageResult<>(paymentPage, list);
//        return ResponseResult.success(pageResult);
//    }

}
