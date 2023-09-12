package gz.helpp.controllergrafici.lanterna;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import gz.helpp.bean.BeanRegistration;
import gz.helpp.controllerapplicativi.ControllerApplicativoRegisterScreen;
import gz.helpp.model.ModelSession;
import gz.helpp.strategypattern.InterfacciaControllerGrafico;
import gz.helpp.utils.LanternaCommonCodeUtils;

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

                BeanRegistration beanRegistration = new BeanRegistration();

                beanRegistration.setUserName(username);
                beanRegistration.setPassword(password);
                beanRegistration.setEmail(email);

                ControllerApplicativoRegisterScreen controllerApplicativoRegisterScreen = new ControllerApplicativoRegisterScreen();
                try {

                    if(beanRegistration.isInputEmpty()){
                        errorLabel.setText("invalid input");
                    }

                    controllerApplicativoRegisterScreen.registerUser(beanRegistration);
                    window.close();
                    screen.close();

                } catch (SQLException | IOException e) {
                    ModelSession.getLogger().error("LanternaRegister initializer method error", e);
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