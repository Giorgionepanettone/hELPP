package gz.helpp.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StandardInitializerMethodJavaFx{

    private StandardInitializerMethodJavaFx(){
        throw new IllegalStateException("util class");
    }

    public static void standardInitializer(FXMLLoader fxmlLoader) throws IOException {
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}