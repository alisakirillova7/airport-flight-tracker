package com.company.airport.view.airline;

import com.company.airport.entity.Airline;
import com.company.airport.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "airlines/:id", layout = MainView.class)
@ViewController(id = "Airline.detail")
@ViewDescriptor(path = "airline-detail-view.xml")
@EditedEntityContainer("airlineDc")
public class AirlineDetailView extends StandardDetailView<Airline> {
}