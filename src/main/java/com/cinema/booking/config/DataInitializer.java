package com.cinema.booking.config;

import com.cinema.booking.domain.Movie;
import com.cinema.booking.repository.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(MovieRepository movieRepository) {
        return args -> {
            if (movieRepository.count() == 0) {
                Movie movie1 = new Movie();
                movie1.setTitle("Interstellar");
                movie1.setDescription("A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.");
                movie1.setAgeRating(12);
                movieRepository.save(movie1);

                Movie movie2 = new Movie();
                movie2.setTitle("The Dark Knight");
                movie2.setDescription("When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.");
                movie2.setAgeRating(16);
                movieRepository.save(movie2);

                Movie movie3 = new Movie();
                movie3.setTitle("Inception");
                movie3.setDescription("A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.");
                movie3.setAgeRating(12);
                movieRepository.save(movie3);
            }
        };
    }
}
