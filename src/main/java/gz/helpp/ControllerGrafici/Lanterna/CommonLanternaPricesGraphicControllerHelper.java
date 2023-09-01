package gz.helpp.ControllerGrafici.Lanterna;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Label;
import gz.helpp.Model.ModelCrypto;
import gz.helpp.ObserverPatternImpl.CryptoUpdater;
import gz.helpp.ControllerGrafici.JavaFx.*;
import javafx.util.Pair;
import org.knowm.xchange.bitstamp.service.BitstampMarketDataService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CommonLanternaPricesGraphicControllerHelper{
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