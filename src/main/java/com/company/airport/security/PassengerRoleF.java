package com.company.airport.security;

import com.company.airport.entity.Ticket;
import io.jmix.security.role.annotation.JpqlRowLevelPolicy;
import io.jmix.security.role.annotation.RowLevelRole;

@RowLevelRole(name = "Пассажир (фильтр)", code = PassengerRoleF.CODE)
public interface PassengerRoleF {
    String CODE = "passenger-row-level-role";

    @JpqlRowLevelPolicy(entityClass = Ticket.class, where = "{E}.passenger.email = :current_user_username")
    void ticket();
}