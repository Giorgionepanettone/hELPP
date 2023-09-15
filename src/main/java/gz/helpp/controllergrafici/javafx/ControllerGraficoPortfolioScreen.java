package gz.helpp.controllergrafici.javafx;

import gz.helpp.bean.BeanMarketData;
import gz.helpp.model.ModelSession;
import gz.helpp.model.ModelTransaction;
import gz.helpp.model.ModelTransactionType;
import gz.helpp.observerpattern.Observer;
import gz.helpp.observerpatternimpl.CryptoUpdater;
import gz.helpp.strategypattern.InterfacciaControllerGrafico;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.knowm.xchange.bitstamp.service.BitstampMarketDataService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerGraficoPortfolioScreen implements InterfacciaControllerGrafico, Observer {

    private Map<String, Pair<Double, Double>> portfolio;

    private CryptoUpdater cryptoUpdater;

    @FXML
    GridPane gridPane;

    @Override
    public void initializer(){
        ModelSession modelSession = ModelSession.getInstance();
        this.portfolio = (modelSession.getModelUser().getPortfolio());
        this.cryptoUpdater = CryptoUpdater.getInstance();
        cryptoUpdater.register(this);

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PortfolioScreen.fxml"));
            fxmlLoader.setController(this);
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(event -> CryptoUpdater.getInstance().unregister(this));
            setUp();
            stage.show();
        }
        catch(IOException e){
            ModelSession.getLogger().error("ControllerGraficoPortfolioScreen file, initializer method error", e);
        }
    }

    public void setUp() {
        Map<String, Pair<Double, Double>> newModelSessionPortfolio = new HashMap<>();

        for (Map.Entry<String, Pair<Double, Double>> entry : portfolio.entrySet()) {
            Double quantity = entry.getValue().getKey();
            String ticker = entry.getKey();
            if (quantity == 0.0) {
                continue;
            }
            addRow(ticker, "loading", "loading", quantity.toString());
            newModelSessionPortfolio.put(ticker, entry.getValue());
        }

        // Replace the original portfolio with the updated one
        ModelSession.getInstance().getModelUser().setPortfolio(newModelSessionPortfolio);
        this.portfolio = newModelSessionPortfolio;
    }


    public void updateRow(String ticker) throws IOException {
        int rowIndex = getRowIndex(ticker);
        this.portfolio = ModelSession.getInstance().getModelUser().getPortfolio();

        // Modify the row
        if (rowIndex != -1) {
            Label quantityLabel = (Label) gridPane.getChildren().get(7 + rowIndex * 5);

            quantityLabel.setText(this.portfolio.get(ticker).getKey().toString());
            update();
        }
    }

    private void addRow(String symbol, String price, String profit, String quantityOwned) {
        int rowIndex = gridPane.getRowCount();

        String font = "Arial";

        Label symbolLabel = new Label(symbol);
        symbolLabel.setFont(new Font(font, 30));


        Label currentPriceLabel = new  Label(price);
        currentPriceLabel.setFont(new Font(font, 30));


        Label profitLabel = new Label(profit);
        profitLabel.setFont(new Font(font, 30));

        Label quantityOwnedLabel = new Label(quantityOwned);
        quantityOwnedLabel.setFont(new Font(font, 30));

        Button sellButton = new Button("Sell");
        sellButton.setStyle("-fx-background-color: #FFD700;");

        sellButton.setFont(new Font(font, 30));
        sellButton.setOnAction(event -> {
            String ticker = ControllerGraficoBuyMenu.findSymbolForButton((Button) event.getSource(), 0, gridPane);
            String currentPrice = ControllerGraficoBuyMenu.findSymbolForButton((Button) event.getSource(), 1, gridPane);

            FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("AskForQuantity.fxml"));
            try {
                fxmlLoader1.load();
            } catch (IOException e) {
                ModelSession.getLogger().error("ControllerGraficoPortfolioScreen addRow method error", e);
            }
            ControllerGraficoAskForQuantity controllerGraficoAskForQuantity = new ControllerGraficoAskForQuantity(ticker, ModelTransactionType.Type.SELL, currentPrice);
            controllerGraficoAskForQuantity.initializer();

            controllerGraficoAskForQuantity.setControllerGraficoPortfolioScreen(this);
        });

        gridPane.addRow(rowIndex, symbolLabel, currentPriceLabel, profitLabel, quantityOwnedLabel, sellButton);
    }

    private int getRowIndex(String symbol){
        for (int i = 0; i < gridPane.getRowCount(); i++) {
            Node node = gridPane.getChildren().get(4 + i * 5);
            if (node instanceof Label symbolLabel ) {
                symbolLabel = (Label) node;
                if (symbolLabel.getText().equals(symbol)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public void update() throws IOException {
        Set<Map.Entry<String, Pair<Double, Double>>> entrySet = portfolio.entrySet();
        BeanMarketData beanMarketData = cryptoUpdater.getState();
        BitstampMarketDataService bitstampMarketDataService = beanMarketData.getBitstampMarketDataService();
        for (Map.Entry<String, Pair<Double, Double>> entry : entrySet){
            String ticker = entry.getKey();
            int rowIndex = getRowIndex(ticker);
            Label priceLabel = (Label) gridPane.getChildren().get(rowIndex * 5 + 5);
            Label profitLabel = (Label) gridPane.getChildren().get(rowIndex * 5 + 6);

            Double price = ControllerGraficoBuyMenu.getPrice(ticker + "/EUR", bitstampMarketDataService);
            Double profit = price * entry.getValue().getKey() - entry.getValue().getValue();
            Platform.runLater(() -> priceLabel.setText("€" + price));
            Platform.runLater(() -> profitLabel.setText("€" + profit));
        }
    }
}