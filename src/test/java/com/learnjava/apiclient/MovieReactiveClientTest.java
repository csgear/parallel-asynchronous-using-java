package com.learnjava.apiclient;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Xiaojun.Yang
 */
class MovieReactiveClientTest {

    WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080/movies").build();
    MovieReactiveClient movieReactiveClient = new MovieReactiveClient(webClient);

    @Test
    void testRetrieveMovieById() {
        var movieInfoId = "1";
        var moviesInfo = movieReactiveClient.retrieveMovieById(movieInfoId) ;

        StepVerifier.create(moviesInfo)
                .assertNext(movieInfo -> {
                    assertEquals("Batman Begins", movieInfo.getMovieInfo().getName());
                    assertEquals(1, movieInfo.getReviewList().size());
                })
                .verifyComplete() ;
    }
}