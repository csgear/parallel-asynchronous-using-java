package com.learnjava.apiclient;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Xiaojun.Yang
 */
class MovieRestClientTest {
    private final RestTemplate restTemplate = new RestTemplate() ;
    private final MovieRestClient movieRestClient = new MovieRestClient(restTemplate);


    @Test
    void retrieveMovie() {
        var movieInfoId = 1L;
        var movie = movieRestClient.retrieveMovie(movieInfoId);

        assert movie != null;
        assertEquals("Batman Begins", movie.getMovieInfo().getName());
        assert movie.getReviewList().size() == 1;
    }
}