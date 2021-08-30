package org.thebreak.roombooking.app.service;

import org.springframework.data.domain.Page;
import org.thebreak.roombooking.app.model.Room;

public interface RoomService {
    Room add(Room room);

    Page<Room> findPage(Integer page, Integer size);

    Room findById(String id);

    void deleteById(String id);

    Room update(Room room);
}
