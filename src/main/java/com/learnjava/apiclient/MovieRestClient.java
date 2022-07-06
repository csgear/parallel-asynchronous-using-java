/*
 * Copyright (c) 2022 HyTrust Inc. All rights reserved.
 */

package com.learnjava.apiclient;

import com.learnjava.domain.movie.Movie;
import com.learnjava.domain.movie.MovieInfo;
import com.learnjava.domain.movie.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * @author Xiaojun.Yang
 */
@RequiredArgsConstructor
public class MovieRestClient {
    private final RestTemplate restTemplate ;
    private final String BASE_URL = "http://localhost:8080/movies" ;

    public Movie retrieveMovie(Long movieInfoId) {
        var movieInfo = invokeMovieInfoService(movieInfoId);
        var reviews = invokeReviewsService(movieInfoId);
        return new Movie(movieInfo, reviews);
    }

    private List<Review> invokeReviewsService(Long movieInfoId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<List<Review>> entity = new HttpEntity<>(headers);

        String uri = UriComponentsBuilder.fromUriString(BASE_URL + "/v1/reviews")
                .queryParam("movieInfoId", movieInfoId)
                .buildAndExpand()
                .toUriString();

        return restTemplate.exchange(uri,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<List<Review>>(){})
                .getBody();
    }

    private MovieInfo invokeMovieInfoService(Long movieInfoId) {
        var movieInfoUrlPath = BASE_URL + "/v1/movie_infos/{movieInfoId}";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<MovieInfo> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(movieInfoUrlPath, HttpMethod.GET, entity, MovieInfo.class,
                        movieInfoId)
                .getBody();
    }
}
