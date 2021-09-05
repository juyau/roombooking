package org.thebreak.roombooking.payment.service;

import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.thebreak.roombooking.payment.model.Payment;

import java.time.LocalDateTime;

public class PaymentServiceImplTest extends TestCase {

    @Autowired
    PaymentService paymentService;

    @Test
    public void testFindTotalAmountByTypeAndDateRange() {
//        LocalDateTime start = LocalDateTime.parse("2021-09-05T00:11:00");
//        LocalDateTime end = LocalDateTime.parse("2021-09-05T00:14:00");
//
//        PaymentService paymentService = this.paymentService;
//        paymentService.findTotalAmountByTypeAndDateRange(1, start, end);
//
//        System.out.println("test ended...");
    }
}