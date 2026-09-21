module ni.uam.edu.proyectoexamen {
    requires javafx.controls;
    requires javafx.fxml;


    opens ni.uam.edu.proyectoexamen to javafx.fxml;
    exports ni.uam.edu.proyectoexamen;
}