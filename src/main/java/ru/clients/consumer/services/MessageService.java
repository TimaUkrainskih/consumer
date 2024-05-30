package ru.clients.consumer.services;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import ru.clients.consumer.models.KafkaMessage;
import ru.clients.consumer.models.Message;
import ru.clients.consumer.repository.MessagesJdbcRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {

    private static final String HEADER_KEY = "header";

    private final MessagesJdbcRepository messagesJdbcRepository;

    @KafkaListener(topics = "test", groupId = "group_id")
    public void consumeFromSpecPartition(ConsumerRecord<String, String> record,
                                         @Header(HEADER_KEY) byte[] delay) {
        long time = Long.parseLong(new String(delay));
        KafkaMessage message = new KafkaMessage(record.value(), time);
        Message message1 = new Message(LocalDateTime.now(), time, message);
        messagesJdbcRepository.save(message1);
    }
}
