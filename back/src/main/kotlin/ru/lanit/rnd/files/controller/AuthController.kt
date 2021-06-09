package ru.lanit.rnd.files.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.*
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider
import org.springframework.security.ldap.userdetails.LdapUserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import ru.lanit.rnd.files.entity.UserEntity
import ru.lanit.rnd.files.repository.FileRepository
import ru.lanit.rnd.files.repository.UserRepository
import ru.lanit.rnd.files.service.JwtSigner
import ru.lanit.rnd.files.service.LdapService


data class User(val email: String)

data class UserCredentials(val email: String, val password: String)

@RestController
class AuthController(
    private val ldapService: LdapService
) {

    @PostMapping("/login")
    fun login(@RequestBody user: UserCredentials): Mono<ResponseEntity<String>> {
        return ldapService.auth(UsernamePasswordAuthenticationToken(user.email, user.password))
            .flatMap {
                Mono.just(ResponseEntity.ok().body("{\"token\":\"${it}\"}"))
            }
            .onErrorResume {
                Mono.just(ResponseEntity.status(500).body("{\"error\":\"${it.localizedMessage}\"}"))
            }
    }
}