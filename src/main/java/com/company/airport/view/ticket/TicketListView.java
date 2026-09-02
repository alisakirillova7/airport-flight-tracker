package com.company.airport.view.ticket;

import com.company.airport.entity.Ticket;
import com.company.airport.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "tickets", layout = MainView.class)
@ViewController(id = "Ticket.list")
@ViewDescriptor(path = "ticket-list-view.xml")
@LookupComponent("ticketsDataGrid")
@DialogMode(width = "64em")
public class TicketListView extends StandardListView<Ticket> {
}