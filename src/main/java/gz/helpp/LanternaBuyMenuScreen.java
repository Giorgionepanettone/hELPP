package gz.helpp;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import org.knowm.xchange.bitstamp.service.BitstampMarketDataService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LanternaBuyMenuScreen extends BasicWindow implements InterfacciaControllerGrafico, Observer{

    private Panel contentPanel;

    private List<ModelCrypto> displayedCryptoList;

    private BitstampMarketDataService bitstampMarketDataService;

    private CryptoUpdater cryptoUpdater;

    private List<Component> components;

    public LanternaBuyMenuScreen() throws IOException {
        cryptoUpdater = CryptoUpdater.getInstance();
        cryptoUpdater.register(this);
        update();
    }

    public void initializer(){
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Screen screen;
        try {
            screen = terminalFactory.createScreen();
            screen.startScreen();
            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            final BasicWindow window = new BasicWindow("Buy Crypto Menu");

            contentPanel = new Panel();
            contentPanel.setLayoutManager(new GridLayout(4));

            Label headerSymbol = new Label("Symbol");
            Label headerName = new Label("Name");
            Label headerPrice = new Label("Price");
            Label dummyLabel = new Label("");

            components = new ArrayList<>();
            contentPanel.addComponent(headerSymbol);
            contentPanel.addComponent(headerName);
            contentPanel.addComponent(headerPrice);
            contentPanel.addComponent(dummyLabel);

            components.add(headerSymbol);
            components.add(headerName);
            components.add(headerPrice);
            components.add(dummyLabel);

            displayedCryptoList = new CopyOnWriteArrayList<>();

            String[][] cryptoList = new CryptoList().getCryptoList();

            for (int i = 0; i < cryptoList.length; i++) {
                final int rowIdx = i;
                Label symbolLabel = new Label(cryptoList[i][0]);
                Label nameLabel = new Label(cryptoList[i][1]);
                Label priceLabel = new Label("retrieving data");

                ModelCrypto crypto = new ModelCrypto(cryptoList[i][0], cryptoList[i][1]);
                displayedCryptoList.add(crypto);

                Button buyButton = new Button("Buy", () -> {

                    String cryptoTicker = cryptoList[rowIdx][0];
                    double price = findOutCryptoPrice(cryptoTicker);
                    LanternaAskForQuantity lanternaAskForQuantity = new LanternaAskForQuantity(cryptoTicker, price, ModelTransaction.Type.BUY);
                    lanternaAskForQuantity.initializer();
                });

                contentPanel.addComponent(symbolLabel);
                contentPanel.addComponent(nameLabel);
                contentPanel.addComponent(priceLabel);
                contentPanel.addComponent(buyButton);

                components.add(symbolLabel);
                components.add(nameLabel);
                components.add(priceLabel);
                components.add(buyButton);
            }

            Label errorLabel = new Label("");
            contentPanel.addComponent(errorLabel);
            components.add(errorLabel);
            window.setComponent(contentPanel);

            textGUI.addWindowAndWait(window);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double findOutCryptoPrice(String ticker){
        double price = -1;
        for(ModelCrypto crypto: displayedCryptoList){
            if(crypto.getTicker().equals(ticker)){
                price = crypto.getPrice();
                break;
            }
        }
        return price;
    }

    public void update() throws IOException {
        bitstampMarketDataService = cryptoUpdater.getState();
        if(displayedCryptoList == null) return;

        int j = 0;
        for (int i = 0; i < components.size(); i += 4) {
            Component symbolComponent = components.get(i);
            String ticker;
            try {
                ticker = displayedCryptoList.get(j).getTicker();
            }
            catch(Exception e){
                break;
            }
                if (symbolComponent instanceof Label) {
                Label symbolLabel = (Label) symbolComponent;
                if (symbolLabel.getText().equals(ticker)) {
                    int priceLabelIndex = i + 2;
                    if (priceLabelIndex >= 0 && priceLabelIndex < components.size()) {
                        Component priceComponent = components.get(priceLabelIndex);
                        if (priceComponent instanceof Label) {
                            Label priceLabel = (Label) priceComponent;
                            ModelCrypto crypto = displayedCryptoList.get(j);
                            double price = ControllerGraficoBuyMenu.getPrice(ticker + "/EUR" ,bitstampMarketDataService);
                            crypto.setPrice(price);
                            priceLabel.setText(Double.toString(price));
                            j++;
                        }
                    }
                }
            }
        }

    }
}