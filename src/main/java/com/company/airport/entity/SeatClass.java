package com.company.airport.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum SeatClass implements EnumClass<String> {

    ECONOMY("A"),
    BUSINESS("B"),
    FIRST("C");

    private final String id;

    SeatClass(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static SeatClass fromId(String id) {
        for (SeatClass at : SeatClass.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}