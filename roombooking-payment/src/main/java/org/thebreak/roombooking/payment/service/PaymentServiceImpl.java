package org.thebreak.roombooking.payment.service;

import com.braintreegateway.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thebreak.roombooking.common.response.ResponseResult;
import org.thebreak.roombooking.common.util.PriceUtils;
import org.thebreak.roombooking.dao.PaymentRepository;
import org.thebreak.roombooking.model.Payment;


import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Value("${braintree.ENVIRONMENT}")
    private String ENVIRONMENT;

    @Value("${braintree.MERCHANT_ID}")
    private String MERCHANT_ID;

    @Value("${braintree.PUBLIC_KEY}")
    private String PUBLIC_KEY;

    @Value("${braintree.PRIVATE_KEY}")
    private String PRIVATE_KEY;

    public BraintreeGateway getBrainTreeGateway() {
        return new BraintreeGateway(Environment.SANDBOX, MERCHANT_ID, PUBLIC_KEY, PRIVATE_KEY);
    }
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public String getToken(String customerId) {
        BraintreeGateway gateway = getBrainTreeGateway();
        ClientTokenRequest clientTokenRequest = new ClientTokenRequest();
        if (customerId != null) {
            clientTokenRequest.customerId(customerId);
        }
        return gateway.clientToken().generate(clientTokenRequest);
    }

    @Override
    public ResponseResult<?> makePayment(String bookingId, int amount, String paymentMethodNonce) {

        Result<Transaction> result = requestPayment(PriceUtils.longToBigDecimalInDollar(amount), paymentMethodNonce);

        if (result.isSuccess()) {
            Transaction transaction = result.getTarget();
            String transactionId = transaction.getId();
            Payment payment = insertPayment(bookingId, amount, transactionId);
            return ResponseResult.success(payment);
        } else {
            String message = result.getMessage();
            return ResponseResult.fail(message);
        }

    }

    private Payment insertPayment(String bookingId, int amount, String transactionId) {
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setTransId(transactionId);
        payment.setBookingId(bookingId);
        return paymentRepository.save(payment);
    }

    private Result<Transaction> requestPayment(BigDecimal amount, String paymentMethodNonce) {
        TransactionRequest request = new TransactionRequest()
                .amount(amount)
                .paymentMethodNonce(paymentMethodNonce)
                .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> result = getBrainTreeGateway().transaction().sale(request);
        return result;
    }

}
