module web.arachne.corrudither {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.desktop;
    requires javafx.swing;
    requires org.apache.commons.io;
    requires org.apache.commons.imaging;
    opens web.arachne.corrudither to javafx.fxml;
    exports web.arachne.corrudither;
    exports web.arachne.gifencoder;
    opens web.arachne.gifencoder to javafx.fxml;
    exports web.arachne.corrudither.filters;
    opens web.arachne.corrudither.filters to javafx.fxml;
    exports web.arachne.corrudither.imageprocessors;
    opens web.arachne.corrudither.imageprocessors to javafx.fxml;
}