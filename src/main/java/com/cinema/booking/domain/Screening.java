package com.cinema.booking.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long movieId;
    private LocalDateTime startTime;
    private Double ticketPrice;
    private Integer totalSeats;
    private Integer availableSeats;

    public Screening() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        validateTicketPrice(ticketPrice);
        this.ticketPrice = ticketPrice;
    }

    private void validateTicketPrice(Double ticketPrice) {
        if (ticketPrice == null || ticketPrice <= 0) {
            throw new IllegalArgumentException("Ticket price must be greater than zero");
        }
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public void bookSeats(int count) {
        if (count > this.availableSeats) {
            throw new IllegalStateException("Not enough seats available");
        }
        this.availableSeats -= count;
    }
}
