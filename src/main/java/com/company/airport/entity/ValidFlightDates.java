package com.company.airport.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FlightDatesValidator.class)
public @interface ValidFlightDates {
    String message() default "Время прилета должно быть позже времени вылета!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
