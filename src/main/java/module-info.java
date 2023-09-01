module gz.helpp{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires xchange.core;
    requires xchange.bitstamp;
    requires jsondb.core;
    requires com.googlecode.lanterna;
    requires java.desktop;
    requires org.slf4j;


    opens gz.helpp.controllerGrafici.JavaFx to javafx.fxml;
    exports gz.helpp.controllerGrafici.JavaFx to javafx.graphics;
}