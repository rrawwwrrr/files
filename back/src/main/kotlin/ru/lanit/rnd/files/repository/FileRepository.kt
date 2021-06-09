package ru.lanit.rnd.files.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import ru.lanit.rnd.files.entity.FileEntity

interface FileRepository : ReactiveCrudRepository<FileEntity, Long>{

}