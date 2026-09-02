package com.company.airport.view.flightprice;

import com.company.airport.entity.FlightPrice;
import com.company.airport.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "flight-prices/:id", layout = MainView.class)
@ViewController(id = "FlightPrice.detail")
@ViewDescriptor(path = "flight-price-detail-view.xml")
@EditedEntityContainer("flightPriceDc")
public class FlightPriceDetailView extends StandardDetailView<FlightPrice> {
}