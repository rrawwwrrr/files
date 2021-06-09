package ru.lanit.rnd.files.config;

import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
class NettyWebServerFactoryPortCustomizer : WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

    override fun customize(serverFactory: NettyReactiveWebServerFactory) {
        serverFactory.setPort(8080);
    }
}