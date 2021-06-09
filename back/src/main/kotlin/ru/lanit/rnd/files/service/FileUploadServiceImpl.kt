package ru.lanit.rnd.files.service;

import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.lanit.rnd.files.entity.FileEntity
import ru.lanit.rnd.files.repository.FileRepository
import java.nio.file.Files
import java.nio.file.Paths

@Service
public class FileUploadServiceImpl(
    private val fileRepository: FileRepository
) : FileUploadService {
    val TEMP_PATH = "/tmp/java/"
    val userId = 1;
    val random = "Q4534NMASD0234JKASDF94ASDASDCZ"
    public override fun upload(filePartFlux: Flux<FilePart>): Flux<FileEntity> {
//        val path: String = TEMP_PATH + userId + "/" + random + "/";
        val path: String = TEMP_PATH;
        Files.createDirectories(Paths.get(path));
        return filePartFlux
            .flatMap {
                val fullPath = path + it.filename();
                it.transferTo(Paths.get(fullPath))
                fileRepository.save(FileEntity(it.filename(), fullPath, userId))
            }
    }
}