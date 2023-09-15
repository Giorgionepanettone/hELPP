package gz.helpp.controllergrafici.javafx;

import javafx.application.Application;

import javafx.stage.Stage;

import java.io.IOException;

public class ControllerGraficoStart extends Application{
    @Override
    public void start(Stage stage) throws IOException {
        new ControllerGraficoLogInScreen().initializer();

    }

    public static void main(String[] args) {
        launch();
    }
}