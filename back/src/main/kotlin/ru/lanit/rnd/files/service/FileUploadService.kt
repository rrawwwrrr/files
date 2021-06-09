package ru.lanit.rnd.files.service;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.lanit.rnd.files.entity.FileEntity

public interface FileUploadService {
    // this is for multiple file upload
    fun upload(filePartFlux: Flux<FilePart>): Flux<FileEntity>

}