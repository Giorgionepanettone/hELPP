package gz.helpp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ControllerGraficoRegisterScreen{
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    @FXML
    protected void registerButtonClick() throws SQLException, InterruptedException {
        BeanString beanEmail = new BeanString();
        beanEmail.setString(emailTextField.getText());

        BeanString beanUsername = new BeanString();
        beanUsername.setString(usernameTextField.getText());

        BeanString beanPassword = new BeanString();
        beanPassword.setString(passwordField.getText());

        if(beanEmail.checkValidity() && beanUsername.checkValidity() && beanPassword.checkValidity()){
            ControllerApplicativoRegisterScreen controllerApplicativoRegisterScreen = new ControllerApplicativoRegisterScreen();
            controllerApplicativoRegisterScreen.registerUser(beanUsername, beanEmail, beanPassword);
            errorLabel.setText("Registration was successful");
            Thread.sleep(1500);
            Stage stage = (Stage) errorLabel.getScene().getWindow();
            stage.close();
        }
        else{
            errorLabel.setText("Fields can't be empty");
        }
    }
}