package ru.clients.consumer.repository;

import ru.clients.consumer.models.KafkaMessage;
import ru.clients.consumer.models.Message;

import java.time.LocalDateTime;
import java.util.List;

public interface MessagesRepository {

    void save(Message message);

    List<String> findByIsProcessedFalseAndTimeBefore(LocalDateTime time);

    void deleteSentMessages();
}
