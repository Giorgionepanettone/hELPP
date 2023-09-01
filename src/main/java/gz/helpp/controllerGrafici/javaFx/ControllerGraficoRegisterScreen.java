package gz.helpp.controllerGrafici.javaFx;

import gz.helpp.bean.BeanString;
import gz.helpp.controllerApplicativi.ControllerApplicativoRegisterScreen;
import gz.helpp.model.ModelSession;
import gz.helpp.strategyPattern.InterfacciaControllerGrafico;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ControllerGraficoRegisterScreen implements InterfacciaControllerGrafico {
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    public void initializer(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RegisterScreen.fxml"));
            fxmlLoader.setController(this);

            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(Exception e){
            ModelSession.getLogger().error("ControllerGraficoRegisterScreen initializer method error",e );
        }
    }

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