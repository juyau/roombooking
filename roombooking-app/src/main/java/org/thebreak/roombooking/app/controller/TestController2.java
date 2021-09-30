package org.thebreak.roombooking.app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thebreak.roombooking.app.kafka.KafkaProducerService;
import org.thebreak.roombooking.common.model.BookingNotificationEmailBO;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/app/test/")
class TestController2 {
    @Autowired
    private KafkaProducerService kafkaService;

    @GetMapping()
    public String getString(){
        return "test final??";
    }

    @PostMapping("/sendNotification")
    public String sendNotification(@RequestBody BookingNotificationEmailBO email)
    {
        kafkaService.sendBookingSuccess(email);
        return "Message sent successfully to the Kafka topic notification";
    }

//    @GetMapping("email")
//    public String getString(@RequestParam String msg){
//       kafkaService.testEmail(msg);
//        return "OK";
//    }
}
