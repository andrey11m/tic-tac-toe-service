package com.site.stockservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class AudioClientWebClient {

    private final WebClient webClient;

    public Flux<byte[]> startListeningToAudio() {
        return webClient
                .get()
                .uri("http://localhost:8090/audio-stream")
                .retrieve()
                .bodyToFlux(byte[].class)
                .retry().doOnError(IOException.class, e -> log.error(e.getMessage()));
    }
}
