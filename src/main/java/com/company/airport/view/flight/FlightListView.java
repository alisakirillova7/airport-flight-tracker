package com.company.airport.view.flight;

import com.company.airport.entity.Flight;
import com.company.airport.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "flights", layout = MainView.class)
@ViewController(id = "Flight.list")
@ViewDescriptor(path = "flight-list-view.xml")
@LookupComponent("flightsDataGrid")
@DialogMode(width = "64em")
public class FlightListView extends StandardListView<Flight> {
}