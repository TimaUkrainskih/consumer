package ru.clients.consumer.kafka;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class KafkaConsumer {

    private WebClient webClient;


    @KafkaListener(topicPartitions = @TopicPartition(topic = "test", partitions = {"4"}),
            groupId = "group_id")
    public void consumeFromSpecPartition(ConsumerRecord<String, String> message) {
        webClient = WebClient.create("http://localhost:5000");
        webClient.post()
                .uri("/data")
                .body(Mono.just(message.value()), String.class)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> System.err.println("Error occurred: " + error.getMessage()))
                .subscribe(response -> System.out.println("Response from server: " + response));
        System.out.println("message" + message.value());
    }
}
