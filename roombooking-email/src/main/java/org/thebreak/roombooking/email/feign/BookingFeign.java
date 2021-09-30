package org.thebreak.roombooking.email.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.thebreak.roombooking.common.model.PaymentEmailBO;
import org.thebreak.roombooking.common.response.ResponseResult;

@FeignClient(value = "roombooking-app/api/v1/app/bookings")
public interface BookingFeign {

    @GetMapping(value = "/getPaymentEmailBObyId/{id}")
    ResponseResult<PaymentEmailBO> findPaymentEmailBObyId(@PathVariable String id);

}
