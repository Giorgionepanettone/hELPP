package gz.helpp;

import com.googlecode.lanterna.gui2.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LanternaBuyMenuScreen extends BasicWindow implements InterfacciaControllerGrafico, Observer{

    private List<ModelCrypto> displayedCryptoList;

    private CryptoUpdater cryptoUpdater;

    private CommonLanternaPricesGraphicControllerHelper commonLanternaPricesGraphicControllerHelper;
    public LanternaBuyMenuScreen() throws IOException {
        cryptoUpdater = CryptoUpdater.getInstance();
        cryptoUpdater.register(this);
        update();
    }

    public void initializer(){
        try {
            Panel contentPanel = new Panel();
            contentPanel.setLayoutManager(new GridLayout(4));

            Label headerSymbol = new Label("Symbol");
            Label headerName = new Label("Name");
            Label headerPrice = new Label("Price");
            Label dummyLabel = new Label("");

            List<Component> components = new ArrayList<>();
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

            commonLanternaPricesGraphicControllerHelper = new CommonLanternaPricesGraphicControllerHelper(displayedCryptoList, components, null);
            Label errorLabel = new Label("");
            contentPanel.addComponent(errorLabel);
            components.add(errorLabel);

            InitializationResult initializationResult = LanternaCommonCodeUtils.createAndInitializeWindow("BuyMenu screen", contentPanel);
            initializationResult.getTextGUI().addWindowAndWait(initializationResult.getWindow());
        } catch (Exception e) {
            ModelSession.getLogger().error("LanternaBuyMenuScreen initializer error", e);
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
        if(commonLanternaPricesGraphicControllerHelper != null) commonLanternaPricesGraphicControllerHelper.updateCommon(4, 2, false);
    }
}