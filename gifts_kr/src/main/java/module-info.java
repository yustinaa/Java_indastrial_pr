module com.example.gifts_kr {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.gifts_kr to javafx.fxml;
    exports com.example.gifts_kr;
}