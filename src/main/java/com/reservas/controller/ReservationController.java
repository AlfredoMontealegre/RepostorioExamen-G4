package com.reservas.controller;

import com.reservas.model.Reservation;
import com.reservas.model.ReservationStatus;
import com.reservas.model.ServiceType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;

import com.reservas.service.ReservationService;

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

    @FXML private TableView<?> reservationTable;
    @FXML private TableColumn<?, ?> idColumn;
    @FXML private TableColumn<?, ?> clientColumn;
    @FXML private TableColumn<?, ?> dateColumn;
    @FXML private TableColumn<?, ?> timeColumn;
    @FXML private TableColumn<?, ?> serviceColumn;
    @FXML private TableColumn<?, ?> statusColumn;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private final ReservationService reservationService = new ReservationService();

    @FXML
    private void initialize() {
        serviceComboBox.getItems().setAll(ServiceType.values());
        statusComboBox.getItems().setAll(ReservationStatus.values());
        statusComboBox.setValue(ReservationStatus.PENDIENTE);
        datePicker.setValue(LocalDate.now());
        configureTable();
        reservationTable.setItems(reservationService.getReservations());
        messageLabel.setText("");
    }

    @FXML
    public void handleSave(ActionEvent event) {
        messageLabel.setText("Validación pendiente de completar en la siguiente parte.");
    }

    @FXML
    public void handleUpdate(ActionEvent event) {
        messageLabel.setText("Seleccione una reserva para actualizar.");
    }

    @FXML
    public void handleDelete(ActionEvent event) {
        messageLabel.setText("Seleccione una reserva para eliminar.");
    }

    @FXML
    public void handleClear(ActionEvent event) {
        clearForm();
        messageLabel.setText("Formulario limpiado.");
    }

    @FXML
    public void handleClearSearch(ActionEvent event) {
        searchField.clear();
        messageLabel.setText("");
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
            messageLabel.setText("El cliente debe tener al menos 3 caracteres.");
            clientField.requestFocus();
            return false;
        }

        if (datePicker.getValue() == null) {
            messageLabel.setText("Seleccione una fecha.");
            datePicker.requestFocus();
            return false;
        }

        if (datePicker.getValue().isBefore(LocalDate.now())) {
            messageLabel.setText("La fecha no puede ser anterior al día actual.");
            datePicker.requestFocus();
            return false;
        }

        String time = timeField.getText() == null ? "" : timeField.getText().trim();
        try {
            LocalTime.parse(time, TIME_FORMATTER);
        } catch (DateTimeParseException ex) {
            messageLabel.setText("La hora debe tener el formato HH:mm, por ejemplo 08:30.");
            timeField.requestFocus();
            return false;
        }

        if (serviceComboBox.getValue() == null) {
            messageLabel.setText("Seleccione un tipo de servicio.");
            serviceComboBox.requestFocus();
            return false;
        }

        if (statusComboBox.getValue() == null) {
            messageLabel.setText("Seleccione un estado.");
            statusComboBox.requestFocus();
            return false;
        }

        return true;
    }

    private void clearForm() {
        clientField.clear();
        datePicker.setValue(LocalDate.now());
        timeField.clear();
        serviceComboBox.getSelectionModel().clearSelection();
        statusComboBox.setValue(ReservationStatus.PENDIENTE);
    }
}
