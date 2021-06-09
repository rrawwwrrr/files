package ru.lanit.rnd.files.service;

import reactor.core.publisher.Flux;
import ru.lanit.rnd.files.model.Event;

public interface EventUnicastService {

    void onNext(Event next);

    Flux<Event> getMessages();
}
