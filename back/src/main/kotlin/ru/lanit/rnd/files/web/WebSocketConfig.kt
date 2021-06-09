package ru.lanit.rnd.files.web;

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerAdapter
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter

@Configuration
public class WebSocketConfig(private var webSocketHandler: WebSocketHandler) {

    @Bean
    public fun handlerMapping(): HandlerMapping {
        val path: String = "/push";
        val map: MutableMap<String, WebSocketHandler> = mutableMapOf(path to webSocketHandler)
        return SimpleUrlHandlerMapping(map, -1);
    }


    @Bean
    public fun wsHandlerAdapter(): HandlerAdapter {
        return WebSocketHandlerAdapter();
    }
}
