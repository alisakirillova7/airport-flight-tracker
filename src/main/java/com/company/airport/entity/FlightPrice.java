package com.company.airport.entity;

import io.jmix.core.MetadataTools;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.datatype.DatatypeFormatter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@JmixEntity
@Table(name = "FLIGHT_PRICE", indexes = {
        @Index(name = "IDX_FLIGHT_PRICE_FLIGHT", columnList = "FLIGHT_ID")
})
@Entity
public class FlightPrice {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @JoinColumn(name = "FLIGHT_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Flight flight;

    @Column(name = "SEAT_CLASS", nullable = false)
    @NotNull
    private String seatClass;

    @Column(name = "PRICE", nullable = false, precision = 19, scale = 2)
    @NotNull
    private BigDecimal price;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public SeatClass getSeatClass() {
        return seatClass == null ? null : SeatClass.fromId(seatClass);
    }

    public void setSeatClass(SeatClass seatClass) {
        this.seatClass = seatClass == null ? null : seatClass.getId();
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
    @DependsOnProperties({"flight", "price", "seatClass"})
    public String getInstanceName(MetadataTools metadataTools, DatatypeFormatter datatypeFormatter) {
        return String.format("%s %s %s",
                metadataTools.format(flight),
                datatypeFormatter.formatBigDecimal(price),
                metadataTools.format(getSeatClass()));
    }
}