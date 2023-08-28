package gz.helpp;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;

public class LanternaProfileScreen extends BasicWindow implements InterfacciaControllerGrafico{

        private Screen screen;

        public void initializer(){
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        try {
            screen = terminalFactory.createScreen();
            screen.startScreen();
            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            final BasicWindow window = new BasicWindow("Profile");

            Panel contentPanel = new Panel();
            contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
            contentPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));


            Label usernameLabel = new Label("Username: "); // Update with actual username
            contentPanel.addComponent(usernameLabel);

            Label balanceLabel = new Label("Balance: "); // Update with actual balance
            contentPanel.addComponent(balanceLabel);

            Label emailLabel = new Label("Email: porcoddio@example.com"); // Update with actual email
            contentPanel.addComponent(emailLabel);

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