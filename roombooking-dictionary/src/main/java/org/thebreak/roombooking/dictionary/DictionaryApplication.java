package org.thebreak.roombooking.dictionary;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongoRepositories
@OpenAPIDefinition(info = @Info(title = "Booking Dictionary API", version = "1.0.0", description = "api for room dictionary service"))
@ComponentScan(basePackages = {"org.thebreak","org.thebreak.roombooking.common"})
public class DictionaryApplication {
    // main test111
    public static void main(String[] args) {
        SpringApplication.run(DictionaryApplication.class, args);
    }

}
