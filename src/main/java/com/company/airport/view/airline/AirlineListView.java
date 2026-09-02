package com.company.airport.view.airline;

import com.company.airport.entity.Airline;
import com.company.airport.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "airlines", layout = MainView.class)
@ViewController(id = "Airline.list")
@ViewDescriptor(path = "airline-list-view.xml")
@LookupComponent("airlinesDataGrid")
@DialogMode(width = "64em")
public class AirlineListView extends StandardListView<Airline> {
}