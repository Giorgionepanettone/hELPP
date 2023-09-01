package gz.helpp.controllerGrafici.javaFx;

import gz.helpp.bean.BeanString;
import gz.helpp.controllerApplicativi.ControllerApplicativoLogIn;
import gz.helpp.model.ModelSession;
import gz.helpp.strategyPattern.InterfacciaControllerGrafico;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class ControllerGraficoLogInScreen implements InterfacciaControllerGrafico {
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Label rejectionLabel;

    @FXML
    Button registerButton;

    public void initializer(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LogInScreen.fxml"));
            fxmlLoader.setController(this);

            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(Exception e){
            ModelSession.getLogger().error("ControllerGraficoLogInScreen initializer method error",e );
        }
    }
    @FXML
    protected void registerButtonClick() throws IOException {
        new ControllerGraficoRegisterScreen().initializer();
    }


    @FXML
    protected void logInButtonClick() throws SQLException {
        BeanString beanUsername = new BeanString();
        BeanString beanPassword = new BeanString();

        beanUsername.setString(usernameTextField.getText());
        beanPassword.setString(passwordPasswordField.getText());

        if (beanUsername.checkValidity() && beanPassword.checkValidity()) {
            ControllerApplicativoLogIn controllerApplicativoLogIn = new ControllerApplicativoLogIn();

            ControllerGraficoMainMenu controllerGraficoMainMenu = new ControllerGraficoMainMenu();
            controllerApplicativoLogIn.link(controllerGraficoMainMenu);

            if(!controllerApplicativoLogIn.validateLogIn(beanUsername, beanPassword)) rejectionLabel.setText("user and password don't match");
        }
        else {
            rejectionLabel.setText("Fields can't be empty");
        }
    }
}
