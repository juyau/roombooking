package org.thebreak.roombooking.payment.service;


import org.springframework.stereotype.Service;
import org.thebreak.roombooking.common.response.ResponseResult;

@Service
public interface PaymentService {
    String getToken(String customerId);

    ResponseResult<?> makePayment(String orderId, int amount, String paymentMethodNonce);
}
