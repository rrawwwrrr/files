package ru.lanit.rnd.files.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import ru.lanit.rnd.files.model.Event;

@Service
public class EventUnicastServiceImpl : EventUnicastService {

    private val processor: EmitterProcessor<Event> = EmitterProcessor.create();

    public override fun onNext(next: Event) {
        processor.onNext(next);
    }

    public override fun getMessages(): Flux<Event> {
        return processor.publish().autoConnect();
    }
}
