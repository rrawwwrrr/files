package ru.lanit.rnd.files.service;

import reactor.core.publisher.Flux;
import ru.lanit.rnd.files.model.Event;

public interface EventUnicastService {

    fun onNext(next: Event);
    fun getMessages(): Flux<Event>;
}
