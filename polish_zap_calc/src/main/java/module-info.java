module com.example.polish_zap_calc {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.polish_zap_calc to javafx.fxml;
    exports com.example.polish_zap_calc;
}