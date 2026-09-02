package com.company.airport.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FlightDatesValidator implements ConstraintValidator<ValidFlightDates, Flight> {

    @Override
    public boolean isValid(Flight flight, ConstraintValidatorContext context) {
        if (flight == null) {
            return true;
        }

        // Если какая-то из дат пустая, пропускаем (для этого есть проверка Mandatory)
        if (flight.getDepartureTime() == null || flight.getArrivalTime() == null) {
            return true;
        }

        // Ключевое правило: время прилета должно быть строго ПОСЛЕ времени вылета
        return flight.getArrivalTime().isAfter(flight.getDepartureTime());
    }
}
