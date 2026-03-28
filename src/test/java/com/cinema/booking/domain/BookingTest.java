package com.cinema.booking.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Test
    void setTicketsCount_ShouldThrowException_WhenMoreThan6Tickets() {
        // Arrange
        Booking booking = new Booking();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> booking.setTicketsCount(7),
                "Should not allow more than 6 tickets per booking");
        assertThrows(IllegalArgumentException.class, () -> booking.setTicketsCount(10),
                "Should not allow 10 tickets per booking");
    }

    @Test
    void setTicketsCount_ShouldSucceed_WhenTicketsCountIsValid() {
        // Arrange
        Booking booking = new Booking();

        // Act & Assert
        assertDoesNotThrow(() -> booking.setTicketsCount(1));
        assertDoesNotThrow(() -> booking.setTicketsCount(6));
    }

    @Test
    void validateAge_ShouldThrowException_WhenCustomerIsTooYoung() {
        // Arrange
        Booking booking = new Booking();
        booking.setCustomerAge(15);
        Movie movie = new Movie();
        movie.setAgeRating(18);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> booking.validateAge(movie),
                "Should throw when customer is younger than movie age rating");
    }

    @Test
    void validateAge_ShouldSucceed_WhenCustomerMeetsAgeRating() {
        // Arrange
        Booking booking = new Booking();
        booking.setCustomerAge(18);
        Movie movie = new Movie();
        movie.setAgeRating(18);

        // Act & Assert
        assertDoesNotThrow(() -> booking.validateAge(movie),
                "Should not throw when customer meets age rating");
    }

    @Test
    void cancel_ShouldThrowException_WhenLessTimeRemainingThenOneHour() {
        // Arrange
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.ACTIVE);
        LocalDateTime now = LocalDateTime.of(2024, 6, 15, 10, 0);
        LocalDateTime screeningTime = LocalDateTime.of(2024, 6, 15, 10, 30); // 30 хв до сеансу

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> booking.cancel(now, screeningTime),
                "Should not allow cancellation less than 1 hour before screening");
    }

    @Test
    void cancel_ShouldSetStatusToCancelled_WhenMoreThanOneHourRemaining() {
        // Arrange
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.ACTIVE);
        LocalDateTime now = LocalDateTime.of(2024, 6, 15, 10, 0);
        LocalDateTime screeningTime = LocalDateTime.of(2024, 6, 15, 12, 0); // 2 години до сеансу

        // Act
        booking.cancel(now, screeningTime);

        // Assert
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }
}
