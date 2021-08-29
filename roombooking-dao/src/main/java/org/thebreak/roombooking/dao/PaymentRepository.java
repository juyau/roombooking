package org.thebreak.roombooking.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.thebreak.roombooking.model.Payment;

//@Repository
@Component
public interface PaymentRepository extends MongoRepository<Payment, String> {

}
