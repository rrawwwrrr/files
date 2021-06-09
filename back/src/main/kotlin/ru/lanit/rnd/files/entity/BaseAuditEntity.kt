package ru.lanit.rnd.files.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseAuditEntity<T> : BaseEntity<T>() {

    @CreatedDate
    var created: LocalDateTime?=null

    @LastModifiedDate
    var modified: LocalDateTime?=null
}