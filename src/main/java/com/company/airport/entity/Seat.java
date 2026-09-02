package com.company.airport.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@JmixEntity
@Table(name = "SEAT", indexes = {
        @Index(name = "IDX_SEAT_AIRCRAFT", columnList = "AIRCRAFT_ID")
})
@Entity
public class Seat {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;


    @Column(name = "SEAT_NUMBER", nullable = false)
    @NotNull
    private String seatNumber;

    @Column(name = "SEAT_CLASS", nullable = false)
    @NotNull
    private String seatClass;

    @JoinColumn(name = "AIRCRAFT_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Aircraft aircraft;

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public SeatClass getSeatClass() {
        return seatClass == null ? null : SeatClass.fromId(seatClass);
    }

    public void setSeatClass(SeatClass seatClass) {
        this.seatClass = seatClass == null ? null : seatClass.getId();
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @InstanceName
    @DependsOnProperties({"seatNumber"})
    public String getInstanceName() {
        return this.seatNumber != null ? this.seatNumber : "Без номера";
    }
}