# Sistema de Reservas - JavaFX + Maven

Aplicación de escritorio para administrar reservas. Permite registrar cliente, fecha, hora, tipo de servicio y estado, usando `ComboBox`, `DatePicker`, validaciones y `TableView`.

## Tecnologías

- Java 21
- JavaFX 21
- Maven
- Lombok
- FXML

No se utiliza un archivo CSS externo.

## Ejecución

Con JDK 21 instalado:

```bash
mvn clean javafx:run
```

## Reparto en 8 commits

| Parte | Integrante | Commit | Entrega |
|---|---|---|---|
| 1 | Integrante 1 | `feat: crear base Maven y ventana JavaFX` | POM, módulo y ventana inicial |
| 2 | Integrante 2 | `feat: agregar modelo de reservas y enums` | Entidad `Reservation` y enums |
| 3 | Integrante 3 | `feat: diseñar interfaz con formulario y TableView` | FXML, campos, botones y tabla |
| 4 | Integrante 4 | `feat: agregar formulario y validaciones de entrada` | Validación de cliente, fecha, hora, servicio y estado |
| 5 | Integrante 1 | `feat: agregar servicio en memoria y configuración de TableView` | Servicio y enlace de columnas |
| 6 | Integrante 2 | `feat: implementar operaciones de guardar actualizar y eliminar` | CRUD y selección de registros |
| 7 | Integrante 3 | `feat: agregar búsqueda de reservas por cliente` | Filtro dinámico de búsqueda |
| 8 | Integrante 4 | `feat: cerrar funcionalidad con datos de demo y validación de horario` | Datos de demostración, control de duplicados y documentación |

Cada integrante tiene exactamente dos commits en esta versión.

## Flujo para trabajar en equipo

La rama principal contiene los ocho commits en orden. Para trabajar sobre una parte específica pueden crear una rama desde el commit correspondiente o repartirse los commits ya realizados y después integrar todo con `git merge`.

Para revisar el historial:

```bash
git log --oneline --decorate --graph
```
