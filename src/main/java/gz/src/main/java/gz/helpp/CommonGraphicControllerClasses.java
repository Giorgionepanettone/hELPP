package gz.helpp;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import javafx.util.Pair;
import org.knowm.xchange.bitstamp.service.BitstampMarketDataService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

class CommonLanternaPricesGraphicControllerHelper{
    private List<Component> components;

    private List<ModelCrypto> displayedCryptoList;

    private CryptoUpdater cryptoUpdater;

    private Map<String, Pair<Double, Double>> portfolio;

    public CommonLanternaPricesGraphicControllerHelper(List<ModelCrypto> displayedCryptoList, List<Component> components, Map<String, Pair<Double, Double>> portfolio) {
        this.displayedCryptoList = displayedCryptoList;
        this.components = components;
        this.portfolio = portfolio;
        this.cryptoUpdater = CryptoUpdater.getInstance();
    }

    public void updateCommon(int increment, int offset, boolean profitCalculation) throws IOException {
        BitstampMarketDataService bitstampMarketDataService = cryptoUpdater.getState();
        if(displayedCryptoList == null) return;

        int j = 0;
        for (int i = 0; i < components.size(); i += increment) {
            Component symbolComponent = components.get(i);
            String ticker;
            try {
                ticker = displayedCryptoList.get(j).getTicker();
            }
            catch(Exception e){
                break;
            }
            if (symbolComponent instanceof Label symbolLabel) {
                symbolLabel = (Label) symbolComponent;
                if (symbolLabel.getText().equals(ticker)) {
                    int priceLabelIndex = i + offset;
                    Component priceComponent = components.get(priceLabelIndex);
                    Label priceLabel = (Label) priceComponent;
                    ModelCrypto crypto = displayedCryptoList.get(j);
                    double price = ControllerGraficoBuyMenu.getPrice(ticker + "/EUR" ,bitstampMarketDataService);
                    crypto.setPrice(price);
                    priceLabel.setText(Double.toString(price));

                    if(profitCalculation){
                        Label profitLabel = (Label) components.get(priceLabelIndex + 1);
                        Pair<Double, Double> pair = portfolio.get(ticker);
                        Double profit = price * pair.getKey() - pair.getValue();
                        profitLabel.setText(Double.toString(profit));
                    }
                    j++;
                }
            }
        }

    }
}

class HandleDepositOrWithdrawHelper{
    public boolean depositOrWithDraw(ModelTransaction.Type type, QuantityBean quantityBean) throws SQLException {

        if(type.equals(ModelTransaction.Type.DEPOSIT)){
            ControllerApplicativoDepositMenu controllerApplicativoDepositMenu = new ControllerApplicativoDepositMenu();

            controllerApplicativoDepositMenu.deposit(quantityBean);
            return true;
        }
        if(type.equals(ModelTransaction.Type.WITHDRAW)){
            ControllerApplicativoWithdrawMenu controllerApplicativoWithdrawMenu = new ControllerApplicativoWithdrawMenu();

            return controllerApplicativoWithdrawMenu.withdraw(quantityBean);
        }
        return false;
    }
}

class LanternaCommonCodeUtils{

    private LanternaCommonCodeUtils(){
        throw new IllegalStateException("utility class");
    }
    public static InitializationResult createAndInitializeWindow(String windowName, Panel contentPanel) throws IOException {
        Screen screen = null;
        try {
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
            screen = terminalFactory.createScreen();
            screen.startScreen();
            WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            BasicWindow window = new BasicWindow(windowName);

            window.setComponent(contentPanel);

            return new InitializationResult(terminalFactory, textGUI, window, contentPanel);
        }
        catch(IOException e){
            if(screen != null) screen.close();
            throw new LanternaScreenCreationException(windowName + "initialization error");
        }
    }
}

class InitializationResult {
    private final DefaultTerminalFactory terminalFactory;
    private final WindowBasedTextGUI textGUI;
    private final BasicWindow window;
    private final Panel contentPanel;

    public InitializationResult(DefaultTerminalFactory terminalFactory, WindowBasedTextGUI textGUI, BasicWindow window, Panel contentPanel){
        this.terminalFactory = terminalFactory;
        this.textGUI = textGUI;
        this.window = window;
        this.contentPanel = contentPanel;
    }

    public DefaultTerminalFactory getTerminalFactory(){
        return this.terminalFactory;
    }

    public WindowBasedTextGUI getTextGUI() {
        return textGUI;
    }

    public BasicWindow getWindow() {
        return window;
    }

    public Panel getContentPanel() {
        return contentPanel;
    }
}