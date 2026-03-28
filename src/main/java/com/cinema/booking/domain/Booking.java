package com.cinema.booking.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long screeningId;
    private String customerName;
    private Integer customerAge;
    private Integer ticketsCount;

    @Enumerated(EnumType.STRING)
    private BookingStatus status; // ACTIVE or CANCELLED

    public Booking() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(Long screeningId) {
        this.screeningId = screeningId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getCustomerAge() {
        return customerAge;
    }

    public void setCustomerAge(Integer customerAge) {
        this.customerAge = customerAge;
    }

    public Integer getTicketsCount() {
        return ticketsCount;
    }

    public void setTicketsCount(Integer ticketsCount) {
        validateTicketsCount(ticketsCount);
        this.ticketsCount = ticketsCount;
    }

    private void validateTicketsCount(Integer ticketsCount) {
        if (ticketsCount == null || ticketsCount <= 0 || ticketsCount > 6) {
            throw new IllegalArgumentException("Tickets count must be between 1 and 6");
        }
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public void validateAge(Movie movie) {
        if (isTooYoungFor(movie)) {
            throw new IllegalStateException("Customer age is below the movie age rating");
        }
    }

    private boolean isTooYoungFor(Movie movie) {
        return this.customerAge < movie.getAgeRating();
    }

    public void cancel(LocalDateTime now, LocalDateTime screeningTime) {
        if (Duration.between(now, screeningTime).toMinutes() < 60) {
            throw new IllegalStateException("Cannot cancel booking less than 1 hour before screening");
        }
        this.status = BookingStatus.CANCELLED;
    }
}
