package com.company.airport.entity;

import io.jmix.core.MetadataTools;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@JmixEntity
@Table(name = "TICKET", indexes = {
        @Index(name = "IDX_TICKET_FLIGHT", columnList = "FLIGHT_ID"),
        @Index(name = "IDX_TICKET_PASSENGER", columnList = "PASSENGER_ID")
})
@Entity
public class Ticket {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "PAID")
    private Boolean paid;

    @Column(name = "TICKET_NUMBER", nullable = false)
    @NotNull
    private String ticketNumber;

    @JoinColumn(name = "FLIGHT_SEAT_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private FlightSeat flightSeat;

    public FlightSeat getFlightSeat() {
        return flightSeat;
    }

    public void setFlightSeat(FlightSeat flightSeat) {
        this.flightSeat = flightSeat;
    }

    @JoinColumn(name = "FLIGHT_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Flight flight;

    @JoinColumn(name = "PASSENGER_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Passenger passenger;

    @Column(name = "PRICE", precision = 19, scale = 2)
    private BigDecimal price;

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @InstanceName
    @DependsOnProperties({"ticketNumber"})
    public String getInstanceName(MetadataTools metadataTools) {
        return metadataTools.format(ticketNumber);
    }
}