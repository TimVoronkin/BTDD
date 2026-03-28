package com.cinema.booking.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long screeningId;
    private String customerName;
    private Integer customerAge;
    private Integer ticketsCount;
    private String status; // ACTIVE or CANCELLED

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
}
