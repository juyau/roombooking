package org.thebreak.roombooking.app.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.thebreak.roombooking.app.model.Booking;


import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    Optional<Booking> findById(String bookingId);

    Page<Booking> findByUserId(String userId, Pageable pageable);

    List<Booking> findByStatus(int status);
}
