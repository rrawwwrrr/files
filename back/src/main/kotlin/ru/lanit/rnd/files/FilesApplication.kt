package ru.lanit.rnd.files;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EnableScheduling
public class FilesApplication

fun main(args: Array<String>) {
    runApplication<FilesApplication>(*args)
}

