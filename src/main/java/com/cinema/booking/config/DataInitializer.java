package com.cinema.booking.config;

import com.cinema.booking.domain.Movie;
import com.cinema.booking.domain.Screening;
import com.cinema.booking.repository.MovieRepository;
import com.cinema.booking.repository.ScreeningRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(MovieRepository movieRepository, 
                                      ScreeningRepository screeningRepository) {
        return args -> {
            if (movieRepository.count() == 0) {
                // Movies
                Movie interstellar = new Movie();
                interstellar.setTitle("Interstellar");
                interstellar.setDescription("A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.");
                interstellar.setAgeRating(12);
                movieRepository.save(interstellar);

                Movie darkKnight = new Movie();
                darkKnight.setTitle("The Dark Knight");
                darkKnight.setDescription("When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.");
                darkKnight.setAgeRating(16);
                movieRepository.save(darkKnight);

                // Sample Screenings for Interstellar
                Screening s1 = new Screening();
                s1.setMovieId(interstellar.getId());
                s1.setStartTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
                s1.setTicketPrice(150.0);
                s1.setTotalSeats(100);
                s1.setAvailableSeats(100);
                screeningRepository.save(s1);

                Screening s2 = new Screening();
                s2.setMovieId(interstellar.getId());
                s2.setStartTime(LocalDateTime.now().plusDays(1).withHour(19).withMinute(0));
                s2.setTicketPrice(200.0);
                s2.setTotalSeats(100);
                s2.setAvailableSeats(100);
                screeningRepository.save(s2);
                
                // Sample Screening for Dark Knight
                Screening s3 = new Screening();
                s3.setMovieId(darkKnight.getId());
                s3.setStartTime(LocalDateTime.now().plusDays(1).withHour(21).withMinute(30));
                s3.setTicketPrice(220.0);
                s3.setTotalSeats(80);
                s3.setAvailableSeats(80);
                screeningRepository.save(s3);
            }
        };
    }
}
