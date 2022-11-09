package com.example.vaadinapp.web;

import com.example.vaadinapp.domain.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CompetitorClientTest {

    CompetitorClient client = new CompetitorClient();

    //@Test
    void getAll() {
        WebClient client = WebClient.create("http://localhost:8081");
        System.out.println(client.post()
                .uri(builder -> builder.path("competitor").build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(
                        Mono.just(new UserDto("A", "A", "A", LocalDate.now(), 33, 33)),
                        UserDto.class)
                .retrieve()
                .toBodilessEntity().block().getStatusCode());
    }
}