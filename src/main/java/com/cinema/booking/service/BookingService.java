package com.cinema.booking.service;

import com.cinema.booking.domain.Booking;
import com.cinema.booking.domain.BookingStatus;
import com.cinema.booking.domain.Movie;
import com.cinema.booking.domain.Screening;
import com.cinema.booking.repository.BookingRepository;
import com.cinema.booking.repository.MovieRepository;
import com.cinema.booking.repository.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.security.SecureRandom;

@Service
public class BookingService {

    private static final String REFERENCE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int REFERENCE_LENGTH = 4;
    private final SecureRandom random = new SecureRandom();

    private final BookingRepository bookingRepository;
    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, 
                          ScreeningRepository screeningRepository,
                          MovieRepository movieRepository) {
        this.bookingRepository = bookingRepository;
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
    }

    @Transactional
    public Booking createBooking(Booking booking) {
        validateBooking(booking);

        Screening screening = screeningRepository.findById(booking.getScreeningId()).get();
        screening.bookSeats(booking.getTicketsCount());
        booking.setStatus(BookingStatus.ACTIVE);
        booking.setBookingReference(generateReference());

        // Оновлюємо кількість місць у сеансі та зберігаємо бронювання
        screeningRepository.save(screening);
        return bookingRepository.save(booking);
    }

    public void validateBooking(Booking booking) {
        if (booking.getTicketsCount() == null || booking.getCustomerAge() == null) {
            throw new IllegalArgumentException("Booking details are incomplete");
        }

        Screening screening = screeningRepository.findById(booking.getScreeningId())
                .orElseThrow(() -> new IllegalArgumentException("Screening not found"));

        Movie movie = movieRepository.findById(screening.getMovieId())
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));

        booking.validateAge(movie);
        
        // Перевіряємо місця, перш ніж їх забронювати
        if (booking.getTicketsCount() > screening.getAvailableSeats()) {
            throw new IllegalStateException("Not enough seats available");
        }
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        Screening screening = screeningRepository.findById(booking.getScreeningId())
                .orElseThrow(() -> new IllegalArgumentException("Screening not found"));

        // Логіка скасування з доменної моделі
        booking.cancel(LocalDateTime.now(), screening.getStartTime());

        // Повертаємо місця в сеанс при скасуванні
        screening.setAvailableSeats(screening.getAvailableSeats() + booking.getTicketsCount());
        
        screeningRepository.save(screening);
        bookingRepository.save(booking);
    }

    public String generateReference() {
        StringBuilder sb = new StringBuilder(REFERENCE_LENGTH);
        for (int i = 0; i < REFERENCE_LENGTH; i++) {
            sb.append(REFERENCE_CHARS.charAt(random.nextInt(REFERENCE_CHARS.length())));
        }
        return sb.toString();
    }

    @Transactional
    public void cancelBookingByReference(String email, String reference) {
        Booking booking = bookingRepository.findByBookingReference(reference)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found for this reference."));

        if (!booking.getCustomerEmail().equalsIgnoreCase(email)) {
            throw new IllegalArgumentException("Invalid email for this booking reference.");
        }

        Screening screening = screeningRepository.findById(booking.getScreeningId())
                .orElseThrow(() -> new IllegalArgumentException("Screening not found"));

        booking.cancel(LocalDateTime.now(), screening.getStartTime());
        screening.setAvailableSeats(screening.getAvailableSeats() + booking.getTicketsCount());

        screeningRepository.save(screening);
        bookingRepository.save(booking);
    }
}
