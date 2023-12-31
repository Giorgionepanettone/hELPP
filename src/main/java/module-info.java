module gz.helpp{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires xchange.core;
    requires xchange.bitstamp;
    requires jsondb.core;
    requires com.googlecode.lanterna;
    requires java.desktop;
    requires slf4j.api;

    opens gz.helpp.controllergrafici.javafx to javafx.fxml;
    exports gz.helpp.controllergrafici.javafx to javafx.graphics;
    exports gz.helpp.model;
}