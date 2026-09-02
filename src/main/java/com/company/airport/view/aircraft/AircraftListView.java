package com.company.airport.view.aircraft;

import com.company.airport.entity.Aircraft;
import com.company.airport.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "aircrafts", layout = MainView.class)
@ViewController(id = "Aircraft.list")
@ViewDescriptor(path = "aircraft-list-view.xml")
@LookupComponent("aircraftsDataGrid")
@DialogMode(width = "64em")
public class AircraftListView extends StandardListView<Aircraft> {
}