package ru.lanit.rnd.files.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import ru.lanit.rnd.files.entity.UserEntity

interface UserRepository : ReactiveCrudRepository<UserEntity, Long> {

    fun findFirstByLogin(login: String) : Mono<UserEntity>;

}