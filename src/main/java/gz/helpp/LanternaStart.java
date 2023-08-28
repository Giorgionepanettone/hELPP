package gz.helpp;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.sql.SQLException;

public class LanternaStart{
    public static void main(String[] args){
            new MyWindow().initializer();
        }
}

class MyWindow extends BasicWindow implements InterfacciaControllerGrafico{
    private Screen screen;

    @Override
    public void initializer() {
        try {
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
            screen = terminalFactory.createScreen();
            screen.startScreen();
            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            final BasicWindow window = new BasicWindow("Login page");

            Panel contentPanel = new Panel();
            contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
            contentPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            contentPanel.setPreferredSize(new TerminalSize(150, 15));

            contentPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

            TextBox usernameTextBox = new TextBox();
            usernameTextBox.setPreferredSize(new TerminalSize(30, 1)); // Adjust size
            contentPanel.addComponent(new Label("Username:"));
            contentPanel.addComponent(usernameTextBox);

            Label errorLabel = new Label("");

            TextBox passwordTextBox = new TextBox();
            passwordTextBox.setPreferredSize(new TerminalSize(30, 1)); // Adjust size
            passwordTextBox.setMask('*'); // Mask the password
            contentPanel.addComponent(new Label("Password:"));
            contentPanel.addComponent(passwordTextBox);

            contentPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

            Button loginButton = new Button("Login", () -> {
                BeanString beanUser = new BeanString();
                BeanString beanPass = new BeanString();

                beanUser.setString(usernameTextBox.getText());
                beanPass.setString(passwordTextBox.getText());

                try {
                    if(beanUser.checkValidity() && beanPass.checkValidity()){
                        ControllerApplicativoLogIn controllerApplicativoLogIn = new ControllerApplicativoLogIn();
                        controllerApplicativoLogIn.link(new LanternaMainMenu());
                        if(!controllerApplicativoLogIn.validateLogIn(beanUser, beanPass)){
                            errorLabel.setText("user and password don't match, please try again");
                        }
                    }
                    else{
                        errorLabel.setText("invalid input");
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

            window.setComponent(contentPanel);

            textGUI.addWindowAndWait(window);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeOpenResources() throws IOException {
        screen.close();
    }
}


