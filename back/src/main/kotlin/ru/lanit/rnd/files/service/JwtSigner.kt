package ru.lanit.rnd.files.service;

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.Authentication
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.lanit.rnd.files.entity.UserEntity
import ru.lanit.rnd.files.repository.UserRepository
import java.security.KeyPair
import java.time.Duration
import java.time.Instant
import java.util.*

@Service
class JwtSigner(private val userRepository: UserRepository) {
    val keyPair: KeyPair = Keys.keyPairFor(SignatureAlgorithm.RS256)

    fun createJwt(auth: Mono<Authentication>): Mono<String> {
        return auth
            .flatMap {
                val details = (it.principal as LdapUserDetailsImpl);
                val fullName =
                    details.dn.split(",").stream().filter { it.startsWith("CN") }
                        .map { it.replace("CN=", "") }.findFirst()
                val login = details.username
                userRepository.findFirstByLogin(login)
                    .switchIfEmpty(
                        userRepository.save(UserEntity(login, fullName.get(), login + "@lanit.ru"))
                    )
            }
            .flatMap {
                Mono.just(
                    Jwts.builder()
                        .signWith(keyPair.private, SignatureAlgorithm.RS256)
                        .setSubject(it.login)
                        .claim("fullName", it.fullName)
                        .setIssuer("identity")
                        .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(12))))
                        .setIssuedAt(Date.from(Instant.now()))
                        .compact()
                )
            }

    }

    /**
     * Проверить JWT там, где он будет выбрасывать исключения, не являясь допустимым.
     */
    fun validateJwt(jwt: String): Jws<Claims> {
        return Jwts.parserBuilder()
            .setSigningKey(keyPair.public)
            .build()
            .parseClaimsJws(jwt)
    }
}