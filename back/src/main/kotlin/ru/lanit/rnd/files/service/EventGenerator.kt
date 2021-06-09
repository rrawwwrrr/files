package ru.lanit.rnd.files.service;

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.lanit.rnd.files.model.Event
import ru.lanit.rnd.files.repository.FileRepository
import java.util.concurrent.atomic.AtomicInteger

@Component
class EventGenerator(
    private val eventUnicastService: EventUnicastService,
    private val fileRepository: FileRepository
) {

    private val counter: AtomicInteger = AtomicInteger(0);

//    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
//    public fun generateEvent() {
//        fileRepository.findAll().subscribe {
//            val count: Int = counter.getAndIncrement();
//            val event: Event = Event("add", it);
//            eventUnicastService.onNext(event);
//        }
//
//    }
}
