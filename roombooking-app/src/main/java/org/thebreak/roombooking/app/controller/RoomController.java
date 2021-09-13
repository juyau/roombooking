package org.thebreak.roombooking.app.controller;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.thebreak.roombooking.app.model.BookingTimeRange;
import org.thebreak.roombooking.app.model.Room;
import org.thebreak.roombooking.app.model.vo.RoomPreviewVO;
import org.thebreak.roombooking.app.model.vo.RoomVO;
import org.thebreak.roombooking.app.model.vo.RoomWithBookedTimeVO;
import org.thebreak.roombooking.app.service.BookingService;
import org.thebreak.roombooking.app.service.RoomService;
import org.thebreak.roombooking.common.response.CommonCode;
import org.thebreak.roombooking.common.response.PageResult;
import org.thebreak.roombooking.common.response.ResponseResult;


import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@OpenAPIDefinition(info = @Info(title = "Room Controller", description = "Controller for Room operations"))
@RequestMapping(value = "api/v1/app/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingService bookingService;

    @GetMapping()
    @Operation(summary = "Get rooms",
            description = "Get paged list of rooms, default is page 1 and size 10 if not provided.")
    public ResponseResult<PageResult<RoomPreviewVO>> findRoomsPage(
            @RequestParam @Nullable @Parameter(description = "default is 1 if not provided") Integer page,
            @RequestParam @Nullable @Parameter(description = "Max limited to 50, default is 10 if not provided") Integer size) {
        Page<Room> roomPage = roomService.findPage(page, size);

        // map the list content to VO list
        List<Room> roomList = roomPage.getContent();
        List<RoomPreviewVO> voList = new ArrayList<>();
        for (Room room : roomList) {
            RoomPreviewVO roomVO = new RoomPreviewVO();
            BeanUtils.copyProperties(room, roomVO);
            voList.add(roomVO);
        }
        // assemble pageResult
        PageResult<RoomPreviewVO> pageResult = new PageResult<>(roomPage, voList);

        return ResponseResult.success(pageResult);
    }

    @Operation(summary = "Get rooms by id",
            description = "id provided as path variable.")
    @GetMapping(value = "/byId/{id}")
    public ResponseResult<RoomVO> getById(@PathVariable String id) {
        Room r = roomService.findById(id);
        RoomVO roomVO = new RoomVO();
        BeanUtils.copyProperties(r, roomVO);
        return ResponseResult.success(roomVO);
    }

    @Operation(summary = "Get rooms with future booked times",
            description = "full room detail plus future bookedTime list")
    @GetMapping(value = "/withBookedTime/{id}")
    public ResponseResult<RoomWithBookedTimeVO> getRoomWithFutureBookedTime(@PathVariable @NotNull String id) {
        Room r = roomService.findById(id);
        List<BookingTimeRange> bookedTimeList = bookingService.findFutureBookedTimesByRoom(id, r.getCity());
        RoomWithBookedTimeVO roomVO = new RoomWithBookedTimeVO();
        BeanUtils.copyProperties(r, roomVO);
        roomVO.setBookedTime(bookedTimeList);
        return ResponseResult.success(roomVO);
    }

    @PostMapping(value = "/add")
    @Operation(summary = "Add a new room",
            description = "room address and room number cannot be the same with exist records. Note: no need to provide id")
    public ResponseResult<RoomVO> addRoom(@RequestBody @Parameter(description = "room details, no need to provide id") Room room) {
        Room r = roomService.add(room);
        RoomVO roomVO = new RoomVO();
        BeanUtils.copyProperties(r, roomVO);
        return ResponseResult.success(roomVO);
    }

    @PutMapping(value = "/update")
    @Operation(summary = "Update a room",
            description = "please send over all the fields when update.")
    public ResponseResult<RoomVO> updateRoom(@RequestBody Room room) {
        Room r = roomService.update(room);
        RoomVO roomVO = new RoomVO();
        BeanUtils.copyProperties(r, roomVO);
        return ResponseResult.success(roomVO);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "Delete a room",
            description = "room address provided in path variable.")
    public ResponseResult<CommonCode> deleteRoomById(@PathVariable @Nullable String id) {
        roomService.deleteById(id);
        log.debug("Room deleted.");
        return ResponseResult.success();
    }
}
