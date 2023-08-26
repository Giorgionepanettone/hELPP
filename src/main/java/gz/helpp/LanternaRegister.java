package gz.helpp;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.sql.SQLException;


public class LanternaRegister extends BasicWindow implements InterfacciaControllerGrafico{
    public void initializer(){
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Screen screen;
        try {
            screen = terminalFactory.createScreen();
            screen.startScreen();
            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            final BasicWindow window = new BasicWindow("Register Screen");

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
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            contentPanel.addComponent(registerButton);

            window.setComponent(contentPanel);

            textGUI.addWindowAndWait(window);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}