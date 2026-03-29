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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.cinema.booking.repository.BookingRepository;

@Controller
public class BookingController {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingController(ScreeningRepository screeningRepository,
                             MovieRepository movieRepository,
                             BookingService bookingService,
                             BookingRepository bookingRepository) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
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

    @PostMapping("/screenings/{screeningId}/checkout")
    public String processCheckout(@PathVariable Long screeningId, 
                                  @ModelAttribute Booking booking, 
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        try {
            booking.setScreeningId(screeningId);
            // Викликаємо валідацію TDD перед тим, як переходити на чекаут
            bookingService.validateBooking(booking);
            
            Screening screening = screeningRepository.findById(screeningId).get();
            Movie movie = movieRepository.findById(screening.getMovieId()).get();
            
            model.addAttribute("booking", booking);
            model.addAttribute("screening", screening);
            model.addAttribute("movie", movie);
            model.addAttribute("totalPrice", screening.getEffectivePrice() * booking.getTicketsCount());
            
            return "checkout";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/screenings/" + screeningId + "/book";
        }
    }

    @PostMapping("/screenings/{screeningId}/book")
    public String confirmBooking(@PathVariable Long screeningId,
                                 @ModelAttribute Booking booking,
                                 RedirectAttributes redirectAttributes) {
        try {
            booking.setScreeningId(screeningId);
            Booking savedBooking = bookingService.createBooking(booking);
            // Передаємо reference у redirect, щоб показати на сторінці Success
            return "redirect:/bookings/" + savedBooking.getId() + "/success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/screenings/" + screeningId + "/book";
        }
    }

    @GetMapping("/bookings/{bookingId}/success")
    public String bookingSuccess(@PathVariable Long bookingId, Model model) {
        bookingRepository.findById(bookingId).ifPresent(b -> {
            model.addAttribute("bookingReference", b.getBookingReference());
            model.addAttribute("customerEmail", b.getCustomerEmail());
        });
        return "booking-success";
    }

    @GetMapping("/manage")
    public String showManageForm() {
        return "manage-booking";
    }

    @PostMapping("/manage/view")
    public String showTicket(@RequestParam String email,
                             @RequestParam String reference,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            String ref = reference.trim().toUpperCase();
            String normalizedEmail = email.trim();

            // Reuse the same secure lookup from BookingService
            // We need to find the booking first — delegate validation to service layer via a lookup
            com.cinema.booking.domain.Booking booking = bookingRepository
                    .findByBookingReference(ref)
                    .orElseThrow(() -> new IllegalArgumentException("No booking found for this reference."));

            if (!booking.getCustomerEmail().equalsIgnoreCase(normalizedEmail)) {
                throw new IllegalArgumentException("Invalid email for this booking reference.");
            }

            Screening screening = screeningRepository.findById(booking.getScreeningId())
                    .orElseThrow(() -> new IllegalArgumentException("Screening not found."));

            Movie movie = movieRepository.findById(screening.getMovieId())
                    .orElseThrow(() -> new IllegalArgumentException("Movie not found."));

            double totalPrice = screening.getEffectivePrice() * booking.getTicketsCount();

            model.addAttribute("booking", booking);
            model.addAttribute("screening", screening);
            model.addAttribute("movie", movie);
            model.addAttribute("totalPrice", totalPrice);

            return "ticket-view";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/manage";
        }
    }

    @PostMapping("/manage/cancel")
    public String cancelByReference(@RequestParam String email,
                                    @RequestParam String reference,
                                    RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelBookingByReference(email.trim(), reference.trim().toUpperCase());
            redirectAttributes.addFlashAttribute("successMessage",
                    "Booking '" + reference.toUpperCase() + "' has been successfully cancelled.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/manage";
    }
}
