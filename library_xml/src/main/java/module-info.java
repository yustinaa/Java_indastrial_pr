module org.example.library_xml {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.xml;

    opens org.example.library_xml to javafx.fxml;
    exports org.example.library_xml;
}