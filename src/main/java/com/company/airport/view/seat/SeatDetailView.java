package com.company.airport.view.seat;

import com.company.airport.entity.Seat;
import com.company.airport.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "seats/:id", layout = MainView.class)
@ViewController(id = "Seat.detail")
@ViewDescriptor(path = "seat-detail-view.xml")
@EditedEntityContainer("seatDc")
public class SeatDetailView extends StandardDetailView<Seat> {
}