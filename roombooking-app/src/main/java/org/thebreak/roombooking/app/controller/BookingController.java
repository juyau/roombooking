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
import org.thebreak.roombooking.app.model.Booking;
import org.thebreak.roombooking.app.model.BookingTimeRange;
import org.thebreak.roombooking.app.model.bo.BookingBO;
import org.thebreak.roombooking.app.model.vo.BookingPreviewVO;
import org.thebreak.roombooking.app.model.vo.BookingVO;
import org.thebreak.roombooking.app.service.BookingService;
import org.thebreak.roombooking.common.exception.CustomException;
import org.thebreak.roombooking.common.response.CommonCode;
import org.thebreak.roombooking.common.response.PageResult;
import org.thebreak.roombooking.common.response.ResponseResult;



import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@OpenAPIDefinition(info = @Info(title = "Booking Controller", description = "Controller for booking operations"))
@RequestMapping(value = "api/v1/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping(value = "/add")
    @Operation(summary = "Add a new booking",
            description = "provide roomId and list of booking range")
    public ResponseResult<BookingPreviewVO> addBooking(@RequestBody @Parameter(description = "room details, no need to provide id") BookingBO bookingBO) {
        BookingPreviewVO b = bookingService.add(bookingBO);
        return ResponseResult.success(b);
    }


    @PutMapping(value = "/updateStatus")
    @Operation(summary = "update booking status by id",
            description = "provide booking id and new status string, paidAmount optional")
    public ResponseResult<BookingVO> updateStatusById(
            @RequestParam @Parameter(description = "booking id") String id,
            @Parameter(description = "status to be updated to") int status,
            @Parameter(description = "paidAmount in Long") @Nullable Long paidAmount
    ) {
        Booking b = bookingService.updateStatusById(id, status, paidAmount);
        BookingVO bookingVO = new BookingVO();
        BeanUtils.copyProperties(b, bookingVO);
        return ResponseResult.success(bookingVO);
    }

    @GetMapping()
    @Operation(summary = "Get all bookings for all rooms, included history bookings",
            description = "Get paged list of bookings, default is page 1 and size 10 if not provided.")
    public ResponseResult<PageResult<BookingVO>> findBookingsPage(
            @RequestParam @Nullable @Parameter(description = "default is 1 if not provided") Integer page,
            @RequestParam @Nullable @Parameter(description = "Max limited to 50, default is 10 if not provided") Integer size) {
        Page<Booking> bookingsPage = bookingService.findPage(page, size);

        // map the list content to VO list
        List<Booking> bookingList = bookingsPage.getContent();
        List<BookingVO> voList = new ArrayList<>();
        for (Booking booking : bookingList) {
            BookingVO bookingVO = new BookingVO();
            BeanUtils.copyProperties(booking, bookingVO);
            voList.add(bookingVO);
        }
        // assemble pageResult
        PageResult<BookingVO> pageResult = new PageResult<>(bookingsPage, voList);

        return ResponseResult.success(pageResult);
    }

    @GetMapping("/getActive")
    @Operation(summary = "Get active bookings(status is Paid or Unpaid)",
            description = "Get paged list of active bookings, default is page 1 and size 10 if not provided.")
    public ResponseResult<PageResult<BookingVO>> findPageActiveBookings(
            @RequestParam @Nullable @Parameter(description = "default is 1 if not provided") Integer page,
            @RequestParam @Nullable @Parameter(description = "Max limited to 50, default is 10 if not provided") Integer size) {
        Page<Booking> bookingsPage = bookingService.findPageActiveBookings(page, size);

        // map the list content to VO list
        List<Booking> bookingList = bookingsPage.getContent();
        List<BookingVO> voList = new ArrayList<>();
        for (Booking booking : bookingList) {
            BookingVO bookingVO = new BookingVO();
            BeanUtils.copyProperties(booking, bookingVO);
            voList.add(bookingVO);
        }
        // assemble pageResult
        PageResult<BookingVO> pageResult = new PageResult<>(bookingsPage, voList);

        return ResponseResult.success(pageResult);
    }


    @GetMapping("byUser")
    @Operation(summary = "Get all bookings for specific user",
            description = "Get paged list of bookings, default is page 1 if not provided, size is fixed to 10.")
    public ResponseResult<PageResult<BookingVO>> findPageByUser(
            @RequestParam @NotNull @Parameter(description = "default is 1 if not provided") String userId,
            @RequestParam @Nullable @Parameter(description = "default size is fixed to 10") Integer page) {
        Page<Booking> bookingsPage = bookingService.findPageByUser(userId, page);

        // map the list content to VO list
        List<Booking> bookingList = bookingsPage.getContent();
        List<BookingVO> voList = new ArrayList<>();
        for (Booking booking : bookingList) {
            BookingVO bookingVO = new BookingVO();
            BeanUtils.copyProperties(booking, bookingVO);
            voList.add(bookingVO);
        }
        // assemble pageResult
        PageResult<BookingVO> pageResult = new PageResult<>(bookingsPage, voList);

        return ResponseResult.success(pageResult);
    }

    @Operation(summary = "Get booking detail by id",
            description = "id provided as path variable.")
    @GetMapping(value = "/byId/{id}")
    public ResponseResult<BookingVO> getById(@PathVariable @NotNull String id) {
        Booking b = bookingService.findById(id);
        BookingVO bookingVO = new BookingVO();
        BeanUtils.copyProperties(b, bookingVO);
        return ResponseResult.success(bookingVO);
    }

    @Operation(summary = "Get future booking for a specific room detail by room id",
            description = "id provided as path variable.")
    @GetMapping(value = "/getRoomFutureBookings/{id}")
    public ResponseResult<List<BookingTimeRange>> findFutureBookedTimesByRoomId(@PathVariable @NotNull String id) {
        List<BookingTimeRange> list = bookingService.findFutureBookedTimesByRoom(id, null);
        return ResponseResult.success(list);
    }

    @PutMapping(value = "/update")
    @Operation(summary = "Update a booking",
            description = "for admin only, please send over all the fields when update.")
    public ResponseResult<BookingVO> updateById(@RequestBody Booking booking) {
        Booking b = bookingService.updateById(booking);
        BookingVO bookingVO = new BookingVO();
        BeanUtils.copyProperties(b, bookingVO);
        return ResponseResult.success(bookingVO);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "Delete a booking",
            description = "booking id provided in path variable.")
    public ResponseResult<CommonCode> deleteById(@PathVariable @NotNull String id) {
        bookingService.deleteById(id);
        log.debug("Booking deleted.");
        return ResponseResult.success();
    }
}
