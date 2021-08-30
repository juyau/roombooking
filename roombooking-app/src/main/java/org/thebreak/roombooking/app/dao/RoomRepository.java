package org.thebreak.roombooking.app.dao;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.thebreak.roombooking.app.model.Room;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {
    Room findByAddressAndRoomNumber(String title, String roomNumber);
}
