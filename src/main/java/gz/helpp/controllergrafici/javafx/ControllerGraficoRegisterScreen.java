package gz.helpp.controllergrafici.javafx;

import gz.helpp.bean.BeanRegistration;
import gz.helpp.controllerapplicativi.ControllerApplicativoRegisterScreen;
import gz.helpp.model.ModelSession;
import gz.helpp.strategypattern.InterfacciaControllerGrafico;
import gz.helpp.utils.StandardInitializerMethodJavaFx;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

            StandardInitializerMethodJavaFx.standardInitializer(fxmlLoader);
        }
        catch(Exception e){
            ModelSession.getLogger().error("ControllerGraficoRegisterScreen initializer method error",e );
        }
    }

    @FXML
    protected void registerButtonClick() throws SQLException, InterruptedException {
        BeanRegistration beanRegistration = new BeanRegistration();

        beanRegistration.setEmail(emailTextField.getText());
        beanRegistration.setUserName(usernameTextField.getText());
        beanRegistration.setPassword(passwordField.getText());

        if(beanRegistration.isInputEmpty()){
            errorLabel.setText("Fields can't be empty");
            return;
        }

        ControllerApplicativoRegisterScreen controllerApplicativoRegisterScreen = new ControllerApplicativoRegisterScreen();
        controllerApplicativoRegisterScreen.registerUser(beanRegistration);
        errorLabel.setText("Registration was successful");
        Thread.sleep(1500);
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }
}