package com.cinema.booking.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScreeningTest {

    @Test
    void setTicketPrice_ShouldThrowException_WhenPriceIsZeroOrNegative() {
        // Arrange
        Screening screening = new Screening();
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> screening.setTicketPrice(0.0), "Price should not be zero");
        assertThrows(IllegalArgumentException.class, () -> screening.setTicketPrice(-5.0), "Price should not be negative");
    }
}
