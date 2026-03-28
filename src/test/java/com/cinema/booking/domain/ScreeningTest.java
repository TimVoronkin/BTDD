package com.cinema.booking.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScreeningTest {

    @Test
    void setTicketPrice_ShouldThrowException_WhenPriceIsZeroOrNegative() {
        // Arrange
        Screening screening = new Screening();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> screening.setTicketPrice(0.0), "Price should not be zero");
        assertThrows(IllegalArgumentException.class, () -> screening.setTicketPrice(-5.0), "Price should not be negative");
    }

    @Test
    void bookSeats_ShouldThrowException_WhenNotEnoughSeatsAvailable() {
        // Arrange
        Screening screening = new Screening();
        screening.setAvailableSeats(3);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> screening.bookSeats(5),
                "Should throw when requested seats exceed available seats");
    }

    @Test
    void bookSeats_ShouldDecreaseAvailableSeats_WhenEnoughSeatsExist() {
        // Arrange
        Screening screening = new Screening();
        screening.setAvailableSeats(10);

        // Act
        screening.bookSeats(3);

        // Assert
        assertEquals(7, screening.getAvailableSeats());
    }
}
