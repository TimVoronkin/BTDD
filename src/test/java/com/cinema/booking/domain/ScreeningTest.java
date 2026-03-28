package com.cinema.booking.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
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

    @Test
    void getEffectivePrice_ShouldApply20PercentDiscount_WhenScreeningIsMorning() {
        // Arrange
        Screening screening = new Screening();
        screening.setTicketPrice(100.0);
        screening.setStartTime(LocalDateTime.of(2024, 6, 15, 10, 0)); // 10:00 — ранковий

        // Act
        double price = screening.getEffectivePrice();

        // Assert
        assertEquals(80.0, price, 0.001, "Morning screening should cost 80% of base price");
    }

    @Test
    void getEffectivePrice_ShouldReturnFullPrice_WhenScreeningIsAfterNoon() {
        // Arrange
        Screening screening = new Screening();
        screening.setTicketPrice(100.0);
        screening.setStartTime(LocalDateTime.of(2024, 6, 15, 14, 0)); // 14:00 — денний

        // Act
        double price = screening.getEffectivePrice();

        // Assert
        assertEquals(100.0, price, 0.001, "Afternoon screening should cost full price");
    }
}
