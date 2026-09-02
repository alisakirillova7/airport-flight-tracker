package com.company.airport.view.flight;

import com.company.airport.entity.Flight;
import com.company.airport.entity.FlightSeat;
import com.company.airport.entity.Seat;
import io.jmix.core.DataManager;
import io.jmix.flowui.Notifications;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.ClickEvent;
import io.jmix.flowui.view.*;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@ViewController("Flight.detail")
@ViewDescriptor("flight-detail-view.xml")
@EditedEntityContainer("flightDc")
@Route(value = "flights/:id")
public class FlightDetailView extends StandardDetailView<Flight> {

    @Autowired
    private DataManager dataManager;

    @Autowired
    private Notifications notifications;

    @Subscribe("generateSeatsBtn")
    public void onGenerateSeatsBtnClick(final ClickEvent<Button> event) {
        Flight flight = getEditedEntity();

        if (flight.getId() == null) {
            notifications.create("Attention", "First save the new flight (click OK), then open it to generate seats!")
                    .withType(Notifications.Type.WARNING)
                    .show();
            return;
        }

        if (flight.getAircraft() == null) {
            notifications.create("Error", "First, select an aircraft for the flight!")
                    .withType(Notifications.Type.WARNING)
                    .show();
            return;
        }

        List<Seat> targetSeats = dataManager.load(Seat.class)
                .query("select s from Seat s where s.aircraft = :aircraft")
                .parameter("aircraft", flight.getAircraft())
                .list();

        if (targetSeats.isEmpty()) {
            notifications.create("Attention", "Seat entries have not yet been created in the Seats database for the selected aircraft!")
                    .withType(Notifications.Type.WARNING)
                    .show();
            return;
        }

        boolean alreadyGenerated = dataManager.load(FlightSeat.class)
                .query("select fs from FlightSeat fs where fs.flight = :flight")
                .parameter("flight", flight)
                .optional()
                .isPresent();

        if (alreadyGenerated) {
            notifications.create("Warning", "Seats for this flight have already been generated!")
                    .withType(Notifications.Type.WARNING)
                    .show();
            return;
        }

        int createdCount = 0;

        for (Seat seat : targetSeats) {
            FlightSeat flightSeat = dataManager.create(FlightSeat.class);
            flightSeat.setFlight(flight);
            flightSeat.setSeat(seat);
            flightSeat.setIsReserved(false);

            dataManager.save(flightSeat);
            createdCount++;
        }

        notifications.create("Success", "Successfully generated seats for flight: " + createdCount)
                .withType(Notifications.Type.SUCCESS)
                .show();
    }
}