package gz.helpp;

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
import org.knowm.xchange.bitstamp.service.BitstampMarketDataService;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerGraficoBuyMenu implements Observer, InterfacciaControllerGrafico{

    @FXML
    private GridPane gridPane;

    private CryptoUpdater cryptoUpdater;

    private List<String> displayedCrypto;

    public ControllerGraficoBuyMenu() throws IOException{
        cryptoUpdater = CryptoUpdater.getInstance();
        cryptoUpdater.register(this);
        update();
    }

    @Override
    public void initializer(){


        try{
            FXMLLoader fxmlLoader;
            fxmlLoader = new FXMLLoader(getClass().getResource("BuyMenu.fxml"));
            fxmlLoader.setController(this);
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root,870, 700));
            stage.setOnCloseRequest(event -> CryptoUpdater.getInstance().unregister(this));

            stage.show();
            initializeCrypto();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    static double getPrice(String symbol, BitstampMarketDataService bitstampMarketDataService) throws IOException {
        if(bitstampMarketDataService != null) {
            Ticker ticker = bitstampMarketDataService.getTicker(new CurrencyPair(symbol));
            if (ticker != null) {
                return ticker.getLast().doubleValue();
            }
        }
        return -1;
    }

    public void update() throws IOException {
        BitstampMarketDataService bitstampMarketDataService = cryptoUpdater.getState();
        if(displayedCrypto == null) return;

        for(int i = 0; i < displayedCrypto.size(); i++){
            String ticker = displayedCrypto.get(i);
            modifyRow(ticker ,null, null, getPrice(ticker + "/EUR" ,bitstampMarketDataService));
        }
    }

    public void initializeCrypto() {
        String string = "initializing";
        addRow("BTC", "Bitcoin", string);
        addRow("ETH", "Ethereum", string);
        addRow("LTC", "Litecoin", string);
        addRow("XRP", "Ripple", string);
        addRow("BCH", "Bitcoin Cash", string);
        addRow("USDT", "Tether", string);
        addRow("ADA", "Cardano", string);
        addRow("MATIC", "Polygon", string);
        addRow("DOGE", "Dogecoin", string);
        addRow("SOL", "Solana", string);

        displayedCrypto = new ArrayList<>();

        displayedCrypto.add("BTC");
        displayedCrypto.add("ETH");
        displayedCrypto.add("LTC");
        displayedCrypto.add("XRP");
        displayedCrypto.add("BCH");
        displayedCrypto.add("USDT");
        displayedCrypto.add("ADA");
        displayedCrypto.add("MATIC");
        displayedCrypto.add("DOGE");
        displayedCrypto.add("SOL");
    }

    static String findSymbolForButton(Button buyButton, int i, GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Label symbolLabel) {
                Integer rowIndex = GridPane.getRowIndex(symbolLabel);
                Integer buyButtonRowIndex = GridPane.getRowIndex(buyButton);
                if (rowIndex != null && rowIndex.equals(buyButtonRowIndex) && GridPane.getColumnIndex(node) == i) {
                    return symbolLabel.getText();
                }
            }
        }
        return null;
    }



    private void addRow(String symbol, String name, String price) {
        int rowIndex = gridPane.getRowCount();

        String font = "Arial";

        Label symbolLabel = new Label(symbol);
        symbolLabel.setFont(new Font(font, 30));

        Label nameLabel = new Label(name);
        nameLabel.setFont(new Font(font, 30));

        Label priceLabel = new  Label(price);
        priceLabel.setFont(new Font(font, 30));

        Button buyButton = new Button("Buy");
        buyButton.setOnAction(event -> {
            String ticker = findSymbolForButton((Button) event.getSource(), 0, gridPane);
            String price1 = findSymbolForButton((Button) event.getSource(), 2, gridPane);

            ControllerGraficoAskForQuantity controllerGraficoAskForQuantity = new ControllerGraficoAskForQuantity(ticker, ModelTransaction.Type.BUY, price1);
            controllerGraficoAskForQuantity.initializer();

        });
        buyButton.setFont(new Font(font, 30));
        buyButton.setStyle("-fx-background-color: #FFD700;");
        buyButton.setPrefSize(120, 1);

        gridPane.addRow(rowIndex, symbolLabel, nameLabel, priceLabel, buyButton);
    }

    private void modifyRow(String symbol, String newSymbol, String newName, double newPrice) {
        // Get the row index of the row to modify
        int rowIndex = getRowIndex(symbol);

        // Modify the row
        if (rowIndex != -1) {
            Label symbolLabel = (Label) gridPane.getChildren().get(rowIndex * 4);
            Label nameLabel = (Label) gridPane.getChildren().get(rowIndex * 4 + 1);
            Label priceLabel = (Label) gridPane.getChildren().get(rowIndex * 4 + 2);

            if (newSymbol != null){
                Platform.runLater(() -> symbolLabel.setText(newSymbol));
            }
            if (newName != null){
                Platform.runLater(() -> nameLabel.setText(newName));
            }

            String priceString = Double.toString(newPrice);
            if(priceString.equals("-1.0")) {
                Platform.runLater(() -> priceLabel.setText("retrieving data..."));
            }
            else{
                Platform.runLater(() -> priceLabel.setText("€" + priceString));
            }

            if(newSymbol != null) {
                displayedCrypto.remove(symbol);
                displayedCrypto.add(newSymbol);
            }
            }
    }

    private int getRowIndex(String symbol){
        int rowIndex = -1;
        for (int i = 0; i < gridPane.getRowCount(); i++) {
            Node node = gridPane.getChildren().get(i * 4);
            if (node instanceof Label symbolLabel ) {
                symbolLabel = (Label) node;
                if (symbolLabel.getText().equals(symbol)) {
                    rowIndex = i;
                    break;
                }
            }
        }
        return rowIndex;
    }
}

class QuantityBean{
    private String quantity;

    public void setQuantity(String quantity){
        this.quantity = quantity;
    }

    public double getQuantity(){
        double number;
        try{
            number = Double.parseDouble(quantity);
        } catch(NumberFormatException e){
            return -1;
        }
        if(number <= 0) {
            return -1;
        }
        return number;
    }

}


