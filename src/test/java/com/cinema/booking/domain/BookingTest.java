package com.cinema.booking.domain;

import org.junit.jupiter.api.Test;
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

        // Act & Assert - не повинно кидати виняток
        assertDoesNotThrow(() -> booking.setTicketsCount(1));
        assertDoesNotThrow(() -> booking.setTicketsCount(6));
    }
}
