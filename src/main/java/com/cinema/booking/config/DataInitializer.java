package com.cinema.booking.config;

import com.cinema.booking.domain.Movie;
import com.cinema.booking.domain.Screening;
import com.cinema.booking.repository.MovieRepository;
import com.cinema.booking.repository.ScreeningRepository;
import com.cinema.booking.service.OmdbService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(MovieRepository movieRepository,
            ScreeningRepository screeningRepository,
            OmdbService omdbService) {
        return args -> {
            if (movieRepository.count() == 0) {

                // 1. Joker (tt7286456)
                Movie joker = new Movie();
                omdbService.fetchMovieDetails(joker, "tt7286456");
                joker.setAgeRating(18);
                movieRepository.save(joker);

                // 2. Interstellar (tt0816692)
                Movie interstellar = new Movie();
                omdbService.fetchMovieDetails(interstellar, "tt0816692");
                interstellar.setAgeRating(12);
                movieRepository.save(interstellar);

                // 3. Borat (tt0443453)
                Movie borat = new Movie();
                omdbService.fetchMovieDetails(borat, "tt0443453");
                borat.setAgeRating(18);
                movieRepository.save(borat);

                // 4. My Neighbor Totoro (tt0096283)
                Movie totoro = new Movie();
                omdbService.fetchMovieDetails(totoro, "tt0096283");
                totoro.setAgeRating(0);
                movieRepository.save(totoro);

                // 5. Back to the Future (tt0088763)
                Movie backToTheFuture = new Movie();
                omdbService.fetchMovieDetails(backToTheFuture, "tt0088763");
                backToTheFuture.setAgeRating(12);
                movieRepository.save(backToTheFuture);

                // --- Generate Screenings for each movie ---
                Movie[] newMovies = { joker, interstellar, borat, totoro, backToTheFuture };
                double[] prices = { 150.0, 160.0, 120.0, 110.0, 130.0 };

                for (int i = 0; i < newMovies.length; i++) {
                    // Morning screening (20% discount applies via TDD logic during booking)
                    Screening s1 = new Screening();
                    s1.setMovieId(newMovies[i].getId());
                    // Виставляємо фіксовано на квітень 2026, розподіляючи дати через день: 18, 20,
                    // 22, 24 квітня
                    s1.setStartTime(LocalDateTime.of(2026, 4, 18 + (i * 2), 10, 0));
                    s1.setTicketPrice(prices[i]);
                    s1.setTotalSeats(100);
                    s1.setAvailableSeats(100);
                    screeningRepository.save(s1);

                    // Evening screening
                    Screening s2 = new Screening();
                    s2.setMovieId(newMovies[i].getId());
                    s2.setStartTime(LocalDateTime.of(2026, 4, 18 + (i * 2), 19, 30));
                    s2.setTicketPrice(prices[i]);
                    s2.setTotalSeats(80);
                    s2.setAvailableSeats(80);
                    screeningRepository.save(s2);
                }
            }
        };
    }
}
