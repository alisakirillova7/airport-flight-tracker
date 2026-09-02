package com.company.airport.view.flightprice;

import com.company.airport.entity.FlightPrice;
import com.company.airport.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "flight-prices", layout = MainView.class)
@ViewController(id = "FlightPrice.list")
@ViewDescriptor(path = "flight-price-list-view.xml")
@LookupComponent("flightPricesDataGrid")
@DialogMode(width = "64em")
public class FlightPriceListView extends StandardListView<FlightPrice> {
}