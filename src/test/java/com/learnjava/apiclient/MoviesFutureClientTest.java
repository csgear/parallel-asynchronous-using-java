package com.learnjava.apiclient;

import com.learnjava.util.CommonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoviesFutureClientTest {

    WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080/movies").build();
    MoviesFutureClient moviesFutureClient = new MoviesFutureClient(webClient);

    @BeforeEach
    void setUp() {
        CommonUtil.stopWatchReset();
    }

    @Test
    void testRetrieveMovie() {
        var movieInfoId = 1L;
        var movie = moviesFutureClient.retrieveMovie(movieInfoId);

        assert movie != null;
        assertEquals("Batman Begins", movie.getMovieInfo().getName());
        assert movie.getReviewList().size() == 1;
    }

    @Test
    void retrieveMovieAsync() {
    }

    @Test
    void retrieveMovieListAsync() {
        var movieInfoIds = List.of(1L,2L, 3L , 4L, 5L, 6L, 7L);

        CommonUtil.startTimer();
        var movies = moviesFutureClient.retrieveMovieListAsync(movieInfoIds);
        CommonUtil.timeTaken();

        assert movies.size() == 7;
    }

    @Test
    void retrieveMovieList() {
        var movieInfoIds = List.of(1L,2L, 3L , 4L, 5L, 6L, 7L);

        CommonUtil.startTimer();
        var movies = moviesFutureClient.retrieveMovieList(movieInfoIds);
//        System.out.println("movies : " + movies);
        CommonUtil.timeTaken();

        assert movies.size() == 7;
    }
}