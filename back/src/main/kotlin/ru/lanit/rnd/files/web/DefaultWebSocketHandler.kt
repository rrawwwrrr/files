package ru.lanit.rnd.files.web;

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.lanit.rnd.files.service.EventUnicastService

@Component
public class DefaultWebSocketHandler(
    private val eventUnicastService: EventUnicastService,
    private val objectMapper: ObjectMapper
) : WebSocketHandler {

    public override fun handle(session: WebSocketSession): Mono<Void> {
        val messages: Flux<WebSocketMessage> =
        session.receive()
            .flatMap {
                // or read message here
                eventUnicastService.getMessages()
            }
            .flatMap {
                try {
                    Mono.just(objectMapper.writeValueAsString(it))
                } catch (e: JsonProcessingException) {
                    Mono.error(e)
                }
            }.map(session::textMessage)
        return session.send(messages)
    }
}
