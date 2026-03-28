package com.cinema.booking.controller;

import com.cinema.booking.domain.Booking;
import com.cinema.booking.domain.Movie;
import com.cinema.booking.domain.Screening;
import com.cinema.booking.repository.MovieRepository;
import com.cinema.booking.repository.ScreeningRepository;
import com.cinema.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BookingController {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final BookingService bookingService;

    @Autowired
    public BookingController(ScreeningRepository screeningRepository,
                             MovieRepository movieRepository,
                             BookingService bookingService) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.bookingService = bookingService;
    }

    @GetMapping("/screenings/{screeningId}/book")
    public String showBookingForm(@PathVariable Long screeningId, Model model) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid screening Id:" + screeningId));
        
        Movie movie = movieRepository.findById(screening.getMovieId())
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));

        Booking booking = new Booking();
        booking.setScreeningId(screeningId);

        model.addAttribute("screening", screening);
        model.addAttribute("movie", movie);
        model.addAttribute("booking", booking);
        model.addAttribute("effectivePrice", screening.getEffectivePrice());
        
        return "booking-form";
    }

    @PostMapping("/screenings/{screeningId}/book")
    public String processBooking(@PathVariable Long screeningId, 
                                 @ModelAttribute Booking booking, 
                                 RedirectAttributes redirectAttributes) {
        try {
            booking.setScreeningId(screeningId);
            // Тут відпрацьовує наша TDD-логіка (валідація віку, місць, тощо)
            Booking savedBooking = bookingService.createBooking(booking);
            return "redirect:/bookings/" + savedBooking.getId() + "/success";
            
        } catch (Exception e) {
            // Якщо якесь правило TDD порушене, ми ловимо це тут і показуємо користувачу
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/screenings/" + screeningId + "/book";
        }
    }

    @GetMapping("/bookings/{bookingId}/success")
    public String bookingSuccess(@PathVariable Long bookingId, Model model) {
        model.addAttribute("bookingId", bookingId);
        return "booking-success";
    }
}
