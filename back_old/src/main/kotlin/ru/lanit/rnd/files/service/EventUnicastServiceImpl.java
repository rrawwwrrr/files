package ru.lanit.rnd.files.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import ru.lanit.rnd.files.model.Event;

@Service
public class EventUnicastServiceImpl implements EventUnicastService {

    private EmitterProcessor<Event> processor = EmitterProcessor.create();

    @Override
    public void onNext(Event next) {
        processor.onNext(next);
    }

    @Override
    public Flux<Event> getMessages() {
        return processor.publish().autoConnect();
    }
}
