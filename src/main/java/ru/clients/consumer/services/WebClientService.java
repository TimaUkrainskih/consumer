package ru.clients.consumer.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WebClientService {

    public void postData(String task) {
        WebClient webClient = WebClient.create("http://localhost:5000");
        webClient.post()
                .uri("/data")
                .body(Mono.just(task), String.class)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> System.err.println("Error occurred: " + error.getMessage()))
                .subscribe(response -> System.out.println("Response from server: " + response));
    }
}
