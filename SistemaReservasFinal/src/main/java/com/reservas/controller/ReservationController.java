package com.reservas.controller;

import com.reservas.model.Reservation;
import com.reservas.model.ReservationStatus;
import com.reservas.model.ServiceType;
import com.reservas.service.ReservationService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ReservationController {

    @FXML private TextField clientField;
    @FXML private DatePicker datePicker;
    @FXML private TextField timeField;
    @FXML private ComboBox<ServiceType> serviceComboBox;
    @FXML private ComboBox<ReservationStatus> statusComboBox;
    @FXML private TextField searchField;
    @FXML private Label messageLabel;

    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, Long> idColumn;
    @FXML private TableColumn<Reservation, String> clientColumn;
    @FXML private TableColumn<Reservation, LocalDate> dateColumn;
    @FXML private TableColumn<Reservation, String> timeColumn;
    @FXML private TableColumn<Reservation, ServiceType> serviceColumn;
    @FXML private TableColumn<Reservation, ReservationStatus> statusColumn;

    @FXML private Button saveButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final ReservationService reservationService = new ReservationService();
    private FilteredList<Reservation> filteredReservations;
    private Long selectedReservationId;

    @FXML
    private void initialize() {
        serviceComboBox.getItems().setAll(ServiceType.values());
        statusComboBox.getItems().setAll(ReservationStatus.values());
        statusComboBox.setValue(ReservationStatus.PENDIENTE);
        datePicker.setValue(LocalDate.now());

        configureTable();

        filteredReservations = new FilteredList<>(reservationService.getReservations(), reservation -> true);
        reservationTable.setItems(filteredReservations);

        reservationTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldValue, newValue) -> loadSelectedReservation(newValue)
        );

        searchField.textProperty().addListener(
                (obs, oldText, newText) -> filterReservations(newText)
        );

        loadSampleData();
        messageLabel.setText("Listo para registrar reservas.");
    }

    private void configureTable() {
        idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        clientColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getClient()));
        dateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDate()));
        timeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTime()));
        serviceColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getService()));
        statusColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getStatus()));
    }

    private Reservation reservationFromForm(Long id) {
        return new Reservation(
                id,
                clientField.getText().trim(),
                datePicker.getValue(),
                timeField.getText().trim(),
                serviceComboBox.getValue(),
                statusComboBox.getValue()
        );
    }

    private boolean validateForm() {
        String client = clientField.getText() == null ? "" : clientField.getText().trim();

        if (client.length() < 3) {
            showMessage("El cliente debe tener al menos 3 caracteres.");
            clientField.requestFocus();
            return false;
        }

        if (datePicker.getValue() == null) {
            showMessage("Seleccione una fecha.");
            datePicker.requestFocus();
            return false;
        }

        if (datePicker.getValue().isBefore(LocalDate.now())) {
            showMessage("La fecha no puede ser anterior al día actual.");
            datePicker.requestFocus();
            return false;
        }

        String time = timeField.getText() == null ? "" : timeField.getText().trim();
        try {
            LocalTime.parse(time, TIME_FORMATTER);
        } catch (DateTimeParseException ex) {
            showMessage("La hora debe tener el formato HH:mm, por ejemplo 08:30.");
            timeField.requestFocus();
            return false;
        }

        if (serviceComboBox.getValue() == null) {
            showMessage("Seleccione un tipo de servicio.");
            serviceComboBox.requestFocus();
            return false;
        }

        if (statusComboBox.getValue() == null) {
            showMessage("Seleccione un estado.");
            statusComboBox.requestFocus();
            return false;
        }

        if (reservationService.isTimeSlotTaken(datePicker.getValue(), time, selectedReservationId)) {
            showMessage("Ya existe una reserva para esa fecha y hora.");
            timeField.requestFocus();
            return false;
        }

        return true;
    }

    @FXML
    private void handleSave() {
        if (!validateForm()) {
            return;
        }

        reservationService.add(reservationFromForm(null));
        reservationTable.refresh();
        clearForm();
        showMessage("Reserva guardada correctamente.");
    }

    @FXML
    private void handleUpdate() {
        if (selectedReservationId == null) {
            showMessage("Seleccione una reserva de la tabla para actualizar.");
            return;
        }

        if (!validateForm()) {
            return;
        }

        boolean updated = reservationService.update(reservationFromForm(selectedReservationId));
        if (updated) {
            reservationTable.refresh();
            showMessage("Reserva actualizada correctamente.");
        } else {
            showMessage("No fue posible actualizar la reserva.");
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedReservationId == null) {
            showMessage("Seleccione una reserva de la tabla para eliminar.");
            return;
        }

        Reservation selected = reservationService.findById(selectedReservationId).orElse(null);
        if (selected == null) {
            showMessage("La reserva seleccionada ya no existe.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar reserva");
        alert.setHeaderText("¿Desea eliminar esta reserva?");
        alert.setContentText("Cliente: " + selected.getClient() + " | Fecha: " + selected.getDate());

        if (alert.showAndWait().orElse(null) == javafx.scene.control.ButtonType.OK) {
            reservationService.delete(selectedReservationId);
            reservationTable.getSelectionModel().clearSelection();
            clearForm();
            showMessage("Reserva eliminada correctamente.");
        }
    }

    @FXML
    private void handleClear() {
        clearForm();
        reservationTable.getSelectionModel().clearSelection();
        showMessage("Formulario limpiado.");
    }

    @FXML
    private void handleClearSearch() {
        searchField.clear();
        showMessage("Búsqueda limpiada.");
    }

    private void loadSelectedReservation(Reservation reservation) {
        if (reservation == null) {
            selectedReservationId = null;
            return;
        }

        selectedReservationId = reservation.getId();
        clientField.setText(reservation.getClient());
        datePicker.setValue(reservation.getDate());
        timeField.setText(reservation.getTime());
        serviceComboBox.setValue(reservation.getService());
        statusComboBox.setValue(reservation.getStatus());
        showMessage("Reserva #" + reservation.getId() + " seleccionada.");
    }

    private void filterReservations(String text) {
        String filter = text == null ? "" : text.trim().toLowerCase();
        filteredReservations.setPredicate(reservation ->
                filter.isBlank() || reservation.getClient().toLowerCase().contains(filter)
        );
    }

    private void clearForm() {
        selectedReservationId = null;
        clientField.clear();
        datePicker.setValue(LocalDate.now());
        timeField.clear();
        serviceComboBox.getSelectionModel().clearSelection();
        statusComboBox.setValue(ReservationStatus.PENDIENTE);
    }

    private void loadSampleData() {
        if (!reservationService.isEmpty()) {
            return;
        }

        reservationService.add(new Reservation(
                null,
                "María López",
                LocalDate.now().plusDays(1),
                "09:00",
                ServiceType.CONSULTA,
                ReservationStatus.CONFIRMADA
        ));

        reservationService.add(new Reservation(
                null,
                "Carlos Pérez",
                LocalDate.now().plusDays(2),
                "14:30",
                ServiceType.MANTENIMIENTO,
                ReservationStatus.PENDIENTE
        ));
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
    }
}
