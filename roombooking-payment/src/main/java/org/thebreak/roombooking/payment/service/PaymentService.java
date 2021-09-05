package org.thebreak.roombooking.payment.service;


import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.thebreak.roombooking.common.response.ResponseResult;
import org.thebreak.roombooking.payment.model.TotalAndCountVO;
import org.thebreak.roombooking.payment.model.Payment;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface PaymentService {
    String getToken(String customerId);
    ResponseResult<?> makePayment(String orderId, int amount, String paymentMethodNonce, int type);
    Page<Payment> findPaymentsByType(Integer type, int page, int size);
    List<TotalAndCountVO> findTotalAmountByTypeAndDateRange(Integer type, LocalDateTime start, LocalDateTime end);
    Page<Payment> findPaymentsByTypeAndCreatedAtBetween(Integer type, LocalDateTime start, LocalDateTime end, Integer page, Integer size);
}
