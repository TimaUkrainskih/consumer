package ru.clients.consumer.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.clients.consumer.repository.MessagesJdbcRepository;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final MessagesJdbcRepository messagesJdbcRepository;

    private final WebClientService webClientService;

    @Scheduled(fixedRate = 20_000)
    public void sendMessageToServer() {
        List<String> list = messagesJdbcRepository.findByIsProcessedFalseAndTimeBefore(LocalDateTime.now());
        for(String task: list) {
            log.info("sendMessageToServer(" + task + ")");
            webClientService.postData(task);
        }
    }

    @Scheduled(fixedRate = 3_000_000)
    public void deleteSendMessage() {
        log.info("deleteSentMessages()");
        messagesJdbcRepository.deleteSentMessages();
    }
}
