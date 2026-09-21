package com.reservas.model;

public enum ServiceType {
    CONSULTA("Consulta"),
    MANTENIMIENTO("Mantenimiento"),
    INSTALACION("Instalación"),
    ASESORIA("Asesoría");

    private final String label;

    ServiceType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
