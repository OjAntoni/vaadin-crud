package com.example.vaadinapp.web;

import com.example.vaadinapp.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@NoArgsConstructor
public class CompetitorClient {
    private WebClient client = WebClient.create("http://pamiwserver-env.eba-2bpp63gm.eu-central-1.elasticbeanstalk.com");

    private ObjectMapper objectMapper = new ObjectMapper();

    public List<User> getAll(){
        Mono<List<User>> listMono = client.get()
                .uri(builder -> builder.path("competitors").queryParam("perPage", 120).
                        queryParam("page", 0).build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
        System.out.println(listMono.block());
        return listMono.block();
    }
}
