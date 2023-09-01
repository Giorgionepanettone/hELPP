package gz.helpp.controllerGrafici.lanterna;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import gz.helpp.model.ModelSession;
import gz.helpp.model.ModelTransaction;
import gz.helpp.observerPattern.BalanceObserver;
import gz.helpp.strategyPattern.InterfacciaControllerGrafico;
import gz.helpp.utils.LanternaCommonCodeUtils;

import java.io.IOException;

public class LanternaMainMenu extends BasicWindow implements InterfacciaControllerGrafico, BalanceObserver {
    private Label balanceLabel;

    public void initializer(){
        try {
            Panel contentPanel = new Panel();
            contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
            contentPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

            Button profileButton = new Button("Profile", () -> new LanternaProfileScreen().initializer());
            contentPanel.addComponent(profileButton);

            Button buyCryptoButton = new Button("Buy Crypto", () -> {
                try {
                    new LanternaBuyMenuScreen().initializer();
                } catch (IOException e) {
                    ModelSession.getLogger().error("LanternaMainMenu initializer method button initialization error", e);
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

            InitializationResult initializationResult = LanternaCommonCodeUtils.createAndInitializeWindow("Login screen", contentPanel);
            initializationResult.getTextGUI().addWindowAndWait(initializationResult.getWindow());
        } catch (Exception e) {
            ModelSession.getLogger().error("LanternaMainMenu initializer method error", e);
        }
    }
    public void balanceChanged(double newBalance){

        this.balanceLabel.setText("balance : €" + newBalance);
    }

}