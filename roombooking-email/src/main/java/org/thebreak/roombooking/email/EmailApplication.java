package org.thebreak.roombooking.email;



import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
@OpenAPIDefinition(info = @Info(title = "Booking Email API", version = "1.0.0", description = "api for room email service"))
public class EmailApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailApplication.class, args);
    }

}
