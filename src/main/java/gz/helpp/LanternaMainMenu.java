package gz.helpp;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;

public class LanternaMainMenu extends BasicWindow implements InterfacciaControllerGrafico, BalanceObserver{
    private Label balanceLabel;

    private Screen screen;

    public void initializer(){
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        try {
            screen = terminalFactory.createScreen();
            screen.startScreen();
            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            final BasicWindow window = new BasicWindow("Main Menu");



            Panel contentPanel = new Panel();
            contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
            contentPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

            Button profileButton = new Button("Profile", () -> new LanternaProfileScreen().initializer());
            contentPanel.addComponent(profileButton);

            Button buyCryptoButton = new Button("Buy Crypto", () -> {
                try {
                    new LanternaBuyMenuScreen().initializer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            contentPanel.addComponent(buyCryptoButton);

            Button portfolioButton = new Button("Portfolio", () -> new LanternaPortfolioScreen().initializer());
            contentPanel.addComponent(portfolioButton);

            Button depositButton = new Button("Deposit", () -> new LanternaAskForQuantity("", -1, ModelTransaction.Type.DEPOSIT).initializer());
            contentPanel.addComponent(depositButton);

            Button withdrawButton = new Button("Withdraw", () -> new LanternaAskForQuantity("", -1, ModelTransaction.Type.WITHDRAW).initializer());
            contentPanel.addComponent(withdrawButton);

            balanceLabel = new Label("balance : €" + ModelSession.getInstance().getModelUser().getBalance());
            contentPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
            contentPanel.addComponent(balanceLabel);

            ModelSession.getInstance().getModelUser().addObserver(this);

            window.setComponent(contentPanel);
            textGUI.addWindowAndWait(window);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void balanceChanged(double newBalance){
        this.balanceLabel.setText("balance : €" + newBalance);
    }

    public void closeOpenResources() throws IOException {
        screen.close();
    }
}