package com.company.airport.view.aircraft;

import com.company.airport.entity.Aircraft;
import com.company.airport.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "aircrafts/:id", layout = MainView.class)
@ViewController(id = "Aircraft.detail")
@ViewDescriptor(path = "aircraft-detail-view.xml")
@EditedEntityContainer("aircraftDc")
public class AircraftDetailView extends StandardDetailView<Aircraft> {
}