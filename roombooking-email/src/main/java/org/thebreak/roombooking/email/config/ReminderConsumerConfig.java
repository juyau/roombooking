package org.thebreak.roombooking.email.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.thebreak.roombooking.common.model.BookingReminderEmailBO;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class ReminderConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    String notificationGroupId;

    @Bean
    public ConsumerFactory<String, BookingReminderEmailBO> reminderConsumerFactory(){
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, notificationGroupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(BookingReminderEmailBO.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookingReminderEmailBO> reminderListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, BookingReminderEmailBO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(reminderConsumerFactory());
        return factory;
    }

}
