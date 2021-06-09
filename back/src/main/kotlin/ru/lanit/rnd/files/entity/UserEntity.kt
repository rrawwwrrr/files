package ru.lanit.rnd.files.entity

import org.springframework.data.relational.core.mapping.Table
import javax.persistence.Entity

@Entity
@Table( "users")
class UserEntity(

    val login: String,
    var fullName: String? = null,
    var email: String? = null,


//    @ManyToOne
//    @JoinColumn(name = "department_id")
//    val department: UserEntity
) : BaseAuditEntity<Int>()