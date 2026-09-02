package com.company.airport.security;

import com.company.airport.entity.*;
import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.securityflowui.role.annotation.MenuPolicy;
import io.jmix.securityflowui.role.annotation.ViewPolicy;

@ResourceRole(name = "Кассир", code = Role.CODE)
public interface Role {
    String CODE = "ticket-clerk";

    @EntityAttributePolicy(entityClass = Passenger.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Passenger.class, actions = EntityPolicyAction.ALL)
    void passenger();

    @EntityAttributePolicy(entityClass = Ticket.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Ticket.class, actions = EntityPolicyAction.ALL)
    void ticket();

    @EntityAttributePolicy(entityClass = Flight.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Flight.class, actions = EntityPolicyAction.READ)
    void flight();

    @EntityAttributePolicy(entityClass = Airport.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Airport.class, actions = EntityPolicyAction.READ)
    void airport();

    @EntityAttributePolicy(entityClass = Airline.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Airline.class, actions = EntityPolicyAction.READ)
    void airline();

    @EntityAttributePolicy(entityClass = Aircraft.class, attributes = {"model", "registrationNumber", "capacity", "id", "*"}, action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Aircraft.class, actions = EntityPolicyAction.READ)
    void aircraft();

    @MenuPolicy(menuIds = {"Passenger.list", "Ticket.list", "Flight.list"})
    @ViewPolicy(viewIds = {"Passenger.list", "Ticket.list", "Flight.list", "Ticket.detail", "Passenger.detail"})
    void screens();

    // Убедись, что у кассира есть READ (или ALL) доступ к FlightSeat
    @EntityPolicy(entityClass = FlightSeat.class, actions = {EntityPolicyAction.READ, EntityPolicyAction.UPDATE})
    @EntityAttributePolicy(entityClass = FlightSeat.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    void flightSeat();

    @EntityPolicy(entityClass = Seat.class, actions = EntityPolicyAction.READ)
    @EntityAttributePolicy(entityClass = Seat.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    void seat();
}