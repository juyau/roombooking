package org.thebreak.roombooking.payment;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableMongoAuditing
@EnableMongoRepositories
@OpenAPIDefinition(info = @Info(title = "Booking payment API", version = "1.0.0", description = "api for room payment service"))
public class PaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }

}
