package gz.helpp.controllergrafici.javafx;

import gz.helpp.bean.BeanLogIn;
import gz.helpp.controllerapplicativi.ControllerApplicativoLogIn;
import gz.helpp.model.ModelSession;
import gz.helpp.strategypattern.InterfacciaControllerGrafico;
import gz.helpp.utils.StandardInitializerMethodJavaFx;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

            StandardInitializerMethodJavaFx.standardInitializer(fxmlLoader);
        }
        catch(Exception e){
            ModelSession.getLogger().error("ControllerGraficoLogInScreen initializer method error",e );
        }
    }
    @FXML
    protected void registerButtonClick(){
        new ControllerGraficoRegisterScreen().initializer();
    }


    @FXML
    protected void logInButtonClick() throws SQLException {
        BeanLogIn beanLogIn = new BeanLogIn();

        beanLogIn.setUserName(usernameTextField.getText());
        beanLogIn.setPassword(passwordPasswordField.getText());

        if (beanLogIn.isInputEmpty()){
            rejectionLabel.setText("Fields can't be empty");
            return;
        }

        ControllerApplicativoLogIn controllerApplicativoLogIn = new ControllerApplicativoLogIn();

        ControllerGraficoMainMenu controllerGraficoMainMenu = new ControllerGraficoMainMenu();
        controllerApplicativoLogIn.link(controllerGraficoMainMenu);

        if(!controllerApplicativoLogIn.validateLogIn(beanLogIn)) rejectionLabel.setText("user and password don't match");

    }
}
