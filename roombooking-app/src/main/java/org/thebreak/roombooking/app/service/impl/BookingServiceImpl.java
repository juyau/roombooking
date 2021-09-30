package org.thebreak.roombooking.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thebreak.roombooking.app.dao.BookingRepository;
import org.thebreak.roombooking.app.dao.RoomRepository;
import org.thebreak.roombooking.app.kafka.KafkaProducerService;
import org.thebreak.roombooking.app.model.Booking;
import org.thebreak.roombooking.app.model.BookingTimeRange;
import org.thebreak.roombooking.app.model.Room;
import org.thebreak.roombooking.app.model.bo.BookingBO;
import org.thebreak.roombooking.app.model.enums.BookingStatusEnum;
import org.thebreak.roombooking.app.model.vo.BookingPreviewVO;
import org.thebreak.roombooking.app.service.BookingService;
import org.thebreak.roombooking.app.service.BookingTimeValidator;
import org.thebreak.roombooking.app.service.RoomService;
import org.thebreak.roombooking.app.service.job.BookingCloseTiming;
import org.thebreak.roombooking.common.Constants;
import org.thebreak.roombooking.common.exception.CustomException;
import org.thebreak.roombooking.common.model.BookingNotificationEmailBO;
import org.thebreak.roombooking.common.response.CommonCode;
import org.thebreak.roombooking.common.util.BookingUtils;
import org.thebreak.roombooking.common.util.PriceUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

//    @Autowired
//    private EmailFeign emailFeign;


    @Autowired
    private KafkaProducerService kafkaService;

    private final BookingRepository repository;

    private final RoomRepository roomRepository;

    private final RoomService roomService;

    @Autowired
    private BookingCloseTiming bookingCloseTiming;

    @Autowired
    private BookingTimeValidator timeValidator;

    @Value("${thebreak.roombooking.should-send-bookingSuccessEmail}")
    private int shouldSendBookingSuccessEmail;


    public BookingServiceImpl(BookingRepository repository, RoomRepository roomRepository, RoomService roomService, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.roomRepository = roomRepository;
        this.roomService = roomService;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    @Transactional
    public BookingPreviewVO add(BookingBO bookingBO) {
        checkBookingBoEmptyOrNull(bookingBO);

        // find room detail by id
        Optional<Room> optionalRoom = roomRepository.findById(bookingBO.getRoomId());
        if (!optionalRoom.isPresent()) {
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }
        Room room = optionalRoom.get();

        List<BookingTimeRange> bookingTimeList = bookingBO.getBookingTime();

        int totalBookedHours = timeValidator.validateBookingTimeAndGetTotalHours(bookingTimeList, room);

        int totalAmount = room.getPrice() * totalBookedHours;
        // TODO user info update
        String userId = "userId001";
        int status = BookingStatusEnum.UNPAID.getCode();

        Booking booking = new Booking();
        booking.setTotalHours(totalBookedHours);
        booking.setTotalAmount(totalAmount);
        booking.setPaidAmount(0);
        booking.setRoom(room);
        booking.setStatus(status);
        booking.setUserId(userId);
        booking.setContact(bookingBO.getContact());
        booking.setRemark(bookingBO.getRemark());
        booking.setBookedTime(bookingTimeList);

        Booking booking1 = repository.save(booking);

        // send email notification
        log.info("BookingServiceImpl: start to send email.");

        if(shouldSendBookingSuccessEmail == 1) {
            try {
                sendBookingSuccessMessage(
                        bookingBO.getContact().getEmail(),
                        bookingBO.getContact().getName(),
                        room.getTitle(),
                        totalBookedHours,
                        bookingBO.getBookingTime().get(0).getStart(),
                        totalAmount);
            } catch (Exception e){
                log.error("email feign send email failed.");
                e.printStackTrace();
            }
        }

        // start 30 minutes closing countdown
        log.info("calling startCloseTiming method with bookingID {} ", booking1.getId());
        bookingCloseTiming.startCloseTiming(booking1.getId());

        BookingPreviewVO bookingPreviewVO = new BookingPreviewVO();

        BeanUtils.copyProperties(booking1, bookingPreviewVO);
        bookingPreviewVO.setRoomCity(room.getCity());
        bookingPreviewVO.setRoomTitle(room.getTitle());

        return bookingPreviewVO;
    }

    private void sendBookingSuccessMessage(String toEmail, String userName, String roomTitle, int totalBookedHours, LocalDateTime startTime, int totalAmount) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a");
        String formatedTime = startTime.format(dateTimeFormatter);
        String formatedAmout = PriceUtils.formatDollarString(totalAmount);
        BookingNotificationEmailBO emailBO = new BookingNotificationEmailBO(toEmail,userName,roomTitle, totalBookedHours, formatedTime, formatedAmout);
        kafkaService.sendBookingSuccess(emailBO);
    }

    private void checkBookingBoEmptyOrNull(BookingBO bookingBO) {
        if (bookingBO == null) {
            CustomException.cast(CommonCode.REQUEST_JSON_MISSING);
        }
        if (bookingBO.getRoomId() == null) {
            CustomException.cast(CommonCode.REQUEST_ID_FIELD_MISSING);
        }
        if (bookingBO.getRoomId().isEmpty()) {
            CustomException.cast(CommonCode.REQUEST_ID_INVALID_OR_EMPTY);
        }
        if (bookingBO.getBookingTime() == null) {
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }
        if (bookingBO.getBookingTime().isEmpty()) {
            CustomException.cast(CommonCode.REQUEST_FIELD_EMPTY);
        }
        if (bookingBO.getContact() == null) {
            CustomException.cast(CommonCode.BOOKING_CONTACT_NOTNULL);
        }

        if (bookingBO.getContact().getEmail() == null || bookingBO.getContact().getMobile() == null || bookingBO.getContact().getName() == null) {
            CustomException.cast(CommonCode.BOOKING_CONTACT_NOTNULL);
        }
        if(!BookingUtils.isEmail(bookingBO.getContact().getEmail())){
            CustomException.cast(CommonCode.BOOKING_EMAIL_INVALID);
        }

    }

    private final MongoTemplate mongoTemplate;

    @Override
    public List<BookingTimeRange> findFutureBookedTimesByRoom(String roomId, String city) {
        if (city == null) {
            Room room = roomService.findById(roomId);
            city = room.getCity();
        }

        LocalDateTime nowAtZonedCity = BookingUtils.getNowAtZonedCity(city);
//        LocalDateTime after7days = nowAtZonedCity.plusDays(7);

        Query query = new Query();
        query.addCriteria(Criteria.where("room.id").is(roomId))
                .addCriteria(Criteria.where("bookedTime").elemMatch(Criteria.where("end").gt(nowAtZonedCity)))
                .fields().include("bookedTime");

        List<Booking> list = mongoTemplate.find(query, Booking.class);

        List<BookingTimeRange> futureBookedTimeList = new ArrayList<>();

        for (Booking booking : list) {
            List<BookingTimeRange> timeList = booking.getBookedTime();
            for (BookingTimeRange bookingTimeRange : timeList) {
                LocalDateTime end = bookingTimeRange.getEnd();
                if (end.isAfter(nowAtZonedCity)) {
                    futureBookedTimeList.add(bookingTimeRange);
                }
            }
        }

        return futureBookedTimeList;
    }

    @Override
    public List<BookingTimeRange> findBookedTimesByRoomInRange(String roomId, LocalDateTime start, LocalDateTime end) {
        Room room = roomService.findById(roomId);

        Query query = new Query();
        query.addCriteria(Criteria.where("room.id").is(roomId))
                .addCriteria(Criteria.where("bookedTime").elemMatch(Criteria.where("start").gte(start))
                        .elemMatch(Criteria.where("end").lte(end)))
                .fields().include("bookedTime");

        List<Booking> list = mongoTemplate.find(query, Booking.class);

        List<BookingTimeRange> bookedTimeList = new ArrayList<>();

        for (Booking booking : list) {
            List<BookingTimeRange> timeList = booking.getBookedTime();
            for(BookingTimeRange timeRange : timeList){
                if(timeRange.getStart().isAfter(start) && timeRange.getEnd().isBefore(end)){
                    bookedTimeList.add(timeRange);
                }
            }
        }
        return bookedTimeList;
    }

    @Override
    public Booking findById(String id) {
        if (id == null) {
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }
        if (id.trim().isEmpty()) {
            CustomException.cast(CommonCode.REQUEST_FIELD_EMPTY);
        }
        Optional<Booking> optional = repository.findById(id);
        if (!optional.isPresent()) {
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }
        return optional.get();
    }

    @Override
    public Page<Booking> findPage(Integer page, Integer size) {
        if (page == null || page < 1) {
            page = 1;
        }
        // mongo page start with 0;
        page = page - 1;

        if (size == null) {
            size = Constants.DEFAULT_PAGE_SIZE;
        }
        if (size > Constants.MAX_PAGE_SIZE) {
            size = Constants.MAX_PAGE_SIZE;
        }

        Page<Booking> bookingsPage = repository.findAll(PageRequest.of(page, size, Sort.by("updatedAt").descending()));

        if (bookingsPage.getContent().size() == 0) {
            CustomException.cast(CommonCode.DB_EMPTY_LIST);
        }
        return bookingsPage;
    }

    @Override
    public Page<Booking> findPageByUser(String userId, Integer page) {
        if (page == null || page < 1) {
            page = 1;
        }
        // mongo page start with 0;
        page = page - 1;
        Pageable pageable = PageRequest.of(page, Constants.DEFAULT_PAGE_SIZE, Sort.by("updatedAt").descending());
        Page<Booking> bookingsPage = repository.findByUserId(userId, pageable);

        if (bookingsPage.getContent().size() == 0) {
            CustomException.cast(CommonCode.DB_EMPTY_LIST);
        }
        return bookingsPage;
    }

    @Override
    public Page<Booking> findPageActiveBookings(Integer page, Integer size) {
        if (page == null || page < 1) {
            page = 1;
        }
        // mongo page start with 0;
        page = page - 1;

        if (size == null) {
            size = Constants.DEFAULT_PAGE_SIZE;
        }
        if (size > Constants.MAX_PAGE_SIZE) {
            size = Constants.MAX_PAGE_SIZE;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("bookedAt").descending());
        Query query = new Query();
        query.addCriteria(Criteria.where("status").in("Paid", "Unpaid")).with(pageable);

        List<Booking> list = mongoTemplate.find(query, Booking.class);
        // MongoTemplate does not have built in Page, have to use PageableExecutionUtils from spring data
        Page<Booking> bookingsPage = PageableExecutionUtils.getPage(
                list,
                pageable,
                () -> mongoTemplate.count(query.limit(-1).skip(-1), Booking.class));

        return bookingsPage;
    }

    @Override
    public void deleteById(String id) {
        if (id == null) {
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }
        if (id.trim().isEmpty()) {
            CustomException.cast(CommonCode.REQUEST_FIELD_EMPTY);
        }
        Optional<Booking> optional = repository.findById(id);
        if (optional.isPresent()) {
            repository.deleteById(id);
        } else {
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }
    }


    @Override
    public Booking updateById(Booking booking) {
        if (booking == null) {
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }
        if (null == booking.getId()) {
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }

        Optional<Booking> optional = repository.findById(booking.getId());
        if (!optional.isPresent()) {
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }

        // TODO to implement update ignore null fields
        Booking roomReturn = optional.get();

        return repository.save(booking);

    }

    @Override
    public Booking updateStatusById(String id, int status, int paidAmount) {
        if (id == null || status == 0) {
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }

        // if status to be paid, need to provide amount > 0
        if (status == BookingStatusEnum.PAID.getCode() && paidAmount <= 0) {
            CustomException.cast(CommonCode.BOOKING_PAID_WITHOUT_AMOUNT);
        }

        Query query = new Query().addCriteria(Criteria.where("id").is(id));
        Update update = new Update().set("status", status);
        if (status == BookingStatusEnum.PAID.getCode()) {
            update.set("paidAmount", paidAmount);
        }
        Booking updatedBooking = mongoTemplate.update(Booking.class)
                .matching(query)
                .apply(update)
                .withOptions(FindAndModifyOptions.options().returnNew(true))
                .findAndModifyValue();

        return updatedBooking;

    }
}
