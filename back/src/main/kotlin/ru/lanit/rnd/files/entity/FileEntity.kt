package ru.lanit.rnd.files.entity

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import javax.persistence.Entity

//import javax.persistence.JoinColumn
//import javax.persistence.ManyToOne

@Entity
@Table("files")
class FileEntity(

    val filename: String,
    var path: String? = null,
    @Column("user_id")
    val userId: Int
//    @ManyToOne
//    @JoinColumn(name = "user_id")

) : BaseAuditEntity<Int>()