package org.thebreak.roombooking.app.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.thebreak.roombooking.app.model.Booking;

import java.util.List;

@Repository
public class BookingDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Booking> findList(Query query) {
        return mongoTemplate.find(query, Booking.class);
    }

}
