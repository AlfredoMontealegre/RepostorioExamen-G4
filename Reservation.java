package com.reservas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private Long id;
    private String client;
    private LocalDate date;
    private String time;
    private ServiceType service;
    private ReservationStatus status;
}
