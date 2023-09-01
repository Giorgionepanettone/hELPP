package gz.helpp.ControllerGrafici.Lanterna;

import com.googlecode.lanterna.gui2.*;
import gz.helpp.Bean.BeanString;
import gz.helpp.Bean.BeanQuantity;
import gz.helpp.Model.ModelCrypto;
import gz.helpp.Model.ModelSession;
import gz.helpp.Model.ModelTransaction;
import gz.helpp.ObserverPattern.Observer;
import gz.helpp.ObserverPatternImpl.CryptoUpdater;
import gz.helpp.StrategyPattern.InterfacciaControllerGrafico;
import gz.helpp.Utils.LanternaCommonCodeUtils;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanternaPortfolioScreen extends BasicWindow implements InterfacciaControllerGrafico, Observer {

    private List<Component> components;

    private CommonLanternaPricesGraphicControllerHelper commonLanternaPricesGraphicControllerHelper;


    public void initializer(){
        Map<String, Pair<Double, Double>> portfolio = ModelSession.getInstance().getModelUser().getPortfolio();
        CryptoUpdater cryptoUpdater = CryptoUpdater.getInstance();
        cryptoUpdater.register(this);
        components = new ArrayList<>();
        try {
            Panel contentPanel = new Panel();
            contentPanel.setLayoutManager(new GridLayout(5));

            List<ModelCrypto> displayedCryptoList = new ArrayList<>();
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

            for (Map.Entry<String, Pair<Double, Double>> entry : portfolio.entrySet()) {
                double quantity = entry.getValue().getKey();
                if(quantity == 0.0){
                    continue;
                }
                String ticker = entry.getKey();
                Label symbolLabel = new Label(ticker);
                Label priceLabel = new Label("retrieving data");
                Label profitLabel = new Label("");
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
            commonLanternaPricesGraphicControllerHelper = new CommonLanternaPricesGraphicControllerHelper(displayedCryptoList, components, portfolio);

            ModelSession.getInstance().getModelUser().setPortfolio(newModelSessionPortfolio);

            InitializationResult initializationResult = LanternaCommonCodeUtils.createAndInitializeWindow("Portfolio", contentPanel);
            initializationResult.getTextGUI().addWindowAndWait(initializationResult.getWindow());
        } catch (Exception e) {
            ModelSession.getLogger().error("LanternaPortflioScreen initializer method error", e);
        }


    }
    public void update() throws IOException {
        if(commonLanternaPricesGraphicControllerHelper != null) commonLanternaPricesGraphicControllerHelper.updateCommon(5, 1, true);
    }

    public void updateRow(BeanString beanTicker, BeanQuantity beanQuantity){
        String ticker = beanTicker.getString();
        double quantity = beanQuantity.getQuantity();

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
}