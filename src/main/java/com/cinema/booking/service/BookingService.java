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

@Service
public class BookingService {

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
        Screening screening = screeningRepository.findById(booking.getScreeningId())
                .orElseThrow(() -> new IllegalArgumentException("Screening not found"));

        Movie movie = movieRepository.findById(screening.getMovieId())
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));

        // Викликаємо нашу бізнес-логіку з доменних сутностей
        booking.validateAge(movie);
        screening.bookSeats(booking.getTicketsCount());

        booking.setStatus(BookingStatus.ACTIVE);
        
        // Оновлюємо кількість місць у сеансі та зберігаємо бронювання
        screeningRepository.save(screening);
        return bookingRepository.save(booking);
    }
}
