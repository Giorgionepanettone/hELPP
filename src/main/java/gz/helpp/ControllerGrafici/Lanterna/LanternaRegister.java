package gz.helpp.ControllerGrafici.Lanterna;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import gz.helpp.Bean.BeanString;
import gz.helpp.ControllerApplicativi.ControllerApplicativoRegisterScreen;
import gz.helpp.Model.ModelSession;
import gz.helpp.StrategyPattern.InterfacciaControllerGrafico;
import gz.helpp.Utils.LanternaCommonCodeUtils;

import java.io.IOException;
import java.sql.SQLException;


public class LanternaRegister extends BasicWindow implements InterfacciaControllerGrafico {

    private Screen screen;
    private BasicWindow window;

    public void initializer(){
        try {
            Panel contentPanel = new Panel();
            contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
            contentPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

            Label titleLabel = new Label("User Registration");
            titleLabel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            contentPanel.addComponent(titleLabel);

            contentPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

            TextBox usernameTextBox = new TextBox();
            usernameTextBox.setPreferredSize(new TerminalSize(30, 1));
            contentPanel.addComponent(new Label("Username:"));
            contentPanel.addComponent(usernameTextBox);

            TextBox passwordTextBox = new TextBox();
            passwordTextBox.setPreferredSize(new TerminalSize(30, 1));
            passwordTextBox.setMask('*');
            contentPanel.addComponent(new Label("Password:"));
            contentPanel.addComponent(passwordTextBox);

            TextBox emailTextBox = new TextBox();
            emailTextBox.setPreferredSize(new TerminalSize(30, 1));
            contentPanel.addComponent(new Label("Email:"));
            contentPanel.addComponent(emailTextBox);

            contentPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));


            Label errorLabel = new Label("");
            contentPanel.addComponent(errorLabel);
            contentPanel.addComponent(new Label(""));

            Button registerButton = new Button("Register", () -> {
                String username = usernameTextBox.getText();
                String password = passwordTextBox.getText();
                String email = emailTextBox.getText();

                BeanString beanUser = new BeanString();
                BeanString beanPass = new BeanString();
                BeanString beanEmail = new BeanString();

                beanUser.setString(username);
                beanPass.setString(password);
                beanEmail.setString(email);

                ControllerApplicativoRegisterScreen controllerApplicativoRegisterScreen = new ControllerApplicativoRegisterScreen();
                try {
                    if(beanUser.checkValidity() && beanPass.checkValidity() && beanEmail.checkValidity()){
                        controllerApplicativoRegisterScreen.registerUser(beanUser, beanEmail, beanPass);
                        window.close();
                        screen.close();
                    }
                    else{
                        errorLabel.setText("invalid input");
                    }
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            });

            contentPanel.addComponent(registerButton);

            InitializationResult initializationResult = LanternaCommonCodeUtils.createAndInitializeWindow("Login screen", contentPanel);
            WindowBasedTextGUI textGUI = initializationResult.getTextGUI();
            this.screen = textGUI.getScreen();
            this.window = initializationResult.getWindow();
            textGUI.addWindowAndWait(window);
        } catch (Exception e) {
            ModelSession.getLogger().error("LanternaRegister initializer method error", e);
        }
    }
}