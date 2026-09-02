package com.company.airport.entity;

import io.jmix.core.MetadataTools;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

@JmixEntity
@Table(name = "FLIGHT", indexes = {
        @Index(name = "IDX_FLIGHT_AIRLINE", columnList = "AIRLINE_ID"),
        @Index(name = "IDX_FLIGHT_DEPARTURE_AIRPORT", columnList = "DEPARTURE_AIRPORT_ID"),
        @Index(name = "IDX_FLIGHT_ARRIVAL_AIRPORT", columnList = "ARRIVAL_AIRPORT_ID"),
        @Index(name = "IDX_FLIGHT_AIRCRAFT", columnList = "AIRCRAFT_ID")
})


@Entity
@ValidFlightDates
public class Flight {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "NUMBER_", nullable = false)
    @NotNull
    private String number;

    @JoinColumn(name = "AIRLINE_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Airline airline;

    @JoinColumn(name = "DEPARTURE_AIRPORT_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Airport departureAirport;

    @JoinColumn(name = "ARRIVAL_AIRPORT_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Airport arrivalAirport;

    @Column(name = "DEPARTURE_TIME", nullable = false)
    @NotNull
    private OffsetDateTime departureTime;

    @Column(name = "ARRIVAL_TIME", nullable = false)
    @NotNull
    private OffsetDateTime arrivalTime;

    @NotNull
    @JoinColumn(name = "AIRCRAFT_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Aircraft aircraft;

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public OffsetDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(OffsetDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public OffsetDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(OffsetDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @InstanceName
    @DependsOnProperties({"airline", "number"})
    public String getInstanceName(MetadataTools metadataTools) {
        return String.format("%s %s",
                metadataTools.format(airline),
                metadataTools.format(number));
    }
}