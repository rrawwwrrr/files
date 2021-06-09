package ru.lanit.rnd.files

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import ru.lanit.rnd.files.controller.User
import ru.lanit.rnd.files.controller.UserCredentials

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

    @LocalServerPort
    var serverPort: Int? = null

    @Test
    fun `can obtain own user details when logged in`() {
        // упорядочивание
        val webClient = WebClient.builder()
            .baseUrl("http://localhost:${serverPort}")
            .build()

        // действие
//        webClient.put()
//            .uri("/user/signup")
//            .bodyValue(UserCredentials("new@example.com", "pw"))
//            .exchange()
//            .block()
        val loginResponse = webClient.post()
            .uri("/login")
            .bodyValue(UserCredentials("email@example.com", "pw"))
            .exchange()
            .block() ?: throw RuntimeException("Should have gotten a response")
        val responseCookies = loginResponse.cookies()
            .map { it.key to it.value.map { cookie -> cookie.value } }
            .toMap()

        val response = webClient.get()
            .uri("/user")
            .cookies { it.addAll(LinkedMultiValueMap(responseCookies)) }
            .exchange()
            .block()

        // утверждение
        assertThat(response?.statusCode()).isEqualTo(HttpStatus.OK)
        assertThat(response?.bodyToFlux(User::class.java)?.blockFirst()).isEqualTo(User("new@example.com"))
    }
}