package ru.lanit.rnd.files.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.lanit.rnd.files.model.Event;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class EventGenerator {

    private AtomicInteger counter = new AtomicInteger(0);

    private EventUnicastService eventUnicastService;

    @Autowired
    public EventGenerator(EventUnicastService eventUnicastService) {
        this.eventUnicastService = eventUnicastService;
    }

    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    public void generateEvent() {
        int count = counter.getAndIncrement();
        Event event = new Event("event", count);
        eventUnicastService.onNext(event);
    }
}
