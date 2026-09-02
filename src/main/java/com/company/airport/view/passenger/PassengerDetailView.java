package com.company.airport.view.passenger;

import com.company.airport.entity.Passenger;
import com.company.airport.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "passengers/:id", layout = MainView.class)
@ViewController(id = "Passenger.detail")
@ViewDescriptor(path = "passenger-detail-view.xml")
@EditedEntityContainer("passengerDc")
public class PassengerDetailView extends StandardDetailView<Passenger> {
}