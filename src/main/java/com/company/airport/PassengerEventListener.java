package com.company.airport;

import com.company.airport.entity.Passenger;
import com.company.airport.entity.User;
import io.jmix.core.DataManager;
import io.jmix.core.event.EntitySavingEvent;
import io.jmix.security.role.assignment.RoleAssignmentRoleType;
import io.jmix.securitydata.entity.RoleAssignmentEntity;
import io.jmix.core.security.SystemAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PassengerEventListener {

    @Autowired
    private DataManager dataManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SystemAuthenticator systemAuthenticator;

    @EventListener
    public void onPassengerSaving(EntitySavingEvent<Passenger> event) {
        if (event.isNewEntity()) {
            Passenger passenger = event.getEntity();

            if (passenger.getEmail() == null || passenger.getEmail().isBlank()) {
                throw new IllegalStateException("Невозможно создать аккаунт пользователя: у пассажира не указан Email!");
            }

            systemAuthenticator.runWithSystem(() -> {

                // 1. Создаем пользователя (User)
                User newUser = dataManager.create(User.class);
                newUser.setUsername(passenger.getEmail());
                newUser.setPassword(passwordEncoder.encode("1111"));
                newUser.setFirstName(passenger.getFirstName());
                newUser.setLastName(passenger.getLastName());
                newUser.setActive(true);

                dataManager.save(newUser);

                // 2. Назначаем Ресурсную роль Пассажира (UI, API)
                RoleAssignmentEntity resourceRole = dataManager.create(RoleAssignmentEntity.class);
                resourceRole.setUsername(newUser.getUsername());
                resourceRole.setRoleCode("passenger-resource-role");
                resourceRole.setRoleType(RoleAssignmentRoleType.RESOURCE);
                dataManager.save(resourceRole);

                // 3. Назначаем Роль уровня строк (Фильтр данных)
                RoleAssignmentEntity rowLevelRole = dataManager.create(RoleAssignmentEntity.class);
                rowLevelRole.setUsername(newUser.getUsername());
                rowLevelRole.setRoleCode("passenger-row-level-role");
                rowLevelRole.setRoleType(RoleAssignmentRoleType.ROW_LEVEL); // Важно: тут тип ROW_LEVEL
                dataManager.save(rowLevelRole);

                // 4. Назначаем Минимальный доступ к UI (ui-minimal)
                RoleAssignmentEntity minimalUiRole = dataManager.create(RoleAssignmentEntity.class);
                minimalUiRole.setUsername(newUser.getUsername());
                minimalUiRole.setRoleCode("ui-minimal");
                minimalUiRole.setRoleType(RoleAssignmentRoleType.RESOURCE);
                dataManager.save(minimalUiRole);
            });
        }
    }
}