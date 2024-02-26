package edu.java.bot.controller;

import edu.java.bot.model.dto.request.AddLinkRequest;
import edu.java.bot.model.dto.request.RemoveLinkRequest;
import edu.java.bot.model.dto.response.ApiErrorResponse;
import edu.java.bot.model.dto.response.MyResponse;
import edu.java.bot.service.client.ScrapperHttpClient;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class for_test {
    private ScrapperHttpClient client;

    @GetMapping("/1")
    public Mono<Optional<ApiErrorResponse>> t1(){
       return client.makeChat(1L);
    }
    @GetMapping("/2")
    public Mono<Optional<ApiErrorResponse>> t2(){
        return client.deleteChat(1L);
    }
    @GetMapping("/3")
    public Mono<? extends MyResponse> t3(){

        return client.trackLink(new AddLinkRequest("aboba"),1L);
    }
    @GetMapping("/4")
    public Mono<? extends MyResponse> t4(){

        return client.getLinks(1L);
    }
    @GetMapping("/5")
    public Mono<? extends MyResponse> t5(){

        return client.unTrackLink(new RemoveLinkRequest("aboba"),1L);
    }
}
