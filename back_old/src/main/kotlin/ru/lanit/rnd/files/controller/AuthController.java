package ru.lanit.rnd.files.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.lanit.rnd.files.model.UserCredentials;
import ru.lanit.rnd.files.service.JwtSigner;

import java.util.HashMap;

@RestController
public class AuthController {
    @Autowired
    public JwtSigner jwtSigner;

    private HashMap<String, UserCredentials> users = new HashMap() {{
        put("email@example.com", "pw");
    }};

    @PostMapping("/login")
    public Mono<ResponseEntity<Void>> login(@RequestBody UserCredentials user) {
        ResponseEntity<Object> hz = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return Mono.justOrEmpty(users.get(user.email)).filter(it -> it.password.equals(user.password))
                .map(it -> {
                    System.out.println(it.email);
                    String jwt = jwtSigner.createJwt(it.email);
                    ResponseCookie authCookie = ResponseCookie.fromClientResponse("X-Auth", jwt)
                            .maxAge(3600)
                            .httpOnly(true)
                            .path("/")
                            .secure(false) // в продакшене должно быть true.
                            .build();

                    return ResponseEntity.noContent()
                            .header("Set-Cookie", authCookie.toString()).build();

                });//.switchIfEmpty(Mono.just(hz));

        //.switchIfEmpty(hz);
    }



    public void switchIfEmpty() {
        Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED));
    }
}
