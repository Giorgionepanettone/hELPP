package gz.helpp;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import javafx.util.Pair;
import org.knowm.xchange.bitstamp.service.BitstampMarketDataService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanternaPortfolioScreen extends BasicWindow implements InterfacciaControllerGrafico, Observer {

    private List<ModelCrypto> displayedCryptoList;

    private Map<String, Pair<Double, Double>> portfolio;

    private CryptoUpdater cryptoUpdater;

    private List<Component> components;

    private Screen screen;


    public void initializer(){
        this.portfolio = ModelSession.getInstance().getModelUser().getPortfolio();
        this.cryptoUpdater = CryptoUpdater.getInstance();
        cryptoUpdater.register(this);
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        components = new ArrayList<>();
        try {
            screen = terminalFactory.createScreen();
            screen.startScreen();
            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            final BasicWindow window = new BasicWindow("Portfolio");

            Panel contentPanel = new Panel();
            contentPanel.setLayoutManager(new GridLayout(5));

            displayedCryptoList = new ArrayList<>();
            Map<String, Pair<Double, Double>> newModelSessionPortfolio = new HashMap<>();

            Label symbol = new Label("symbol");
            Label price = new Label("current price");
            Label profit = new Label("profit");
            Label quantityLabell = new Label("quantity owned");
            Label dummy = new Label("");

            contentPanel.addComponent(symbol);
            contentPanel.addComponent(price);
            contentPanel.addComponent(profit);
            contentPanel.addComponent(quantityLabell);
            contentPanel.addComponent(dummy);

            components.add(symbol);
            components.add(price);
            components.add(profit);
            components.add(quantityLabell);
            components.add(dummy);

            for (Map.Entry<String, Pair<Double, Double>> entry : this.portfolio.entrySet()) {
                double quantity = entry.getValue().getKey();
                if(quantity == 0.0){
                    continue;
                }
                String ticker = entry.getKey();
                Label symbolLabel = new Label(ticker);
                Label priceLabel = new Label("weakling");
                Label profitLabel = new Label("working on it");
                Label quantityLabel = new Label(Double.toString(quantity));

                ModelCrypto crypto = new ModelCrypto(ticker, "don't care");

                displayedCryptoList.add(crypto);
                Button sellButton = new Button("Sell", () -> {
                    LanternaAskForQuantity lanternaAskForQuantity = new LanternaAskForQuantity(ticker, crypto.getPrice(), ModelTransaction.Type.SELL);
                    lanternaAskForQuantity.bindLanternaPortfolioScreen(this);
                    lanternaAskForQuantity.initializer();
                });

                contentPanel.addComponent(symbolLabel);
                contentPanel.addComponent(priceLabel);
                contentPanel.addComponent(profitLabel);
                contentPanel.addComponent(quantityLabel);
                contentPanel.addComponent(sellButton);

                components.add(symbolLabel);
                components.add(priceLabel);
                components.add(profitLabel);
                components.add(quantityLabel);
                components.add(sellButton);

                newModelSessionPortfolio.put(ticker, entry.getValue());
            }
            this.portfolio = newModelSessionPortfolio;
            ModelSession.getInstance().getModelUser().setPortfolio(newModelSessionPortfolio);

            window.setComponent(contentPanel);

            textGUI.addWindowAndWait(window);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void update() throws IOException {
        BitstampMarketDataService bitstampMarketDataService = cryptoUpdater.getState();
        if(displayedCryptoList == null) return;

        int j = 0;
        for (int i = 0; i < components.size(); i += 5) {
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
                    int priceLabelIndex = i + 1;
                    Component priceComponent = components.get(priceLabelIndex);
                    Component profitComponent = components.get(priceLabelIndex + 1);
                    if (priceComponent instanceof Label priceLabel) {
                        priceLabel = (Label) priceComponent;
                        Label profitLabel = (Label) profitComponent;
                        ModelCrypto crypto = displayedCryptoList.get(j);
                        double price = ControllerGraficoBuyMenu.getPrice(ticker + "/EUR" ,bitstampMarketDataService);
                        Pair<Double, Double> pair = portfolio.get(ticker);
                        Double profit = price * pair.getKey() - pair.getValue();
                        crypto.setPrice(price);
                        priceLabel.setText(Double.toString(price));
                        profitLabel.setText(Double.toString(profit));
                        j++;
                    }

                }
            }
        }
    }

    public void updateRow(BeanString beanTicker, QuantityBean quantityBean){
        String ticker = beanTicker.getString();
        double quantity = quantityBean.getQuantity();

        for (int i = 0; i < components.size(); i += 5){
            Component symbolComponent = components.get(i);

            if (symbolComponent instanceof Label symbolLabel) {
                symbolLabel = (Label) symbolComponent;
                if(symbolLabel.getText().equals(ticker)){
                    int quantityLabelIndex = i + 3;
                    Component quantityComponent = components.get(quantityLabelIndex);
                    if(quantityComponent instanceof Label quantityLabel){
                        quantityLabel = (Label) quantityComponent;
                        quantityLabel.setText(Double.toString(Double.parseDouble(quantityLabel.getText()) - quantity));
                        break;
                    }
                }
            }
        }
    }

    public void closeOpenResources() throws IOException {
        screen.close();
    }
}