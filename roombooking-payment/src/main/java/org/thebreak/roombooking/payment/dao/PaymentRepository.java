package org.thebreak.roombooking.payment.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.thebreak.roombooking.payment.model.Payment;


@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

}
