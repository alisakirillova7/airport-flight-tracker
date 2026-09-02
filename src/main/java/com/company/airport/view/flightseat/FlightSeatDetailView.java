package com.company.airport.view.flightseat;

import com.company.airport.entity.FlightSeat;
import com.company.airport.entity.Flight;
import com.company.airport.entity.Seat;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import com.vaadin.flow.router.Route;

@ViewController("FlightSeat.detail")
@ViewDescriptor("flight-seat-detail-view.xml")
@EditedEntityContainer("flightSeatDc")
@Route(value = "flightSeats/:id")
public class FlightSeatDetailView extends StandardDetailView<FlightSeat> {

    @ViewComponent
    private CollectionLoader<Seat> seatsDl;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        // Если редактируем уже существующее место рейса, сразу отфильтруем список мест
        filterSeats(getEditedEntity().getFlight());
    }

    // Слушаем изменение рейса на форме
    @Subscribe(id = "flightSeatDc", target = Target.DATA_CONTAINER)
    public void onFlightSeatDcItemPropertyChange(final InstanceContainer.ItemPropertyChangeEvent<FlightSeat> event) {
        if ("flight".equals(event.getProperty())) {
            filterSeats((Flight) event.getValue());
        }
    }

    private void filterSeats(Flight flight) {
        if (flight != null && flight.getAircraft() != null) {
            // Загружаем только те места, которые принадлежат самолёту этого рейса
            seatsDl.setQuery("select e from Seat e where e.aircraft = :aircraft");
            seatsDl.setParameter("aircraft", flight.getAircraft());
        } else {
            // Если рейс не выбран — скрываем все места
            seatsDl.setQuery("select e from Seat e where 1=0");
        }
        seatsDl.load();
    }
}