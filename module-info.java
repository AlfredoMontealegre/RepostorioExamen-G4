module com.reservas {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.reservas.controller to javafx.fxml;
    opens com.reservas.model to javafx.base;

    exports com.reservas;
}
