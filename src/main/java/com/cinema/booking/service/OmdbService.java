package com.cinema.booking.service;

import com.cinema.booking.domain.Movie;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OmdbService {

    private final String API_KEY = "14639dbb";
    private final String OMDB_URL = "https://www.omdbapi.com/?apikey=" + API_KEY + "&i=";

    private final RestTemplate restTemplate;

    public OmdbService() {
        this.restTemplate = new RestTemplate();
    }

    public void fetchMovieDetails(Movie movie, String imdbId) {
        String url = OMDB_URL + imdbId;
        OmdbResponse response = restTemplate.getForObject(url, OmdbResponse.class);

        if (response != null && "True".equals(response.response)) {
            movie.setTitle(response.title);
            movie.setYear(response.year);
            movie.setPosterUrl(response.poster);
            movie.setRuntime(response.runtime);
            movie.setGenre(response.genre);
            movie.setDescription(response.plot);
        } else {
            throw new IllegalArgumentException("Could not fetch details for IMDB ID: " + imdbId);
        }
    }

    private static class OmdbResponse {
        @JsonProperty("Title")
        public String title;
        @JsonProperty("Year")
        public String year;
        @JsonProperty("Poster")
        public String poster;
        @JsonProperty("Runtime")
        public String runtime;
        @JsonProperty("Genre")
        public String genre;
        @JsonProperty("Plot")
        public String plot;
        @JsonProperty("Response")
        public String response;
    }
}
