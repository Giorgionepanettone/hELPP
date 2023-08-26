package gz.helpp;

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


public class ControllerGraficoLogInScreen {
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Label rejectionLabel;

    @FXML
    Button registerButton;

    @FXML
    protected void registerButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RegisterScreen.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    protected void logInButtonClick() throws SQLException, IOException {
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

class BeanString{
    private String string;
    public void setString(String string){
        this.string = string;
    }
    public String getString(){
        return this.string;
    }
    public boolean checkValidity(){
        return(!this.string.equals(""));
    }
}