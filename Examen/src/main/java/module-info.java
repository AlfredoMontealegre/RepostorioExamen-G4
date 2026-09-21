module ni.uam.edu.examen {
    requires javafx.controls;
    requires javafx.fxml;


    opens ni.uam.edu.examen to javafx.fxml;
    exports ni.uam.edu.examen;
}