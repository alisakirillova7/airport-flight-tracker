package com.company.airport.view.passenger;

import com.company.airport.entity.Passenger;
import com.company.airport.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "passengers", layout = MainView.class)
@ViewController(id = "Passenger.list")
@ViewDescriptor(path = "passenger-list-view.xml")
@LookupComponent("passengersDataGrid")
@DialogMode(width = "64em")
public class PassengerListView extends StandardListView<Passenger> {
}