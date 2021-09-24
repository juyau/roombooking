package org.thebreak.roombooking.payment.service;

import com.braintreegateway.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.thebreak.roombooking.common.Constants;
import org.thebreak.roombooking.common.exception.CustomException;
import org.thebreak.roombooking.common.response.CommonCode;
import org.thebreak.roombooking.common.response.ResponseResult;
import org.thebreak.roombooking.common.util.PriceUtils;
import org.thebreak.roombooking.payment.dao.PaymentRepository;
import org.thebreak.roombooking.payment.model.TotalAndCountVO;
import org.thebreak.roombooking.payment.model.Payment;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

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

    @Autowired
    private MongoTemplate mongoTemplate;

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
    public Page<Payment> findPaymentsByType(Integer type, int page, int size) {
        if (page < 1) {
            page = 1;
        }
        // mongo page start with 0;
        page = page - 1;
        if (size == 0) {
            size = Constants.DEFAULT_PAGE_SIZE;
        }
        if (size > Constants.MAX_PAGE_SIZE) {
            size = Constants.MAX_PAGE_SIZE;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        if(type == null){
            return paymentRepository.findAll(pageable);
        }else{
            return paymentRepository.findPaymentsByType(type, pageable);
        }

    }

    @Override
    public List<TotalAndCountVO> findTotalAmountByTypeAndDateRange(Integer type, LocalDateTime start, LocalDateTime end) {

        // match
        Criteria criteria = new Criteria();
        if(type != null){
            criteria.and("type").is(type);
        }

        if(start != null && end != null){
            if(start.isAfter(end)){
                CustomException.cast(CommonCode.PAYMENT_START_MUST_BEFORE_END);
            }

            System.out.println(start);
            System.out.println(end);

            criteria.and("createdAt").gte(start).lt(end);
        }

        Aggregation aggregation = newAggregation(
                Aggregation.match(criteria),
                group("type").count().as("count").sum("amount").as("totalAmount"),
                project("totalAmount", "count").and("type").previousOperation(),
                sort(Sort.by("type").ascending())
        );

        AggregationResults<TotalAndCountVO> results = mongoTemplate.aggregate(aggregation, Payment.class, TotalAndCountVO.class);
        System.out.println(results.getMappedResults());
        return results.getMappedResults();
    }

    @Override
    public Page<Payment> findPaymentsByTypeAndCreatedAtBetween(Integer type, LocalDateTime start, LocalDateTime end, Integer page, Integer size) {
        if (page == null || page < 1) {
            page = 1;
        }
        // mongo page start with 0;
        page = page - 1;
        if (size == null || size <= 0) {
            size = Constants.DEFAULT_PAGE_SIZE;
        }
        if (size > Constants.MAX_PAGE_SIZE) {
            size = Constants.MAX_PAGE_SIZE;
        }


        Query query = new Query();
        if( type != null && type != 0){
            query.addCriteria(Criteria.where("type").is(type));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
//        query.addCriteria(Criteria.where("createdAt").gte(start).lt(end));
        query.addCriteria(Criteria.where("createdAt").gte(start).lt(end)).with(pageable);


        List<Payment> list = mongoTemplate.find(query, Payment.class);


        // MongoTemplate does not have built in Page, have to use PageableExecutionUtils from spring data
        Page<Payment> paymentPage = PageableExecutionUtils.getPage(
                list,
                pageable,
                () -> mongoTemplate.count(query.limit(-1).skip(-1), Payment.class));

        return paymentPage;
    }

    @Override
    public ResponseResult<?> makePayment(String bookingId, int amount, String paymentMethodNonce, int type) {

        Result<Transaction> result = requestPayment(PriceUtils.longToBigDecimalInDollar(amount), paymentMethodNonce);

        if (result.isSuccess()) {
            Transaction transaction = result.getTarget();
            String transactionId = transaction.getId();
            Payment payment = savePayment(bookingId, amount, transactionId, type);
            // TODO send message to MQ;

            return ResponseResult.success(payment);
        } else {
            String message = result.getMessage();
            return ResponseResult.fail(message);
        }

    }

    private Payment savePayment(String bookingId, int amount, String transactionId, int type) {
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setTransId(transactionId);
        payment.setBookingId(bookingId);
        payment.setType(type);
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
