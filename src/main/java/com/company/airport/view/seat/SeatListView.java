package com.company.airport.view.seat;

import com.company.airport.entity.Seat;
import com.company.airport.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "seats", layout = MainView.class)
@ViewController(id = "Seat.list")
@ViewDescriptor(path = "seat-list-view.xml")
@LookupComponent("seatsDataGrid")
@DialogMode(width = "64em")
public class SeatListView extends StandardListView<Seat> {

}