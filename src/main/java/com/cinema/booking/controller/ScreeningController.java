package com.cinema.booking.controller;

import com.cinema.booking.domain.Movie;
import com.cinema.booking.domain.Screening;
import com.cinema.booking.repository.MovieRepository;
import com.cinema.booking.repository.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ScreeningController {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public ScreeningController(ScreeningRepository screeningRepository,
                               MovieRepository movieRepository) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
    }

    @GetMapping("/movies/{movieId}/screenings")
    public String getScreenings(@PathVariable Long movieId, Model model) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + movieId));
        
        List<Screening> screenings = screeningRepository.findByMovieId(movieId);
        
        model.addAttribute("movie", movie);
        model.addAttribute("screenings", screenings);
        
        return "screenings";
    }
}
