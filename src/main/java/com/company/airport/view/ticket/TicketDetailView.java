package com.company.airport.view.ticket;

import com.company.airport.entity.Ticket;
import com.company.airport.entity.Flight;
import com.company.airport.entity.FlightSeat;
// ВНИМАНИЕ: Если ты уже создала сущность FlightPrice, раскомментируй строку ниже:
// import com.company.airport.entity.FlightPrice;
import com.vaadin.flow.component.AbstractField;
import io.jmix.core.DataManager;
import io.jmix.flowui.component.valuepicker.EntityPicker;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.InstanceLoader;
import io.jmix.flowui.view.*;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import com.company.airport.entity.FlightPrice;


@ViewController("Ticket.detail")
@ViewDescriptor("ticket-detail-view.xml")
@EditedEntityContainer("ticketDc")
@Route(value = "tickets/:id")
public class TicketDetailView extends StandardDetailView<Ticket> {

    @ViewComponent
    private CollectionLoader<FlightSeat> flightSeatsDl;

    @Autowired
    private io.jmix.flowui.Notifications notifications;

    @ViewComponent
    private InstanceContainer<Ticket> ticketDc;

    @Autowired
    private io.jmix.core.UnconstrainedDataManager dataManager; // Заменяем обычный DataManager на этот

    @ViewComponent
    private com.vaadin.flow.component.button.Button payBtn;


    @Subscribe(id = "ticketDl", target = Target.DATA_LOADER)
    public void onTicketDlPostLoad(final InstanceLoader.PostLoadEvent<Ticket> event) {
        Ticket ticket = event.getLoadedEntity();
        refreshSeatsList(ticket.getFlight(), ticket.getFlightSeat());
    }
    @Subscribe(id = "ticketDc", target = Target.DATA_CONTAINER)
    public void onTicketDcItemPropertyChange(final InstanceContainer.ItemPropertyChangeEvent<Ticket> event) {
        Ticket ticket = ticketDc.getItemOrNull();
        if (ticket == null) return;

        // Ситуация А: Кассир изменил рейс
        if ("flight".equals(event.getProperty())) {
            Flight newFlight = (Flight) event.getValue();

            // Безопасно проверяем рейсы через Objects.equals
            FlightSeat currentSeat = (ticket.getFlight() != null && newFlight != null && java.util.Objects.equals(ticket.getFlight(), newFlight))
                    ? ticket.getFlightSeat()
                    : null;

            refreshSeatsList(newFlight, currentSeat);
        }

        // Ситуация Б: Кассир выбрал конкретное место на рейсе
        if ("flightSeat".equals(event.getProperty())) {
            FlightSeat selectedSeat = (FlightSeat) event.getValue();

            if (selectedSeat != null && ticket.getFlight() != null && selectedSeat.getSeat() != null) {
                var seatClass = selectedSeat.getSeat().getSeatClass();

                // Ищем цену в справочнике
                FlightPrice flightPrice = dataManager.load(FlightPrice.class)
                        .query("select e from FlightPrice e where e.flight = :flight and e.seatClass = :seatClass")
                        .parameter("flight", ticket.getFlight())
                        .parameter("seatClass", seatClass)
                        .optional()
                        .orElse(null);

                if (flightPrice != null) {
                    ticket.setPrice(flightPrice.getPrice());
                } else {
                    ticket.setPrice(null);
                }
            } else {
                ticket.setPrice(null);
            }
        }
    }

    private void refreshSeatsList(Flight flight, FlightSeat currentSeat) {
        if (flight == null) {
            // Если рейс не выбран, список мест пустой
            flightSeatsDl.setQuery("select e from FlightSeat e where e.id is null");
        } else {
            // Запрос: выбираем места текущего рейса, которые СВОБОДНЫ (isReserved = false)
            // ИЛИ это то место, которое уже сохранено в текущем билете (чтобы не пропадало при редактировании)
            flightSeatsDl.setQuery("select e from FlightSeat e where e.flight = :flight " +
                    "and (e.isReserved = false or e = :currentSeat)");
            flightSeatsDl.setParameter("flight", flight);
            flightSeatsDl.setParameter("currentSeat", currentSeat);
        }
        flightSeatsDl.load(); // Принудительно обновляем выпадающий список в интерфейсе
    }

    @Subscribe("flightField")
    public void onFlightFieldComponentValueChange(
            final com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent<?, Flight> event) {

        Flight selectedFlight = event.getValue();
        Ticket ticket = getEditedEntity();

        refreshSeatsList(selectedFlight, ticket.getFlightSeat());
    }


    @Autowired
    private io.jmix.security.role.assignment.RoleAssignmentRepository roleAssignmentRepository;

    // 1. ОБЯЗАТЕЛЬНО ДОБАВЬ ЭТУ СТРОКУ (её не хватало, поэтому passengerField горел красным)
    @ViewComponent
    private io.jmix.flowui.component.valuepicker.EntityPicker<com.company.airport.entity.Passenger> passengerField;


    @Autowired
    private io.jmix.core.security.CurrentAuthentication currentAuthentication;

    @ViewComponent
    private com.vaadin.flow.component.checkbox.Checkbox paidField; // Внедряем галочку оплаты

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        if (currentAuthentication.getUser() == null) return;

        String username = currentAuthentication.getUser().getUsername();

        // 1. Получаем все роли пользователя
        java.util.Collection<? extends io.jmix.security.role.assignment.RoleAssignment> assignments =
                roleAssignmentRepository.getAssignmentsByUsername(username);

        boolean isClerk = assignments.stream().anyMatch(a -> a.getRoleCode().equals("ticket-clerk"));
        boolean isAdmin = assignments.stream().anyMatch(a -> a.getRoleCode().equals("system-full-access") || a.getRoleCode().equals("admin"));

        // 2. Настраиваем видимость и доступность в зависимости от роли
        if (isAdmin) {
            // АДМИНИСТРАТОР: доступно вообще всё
            if (passengerField != null) passengerField.setReadOnly(false);
            if (paidField != null) paidField.setReadOnly(false);
            if (payBtn != null) payBtn.setVisible(true);
        } else if (isClerk) {
            // КАССИР: может менять пассажира и ставить галочку, кнопка оплаты скрыта
            if (passengerField != null) passengerField.setReadOnly(false);
            if (paidField != null) paidField.setReadOnly(false);
            if (payBtn != null) payBtn.setVisible(false);
        } else {
            // ПАССАЖИР: всё заблокировано, кроме кнопки оплаты
            if (passengerField != null) passengerField.setReadOnly(true);
            if (paidField != null) paidField.setReadOnly(true);
            if (payBtn != null) payBtn.setVisible(true);
        }
    }

    @Subscribe
    public void onInitEntity(final InitEntityEvent<Ticket> event) {
        Ticket ticket = event.getEntity();

        // 1. Получаем логин текущего пользователя
        String currentUserEmail = currentAuthentication.getUser().getUsername();

        // Генерируем случайный номер билета, если он пустой
        if (ticket.getTicketNumber() == null) {
            ticket.setTicketNumber("TKT-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        // 2. Ищем сущность пассажира, явно указав полный пакет com.company.airport.entity.Passenger
        com.company.airport.entity.Passenger currentPassenger = dataManager.load(com.company.airport.entity.Passenger.class)
                .query("select e from Passenger e where e.email = :email")
                .parameter("email", currentUserEmail)
                .optional()
                .orElse(null);

        if (currentPassenger != null) {
            // 3. Теперь типы данных идеально совпадают, и метод сработает без ошибок!
            ticket.setPassenger(currentPassenger);
        }

        // Твой стандартный метод обновления мест (если он есть)
        if (ticket.getFlight() != null) {
            refreshSeatsList(ticket.getFlight(), ticket.getFlightSeat());
        }
    }

    @Subscribe("payBtn")
    public void onPayBtnClick(final com.vaadin.flow.component.ClickEvent<com.vaadin.flow.component.button.Button> event) {
        Ticket ticket = getEditedEntity();

        if (Boolean.TRUE.equals(ticket.getPaid())) {
            notifications.create("Attention", "This ticket has already been paid for!")
                    .withType(io.jmix.flowui.Notifications.Type.WARNING)
                    .show();
            return;
        }

        com.vaadin.flow.component.dialog.Dialog paymentDialog = new com.vaadin.flow.component.dialog.Dialog();
        paymentDialog.setHeaderTitle("Payment by bank card");
        paymentDialog.setModal(true);
        paymentDialog.setCloseOnEsc(true);
        paymentDialog.setCloseOnOutsideClick(false);

        com.vaadin.flow.component.textfield.TextField cardNumberField = new com.vaadin.flow.component.textfield.TextField("Номер карты (16 цифр)");
        cardNumberField.setPlaceholder("0000 0000 0000 0000");
        cardNumberField.setMaxLength(16);
        cardNumberField.setWidthFull();

        com.vaadin.flow.component.textfield.PasswordField cvvField =
                new com.vaadin.flow.component.textfield.PasswordField("CVV");
        cvvField.setPlaceholder("123");
        cvvField.setMaxLength(3);
        cvvField.setWidth("80px");

        com.vaadin.flow.component.orderedlayout.VerticalLayout dialogLayout =
                new com.vaadin.flow.component.orderedlayout.VerticalLayout(cardNumberField, cvvField);
        paymentDialog.add(dialogLayout);

        // --- ВОТ ЭТА КНОПКА ПОДТВЕРЖДЕНИЯ ---
        com.vaadin.flow.component.button.Button confirmBtn = new com.vaadin.flow.component.button.Button("Подтвердить", e -> {
            if (cardNumberField.getValue().trim().length() < 16 || cvvField.getValue().trim().isEmpty()) {
                notifications.create("Error", "Please enter valid card details!")
                        .withType(io.jmix.flowui.Notifications.Type.WARNING)
                        .show();
                return;
            }

            // ИЗМЕНЕНИЯ НАЧИНАЮТСЯ ЗДЕСЬ:
            ticket.setPaid(true);
            paymentDialog.close();

            // ВОТ ТОТ САМЫЙ КУСОЧЕК КОДА:
            if (ticket.getFlightSeat() != null) {
                FlightSeat seat = ticket.getFlightSeat();
                seat.setIsReserved(true);

                // Сохраняем место через unconstrained менеджер в обход роли пассажира
                dataManager.save(seat);
            }

            notifications.create("Payment successful", "Ticket successfully paid for!")
                    .withType(io.jmix.flowui.Notifications.Type.SUCCESS)
                    .show();

            closeWithSave(); // Сохраняем сам билет
        });
        confirmBtn.addThemeVariants(com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY);

        com.vaadin.flow.component.button.Button cancelBtn = new com.vaadin.flow.component.button.Button("Отмена", e -> paymentDialog.close());
        paymentDialog.getFooter().add(confirmBtn, cancelBtn);

        paymentDialog.open();
    }

    @Subscribe("paidField")
    public void onPaidFieldComponentValueChange(
            final com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent<?, Boolean> event) {

        // Получаем текущее состояние галочки (true или false)
        Boolean isPaid = event.getValue();
        Ticket ticket = getEditedEntity();

        // Если галочку СТАВЯТ (билет оплачивают) и у билета выбрано место
        if (Boolean.TRUE.equals(isPaid) && ticket.getFlightSeat() != null) {
            FlightSeat seat = ticket.getFlightSeat();

            // Если место ещё не зарезервировано, резервируем его в БД
            if (!Boolean.TRUE.equals(seat.getIsReserved())) {
                seat.setIsReserved(true);
                dataManager.save(seat); // Наш unconstrained dataManager сохранит без проблем с правами

                notifications.create("Success!", "Ticket marked as paid, seat reserved!")
                        .withType(io.jmix.flowui.Notifications.Type.SUCCESS)
                        .show();
            }
        }
        // Если галочку СНИМАЮТ (например, отмена оплаты), возвращаем место в доступные
        else if (Boolean.FALSE.equals(isPaid) && ticket.getFlightSeat() != null) {
            FlightSeat seat = ticket.getFlightSeat();
            if (Boolean.TRUE.equals(seat.getIsReserved())) {
                seat.setIsReserved(false);
                dataManager.save(seat);

                notifications.create("Attention", "Оплата отменена, место снова свободно.")
                        .withType(io.jmix.flowui.Notifications.Type.WARNING)
                        .show();
            }
        }
    }

    @Subscribe("flightSeatField") // Убедись, что id в XML совпадает (например, flightSeatField)
    public void onFlightSeatFieldFocus(final com.vaadin.flow.component.FocusNotifier.FocusEvent<?> event) {
        Ticket ticket = getEditedEntity();
        Flight selectedFlight = ticket.getFlight();

        if (selectedFlight == null) {
            notifications.create("Attention", "Select a flight first!")
                    .withType(io.jmix.flowui.Notifications.Type.WARNING)
                    .show();
            return;
        }

        // Загружаем свободные места
        java.util.List<FlightSeat> freeSeats = dataManager.load(FlightSeat.class)
                .query("select e from FlightSeat e where e.flight = :flight and e.isReserved = false")
                .parameter("flight", selectedFlight)
                .list();

        // Если свободных мест нет — выводим желтое окошко ошибки
        if (freeSeats.isEmpty()) {
            notifications.create("Attention", "There are no seats left on the selected flight!")
                    .withType(io.jmix.flowui.Notifications.Type.WARNING)
                    .withPosition(com.vaadin.flow.component.notification.Notification.Position.MIDDLE)
                    .show();
        }
    }
}