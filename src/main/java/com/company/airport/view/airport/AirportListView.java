package com.company.airport.view.airport;

import com.company.airport.entity.Airport;
import com.company.airport.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "airports", layout = MainView.class)
@ViewController(id = "Airport.list")
@ViewDescriptor(path = "airport-list-view.xml")
@LookupComponent("airportsDataGrid")
@DialogMode(width = "64em")
public class AirportListView extends StandardListView<Airport> {
}