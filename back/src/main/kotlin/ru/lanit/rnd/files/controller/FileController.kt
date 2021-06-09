package ru.lanit.rnd.files.controller

import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.lanit.rnd.files.entity.FileEntity
import ru.lanit.rnd.files.repository.FileRepository
import java.nio.file.Paths

@RestController
@RequestMapping("/api/file")
class FileController(private val fileRepository: FileRepository) {
    @PostMapping("/")
    fun process(): Flux<FileEntity> {
        val file = FileEntity("123123", "12312asdsd", 1)
        fileRepository.save(file)
        return fileRepository.findAll()
    }
}