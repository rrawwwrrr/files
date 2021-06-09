package ru.lanit.rnd.files.controller;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.lanit.rnd.files.entity.FileEntity
import ru.lanit.rnd.files.service.FileUploadService

import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
class UploadController(public val fileUploadService: FileUploadService) {

    @PostMapping("/upload")
    fun process(@RequestPart("files") filePartFlux: Flux<FilePart>): Flux<FileEntity> {
        return fileUploadService.upload(filePartFlux);
    }

}
