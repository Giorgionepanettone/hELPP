package gz.helpp;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerGraficoProfileMenu implements InterfacciaControllerGrafico{
    @FXML
    Label usernameLabel;
    @FXML
    Label balanceLabel;
    @FXML
    Label emailLabel;



    public void initializer(){


        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProfileMenu.fxml"));
            fxmlLoader.setController(this);
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            ModelSession modelSession = ModelSession.getInstance();

            usernameLabel.setText( modelSession.getModelUser().getNickName());
            balanceLabel.setText("€" + Double.toString(modelSession.getModelUser().getBalance()));
            emailLabel.setText(modelSession.getModelUser().getEmail());

            stage.show();

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

}