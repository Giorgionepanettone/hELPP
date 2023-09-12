package gz.helpp.controllergrafici.lanterna;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import gz.helpp.bean.BeanLogIn;
import gz.helpp.controllerapplicativi.ControllerApplicativoLogIn;
import gz.helpp.model.ModelSession;
import gz.helpp.strategypattern.InterfacciaControllerGrafico;
import gz.helpp.utils.LanternaCommonCodeUtils;

import java.sql.SQLException;

public class LanternaStart{
    public static void main(String[] args){
            new MyWindow().initializer();
        }
}

class MyWindow extends BasicWindow implements InterfacciaControllerGrafico {

    @Override
    public void initializer() {
        try {
            Panel contentPanel = new Panel();
            contentPanel.setPreferredSize(new TerminalSize(150, 15));
            contentPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

            TextBox usernameTextBox = new TextBox();
            usernameTextBox.setPreferredSize(new TerminalSize(30, 1));
            contentPanel.addComponent(new Label("Username:"));
            contentPanel.addComponent(usernameTextBox);

            Label errorLabel = new Label("");

            TextBox passwordTextBox = new TextBox();
            passwordTextBox.setPreferredSize(new TerminalSize(30, 1));
            passwordTextBox.setMask('*'); // Mask password
            contentPanel.addComponent(new Label("Password:"));
            contentPanel.addComponent(passwordTextBox);


            contentPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

            Button loginButton = new Button("Login", () -> {
                BeanLogIn beanLogIn = new BeanLogIn();

                beanLogIn.setUserName(usernameTextBox.getText());
                beanLogIn.setPassword(passwordTextBox.getText());

                try {
                    if(beanLogIn.isInputEmpty()) {
                        errorLabel.setText("invalid input");
                        return;
                    }

                    ControllerApplicativoLogIn controllerApplicativoLogIn = new ControllerApplicativoLogIn();
                    controllerApplicativoLogIn.link(new LanternaMainMenu());

                    if(!controllerApplicativoLogIn.validateLogIn(beanLogIn)){
                        errorLabel.setText("user and password don't match, please try again");
                    }


                } catch (SQLException e) {
                    ModelSession.getLogger().error("sql error in LanternaStart", e);
                }
            });

            Button registerButton = new Button("Register", () -> {
                LanternaRegister lanternaRegister = new LanternaRegister();
                lanternaRegister.initializer();
            });

            Panel buttonPanel = new Panel();
            buttonPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
            buttonPanel.addComponent(loginButton);
            buttonPanel.addComponent(new EmptySpace(new TerminalSize(1, 0)));
            buttonPanel.addComponent(registerButton);

            contentPanel.addComponent(buttonPanel);
            contentPanel.addComponent(new Label(""));
            contentPanel.addComponent(errorLabel);

            InitializationResult initializationResult = LanternaCommonCodeUtils.createAndInitializeWindow("Login screen", contentPanel);
            initializationResult.getTextGUI().addWindowAndWait(initializationResult.getWindow());

        } catch (Exception e) {
            ModelSession.getLogger().error("LanternaStart initializer error", e);
        }
    }

}


