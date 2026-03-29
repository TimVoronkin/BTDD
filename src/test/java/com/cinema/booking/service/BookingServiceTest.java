package com.cinema.booking.service;

import com.cinema.booking.domain.Booking;
import com.cinema.booking.domain.BookingStatus;
import com.cinema.booking.domain.Screening;
import com.cinema.booking.repository.BookingRepository;
import com.cinema.booking.repository.ScreeningRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ScreeningRepository screeningRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cancelBookingByReference_Success() {
        String email = "test@test.com";
        String ref = "AABB";
        
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setCustomerEmail(email);
        booking.setBookingReference(ref);
        booking.setScreeningId(10L);
        booking.setStatus(BookingStatus.ACTIVE);
        booking.setTicketsCount(2);

        Screening screening = new Screening();
        screening.setId(10L);
        screening.setStartTime(LocalDateTime.now().plusHours(5));
        screening.setAvailableSeats(50);

        when(bookingRepository.findByBookingReference(ref)).thenReturn(Optional.of(booking));
        when(screeningRepository.findById(10L)).thenReturn(Optional.of(screening));

        bookingService.cancelBookingByReference(email, ref);

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        assertEquals(52, screening.getAvailableSeats());
        verify(bookingRepository).save(booking);
        verify(screeningRepository).save(screening);
    }
    
    @Test
    void cancelBookingByReference_InvalidEmail_ThrowsIllegalArgumentException() {
        String email = "hacker@hacker.com"; // Wrong email
        String ref = "AABB"; // Correct reference
        
        Booking booking = new Booking();
        booking.setCustomerEmail("real@owner.com");
        booking.setBookingReference(ref);

        when(bookingRepository.findByBookingReference(ref)).thenReturn(Optional.of(booking));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.cancelBookingByReference(email, ref);
        });

        assertEquals("Invalid email for this booking reference.", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }
}
