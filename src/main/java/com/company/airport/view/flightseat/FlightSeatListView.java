package com.company.airport.view.flightseat;

import com.company.airport.entity.FlightSeat;
import com.company.airport.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "flight-seats", layout = MainView.class)
@ViewController(id = "FlightSeat.list")
@ViewDescriptor(path = "flight-seat-list-view.xml")
@LookupComponent("flightSeatsDataGrid")
@DialogMode(width = "64em")
public class FlightSeatListView extends StandardListView<FlightSeat> {
}