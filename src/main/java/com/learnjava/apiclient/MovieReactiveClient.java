/*
 * Copyright (c) 2022 HyTrust Inc. All rights reserved.
 */

package com.learnjava.apiclient;

import com.learnjava.domain.movie.Movie;
import com.learnjava.domain.movie.MovieInfo;
import com.learnjava.domain.movie.Review;
import com.learnjava.util.RetryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Xiaojun.Yang
 */
@RequiredArgsConstructor
public class MovieReactiveClient {
    private final WebClient webClient;

    public Mono<Movie> retrieveMovieById(String movieId){
        return this.retrieveMovieInfo(movieId)
                .flatMap(movieInfo -> {
                    var reviewList = this.retrieveReviews(movieId)
                            .collectList();
                    return reviewList.map(reviews -> new Movie(movieInfo, reviews));
                });
    }

    private Mono<MovieInfo> retrieveMovieInfo(String movieId) {
        var url = "/v1/movie_infos/{id}" ;

        return webClient.get()
                .uri(url, movieId)
                .retrieve()
                .bodyToMono(MovieInfo.class)
                .retryWhen(RetryUtil.retrySpec()) ;
    }

    private Flux<Review> retrieveReviews(String movieId){
        var reviewsUrl = "/v1/reviews" ;
        var url = UriComponentsBuilder.fromUriString(reviewsUrl)
                .queryParam("movieInfoId", movieId)
                .buildAndExpand().toString();

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Review.class)
                .retryWhen(RetryUtil.retrySpec()) ;
    }

}
