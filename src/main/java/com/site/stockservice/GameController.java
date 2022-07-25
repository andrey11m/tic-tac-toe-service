package com.site.stockservice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService service;
    private final AudioClientWebClient audioClientWebClient;

    @GetMapping(value = "/game", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<Game> game() {
        return service.geFluxGame();
    }
    @GetMapping(value = "/voice", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<Game> voice() {

        return service.geFluxGame();
    }



    @GetMapping("/set-symbol/{id}")
    public String setSymbol(@PathVariable int id) {
        service.setSymbol(id);
        return "Successes";
    }

    @GetMapping("/restart")
    public String restart() {
        service.createNewGame();
        return "Successes restart";
    }

    @GetMapping(value = "/audio", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<byte[]> audio() {
        return Flux.create(sink ->
                audioClientWebClient.startListeningToAudio().subscribe(new BaseSubscriber<byte[]>() {
                    @Override
                    protected void hookOnNext(byte[] value) {
                        sink.next(value);
                    }

                    @Override
                    protected void hookOnComplete() {
                        sink.complete();
                    }
                })
        );
    }
}
