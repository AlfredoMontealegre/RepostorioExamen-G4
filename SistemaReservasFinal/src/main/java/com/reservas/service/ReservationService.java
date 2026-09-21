package com.reservas.service;

import com.reservas.model.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.Optional;

public class ReservationService {
    private final ObservableList<Reservation> reservations = FXCollections.observableArrayList();
    private long nextId = 1;

    public ObservableList<Reservation> getReservations() {
        return reservations;
    }

    public Reservation add(Reservation reservation) {
        reservation.setId(nextId++);
        reservations.add(reservation);
        return reservation;
    }

    public Optional<Reservation> findById(Long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst();
    }

    public boolean update(Reservation updatedReservation) {
        Optional<Reservation> current = findById(updatedReservation.getId());
        if (current.isEmpty()) {
            return false;
        }

        Reservation reservation = current.get();
        reservation.setClient(updatedReservation.getClient());
        reservation.setDate(updatedReservation.getDate());
        reservation.setTime(updatedReservation.getTime());
        reservation.setService(updatedReservation.getService());
        reservation.setStatus(updatedReservation.getStatus());
        return true;
    }

    public boolean delete(Long id) {
        return reservations.removeIf(reservation -> reservation.getId().equals(id));
    }

    public boolean isTimeSlotTaken(LocalDate date, String time, Long ignoredId) {
        return reservations.stream().anyMatch(reservation ->
                reservation.getDate().equals(date)
                        && reservation.getTime().equals(time)
                        && (ignoredId == null || !reservation.getId().equals(ignoredId)));
    }

    public boolean isEmpty() {
        return reservations.isEmpty();
    }
}
