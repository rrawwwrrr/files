package ru.lanit.rnd.files.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import reactor.core.publisher.Mono
import ru.lanit.rnd.files.service.JwtSigner

@Configuration
class SecurityConfiguration {

    @Bean
    fun jwtAuthenticationConverter(): ServerAuthenticationConverter {
        return ServerAuthenticationConverter { exchange ->
            Mono.justOrEmpty(exchange)
                .flatMap {
                    var auth = it.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
                    auth = if (auth != null && auth.startsWith("Bearer ")) auth.substring(7) else null;
                    Mono.justOrEmpty(auth)
                }
                .filter { it.isNotEmpty() }
                .map {
                    UsernamePasswordAuthenticationToken(it, it)
                }
        }
    }

    @Bean
    fun jwtAuthenticationManager(jwtSigner: JwtSigner): ReactiveAuthenticationManager {
        return ReactiveAuthenticationManager { authentication ->
            Mono.justOrEmpty(authentication)
                .map {
                    jwtSigner.validateJwt(it.credentials as String)
                }
                .map { jws ->
                    UsernamePasswordAuthenticationToken(
                        jws.body.subject,
                        authentication.credentials as String,
                        mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
                    )
                }
        }
    }

    @Bean
    fun authenticationFailureHandler(): ServerAuthenticationFailureHandler {
        return ServerAuthenticationFailureHandler { webFilterExchange, exception ->
            val response = webFilterExchange.exchange.response
            println("asd")
            response.apply {
                statusCode = HttpStatus.OK
                headers.contentType = MediaType.APPLICATION_JSON
//                headers.set(HttpHeaders.WARNING, exception.localizedMessage)

            }
            Mono.empty();
        }
    }

    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        jwtAuthenticationManager: ReactiveAuthenticationManager,
        jwtAuthenticationConverter: ServerAuthenticationConverter,
        authenticationFailureHandler: ServerAuthenticationFailureHandler
    ): SecurityWebFilterChain {
        val authenticationWebFilter = AuthenticationWebFilter(jwtAuthenticationManager)
        authenticationWebFilter.setServerAuthenticationConverter(jwtAuthenticationConverter)
        authenticationWebFilter.setAuthenticationFailureHandler(authenticationFailureHandler)
        return http.authorizeExchange()
            .pathMatchers("/login")
            .permitAll()
            .pathMatchers("/api/**")
            .authenticated()
            .and()
            .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .httpBasic()
            .disable()
            .csrf()
            .disable()
            .formLogin()
            .disable()
            .logout()
            .disable()
            .build()
    }
}