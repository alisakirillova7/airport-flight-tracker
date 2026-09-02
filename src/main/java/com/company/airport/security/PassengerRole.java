package com.company.airport.security;

import com.company.airport.entity.*;
import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.securityflowui.role.annotation.MenuPolicy;
import io.jmix.securityflowui.role.annotation.ViewPolicy;

@ResourceRole(name = "Пассажир", code = PassengerRole.CODE)
public interface PassengerRole {
    String CODE = "passenger-resource-role";

    @EntityAttributePolicy(entityClass = Flight.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Flight.class, actions = EntityPolicyAction.READ)
    void flight();

    // 1. Даем полные права на Ticket (чтобы создавать, заполнять и сохранять свой билет)
    // Разрешаем только чтение, создание и редактирование билета. Удаление (DELETE) не указываем!
    @EntityPolicy(entityClass = Ticket.class, actions = {
            EntityPolicyAction.READ,
            EntityPolicyAction.CREATE,
            EntityPolicyAction.UPDATE
    })
    @EntityAttributePolicy(entityClass = Ticket.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    void ticket();

    // 2. ОЧЕНЬ ВАЖНО: Пассажиру нужно видеть места (FlightSeat) и класс кресла (Seat), иначе форма выбора мест упадет с ошибкой доступа
    //@EntityPolicy(entityClass = FlightSeat.class, actions = EntityPolicyAction.READ)
    //@EntityAttributePolicy(entityClass = FlightSeat.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    //void flightSeat();

    @EntityPolicy(entityClass = Seat.class, actions = EntityPolicyAction.READ)
    @EntityAttributePolicy(entityClass = Seat.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    void seat();

    @EntityPolicy(entityClass = FlightPrice.class, actions = EntityPolicyAction.READ)
    @EntityAttributePolicy(entityClass = FlightPrice.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    void flightPrice();

    // 3. Разрешаем читать данные СВОЕГО профиля пассажира (чтобы система могла подставить его в билет)
    @EntityPolicy(entityClass = Passenger.class, actions = EntityPolicyAction.READ)
    @EntityAttributePolicy(entityClass = Passenger.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    void passenger();

    @MenuPolicy(menuIds = {"Ticket.list", "Flight.list"})
    @ViewPolicy(viewIds = {"Ticket.list", "Flight.list", "Ticket.detail"})
    void screens();

    // Разрешаем пассажиру читать и обновлять FlightSeat для бронирования мест
    @EntityPolicy(entityClass = FlightSeat.class, actions = {
            EntityPolicyAction.READ,
            EntityPolicyAction.UPDATE
    })
    @EntityAttributePolicy(entityClass = FlightSeat.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    void flightSeat();
}