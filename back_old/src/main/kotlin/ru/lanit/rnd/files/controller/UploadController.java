package ru.lanit.rnd.files.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.lanit.rnd.files.service.FileUploadService;

import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UploadController {
    private final FileUploadService fileUploadService;

    @Autowired
    public UploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    // use Flux<FilePart> for multiple file upload
    @PostMapping(value = "/upload-flux", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<List<String>> upload(@RequestPart("files") Flux<FilePart> filePartFlux) {
        filePartFlux.flatMap(it -> it.transferTo(Paths.get("/tmp/java/" + realName(it.filename()))))
                .then(Mono.just("OK"));
        return fileUploadService.getLines(filePartFlux).collectList();
    }

    @PostMapping("/upload")
    public Mono<String> process(@RequestPart("files") Flux<FilePart> filePartFlux) {
        return filePartFlux.flatMap(it -> it.transferTo(Paths.get("/tmp/java/" + realName(it.filename()))))
                .then(Mono.just("OK"));
    }

    public String realName(String name) {
        System.out.println(name);
        return name;
    }

    // use single Mono<FilePart> for single file upload
    @PostMapping(value = "/upload-mono", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<List<String>> upload(@RequestPart("file") Mono<FilePart> filePartMono) {
        return fileUploadService.getLines(filePartMono).collectList();
    }

    // use single FilePart for single file upload
    @PostMapping(value = "/upload-filePart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<List<String>> upload(@RequestPart("file") FilePart filePart) {
        return fileUploadService.getLines(filePart).collectList();
    }

    // use Mono<MultiValueMap<String, Part>> for both single and multiple file upload under `files` param key
    @PostMapping(value = "/upload-multiValueMap", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<List<String>> uploadFileMap(@RequestBody Mono<MultiValueMap<String, Part>> filePartMap) {
        return fileUploadService.getLinesFromMap(filePartMap).collectList();
    }
}
