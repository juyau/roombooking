package org.thebreak.roombooking.app.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.thebreak.roombooking.common.model.PaymentEmailBO;
import org.thebreak.roombooking.common.response.ResponseResult;

import java.util.Map;

@FeignClient(value = "roombooking-payment/api/v1/payment/")
public interface PaymentFeign {


    @GetMapping("getToken")
    ResponseResult<Map<String, String>> getToken();

    @GetMapping("pay/{nonce}")
    ResponseResult<PaymentEmailBO> makePayment(@RequestParam String bookingId, @RequestParam int amount, @PathVariable String nonce, @RequestParam int type);

//    @GetMapping("getPageByType")
//    ResponseResult<?> findPaymentsByType(@RequestParam @Nullable Integer type, int page, int size);

//    @PostMapping(value = "/getTotalAmountByTypeAndDateRange")
//    List<?> findTotalAmountByTypeAndDateRange(@RequestBody TypeAndRangeBO amountBO);

//    @PostMapping(value = "/getPageByTypeAndDateRange")
//    ResponseResult<PageResult<Payment>> findPageByTypeAndDateRange(@RequestBody TypeAndRangeBO BO, @RequestParam @Nullable Integer page, @Nullable Integer size)

}
