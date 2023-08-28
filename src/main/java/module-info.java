module com.example.helpp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires xchange.core;
    requires xchange.bitstamp;
    requires jsondb.core;
    requires com.googlecode.lanterna;
    requires java.desktop;
    requires org.slf4j;


    opens gz.helpp to javafx.fxml, info.picocli;
    exports gz.helpp;
}