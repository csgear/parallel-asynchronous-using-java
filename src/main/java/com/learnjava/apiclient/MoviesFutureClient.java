package com.learnjava.apiclient;

import com.learnjava.domain.movie.Movie;
import com.learnjava.domain.movie.MovieInfo;
import com.learnjava.domain.movie.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author csgear
 */
@RequiredArgsConstructor
public class MoviesFutureClient {
    private final WebClient webClient ;

    public Movie retrieveMovie(Long movieInfoId) {
        var movieInfo = invokeMovieInfoService(movieInfoId);
        var reviews = invokeReviewsService(movieInfoId);
        return new Movie(movieInfo, reviews);
    }

    public CompletableFuture<Movie> retrieveMovieAsync(Long movieInfoId) {

        var movieInfo = CompletableFuture.supplyAsync(() -> invokeMovieInfoService(movieInfoId));
        var reviews = CompletableFuture.supplyAsync(() -> invokeReviewsService(movieInfoId));
        return movieInfo.thenCombine(reviews, Movie::new);
    }

    public List<Movie> retrieveMovieListAsync(List<Long> movieInfoIds) {
        var movieFutures = movieInfoIds
                .stream()
                .parallel()
                .map(this::retrieveMovieAsync)
                .collect(Collectors.toList());

        return movieFutures
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

        public List<Movie> retrieveMovieList(List<Long> movieInfoIds) {
        return movieInfoIds
                .stream()
                //.parallel()
                .map(this::retrieveMovie)
                .collect(Collectors.toList());
    }


    private List<Review> invokeReviewsService(Long movieInfoId) {
        String uri = UriComponentsBuilder.fromUriString("/v1/reviews")
                .queryParam("movieInfoId", movieInfoId)
                .buildAndExpand()
                .toUriString();

        return webClient.get().uri(uri)
                .retrieve()
                .bodyToFlux(Review.class)
                .collectList()
                .block();
    }

    private MovieInfo invokeMovieInfoService(Long movieInfoId) {
        var movieInfoUrlPath = "/v1/movie_infos/{movieInfoId}";

        return webClient.get()
                .uri(movieInfoUrlPath, movieInfoId)
                .retrieve()
                .bodyToMono(MovieInfo.class)
                .block();
    }

}
