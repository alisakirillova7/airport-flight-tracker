package com.company.airport.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@JmixEntity
@Table(name = "FLIGHT_SEAT", indexes = {
        @Index(name = "IDX_FLIGHT_SEAT_FLIGHT", columnList = "FLIGHT_ID"),
        @Index(name = "IDX_FLIGHT_SEAT_SEAT", columnList = "SEAT_ID")
})
@Entity
public class FlightSeat {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @JoinColumn(name = "FLIGHT_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Flight flight;

    @JoinColumn(name = "SEAT_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Seat seat;

    @Column(name = "IS_RESERVED")
    private Boolean isReserved = false;

    public Boolean getIsReserved() {
        return isReserved;
    }

    public void setIsReserved(Boolean isReserved) {
        this.isReserved = isReserved;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    @InstanceName
    @DependsOnProperties({"seat"}) // Говорим Jmix, что имя зависит от объекта seat
    public String getInstanceName() {
        if (getSeat() != null) {
            String number = getSeat().getSeatNumber(); // или как у тебя называется поле номера в Seat
            var seatClass = getSeat().getSeatClass();

            // Получаем понятное название из Enum (например, "Бизнес")
            String className = (seatClass != null) ? seatClass.toString() : "Класс не указан";

            return String.format("Seat %s (%s)", number, className);
        }
        return "Место не выбрано";
    }
}