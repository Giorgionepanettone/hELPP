package gz.helpp.controllergrafici.javafx;

import gz.helpp.bean.BeanTransaction;
import gz.helpp.controllerapplicativi.ControllerApplicativoBuyMenu;
import gz.helpp.controllerapplicativi.ControllerApplicativoPortfolioScreen;
import gz.helpp.model.ModelSession;
import gz.helpp.model.ModelTransaction;
import gz.helpp.strategypattern.InterfacciaControllerGrafico;
import gz.helpp.utils.HandleDepositOrWithdrawHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ControllerGraficoAskForQuantity implements InterfacciaControllerGrafico {
    private String ticker;
    private ModelTransaction.Type type;
    private String price;
    @FXML
    private Label priceLabel;

    @FXML
    private Label priceLabelTitle;

    @FXML
    private Label tickerLabel;

    @FXML
    private Label cryptoLabelTitle;

    private ControllerGraficoPortfolioScreen controllerGraficoPortfolioScreen;

    @FXML
    private TextField quantityTextField;
    @FXML
    private Label errorLabel;

    @FXML
    private Button proceedButton;

    public ControllerGraficoAskForQuantity(String ticker, ModelTransaction.Type type, String price) {
        this.ticker = ticker;
        this.type = type;
        this.price = price;
    }

    @Override
    public void initializer(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AskForQuantity.fxml"));
            fxmlLoader.setController(this);

            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            priceLabel.setText(price);
            tickerLabel.setText(ticker);
            if(this.type.equals(ModelTransaction.Type.DEPOSIT) || this.type.equals(ModelTransaction.Type.WITHDRAW)) {
                this.cryptoLabelTitle.setText("");
                this.priceLabelTitle.setText("");
            }

            proceedButton.setOnAction(e -> {
                try {
                    proceedButtonClick();
                } catch (SQLException | IOException ex) {
                    ModelSession.getLogger().error("ControllerGraficoAskForQuantity button initialize error, in initializer method", ex);
                }
            });
            stage.show();
        }
        catch(IOException e){
            ModelSession.getLogger().error("ControllerGraficoAskForQuantity initializer error", e);
        }
    }

    public void setError(String error){
        this.errorLabel.setText(error);
    }
    public void setControllerGraficoPortfolioScreen(ControllerGraficoPortfolioScreen controllerGraficoPortfolioScreen) {
        this.controllerGraficoPortfolioScreen = controllerGraficoPortfolioScreen;
    }

    public void terminate(){
        ((Stage) priceLabel.getScene().getWindow()).close();
    }

    @FXML
    protected void proceedButtonClick() throws SQLException, IOException {
        BeanTransaction beanTransaction = new BeanTransaction();
        beanTransaction.setQuantity(quantityTextField.getText());

        if(!beanTransaction.isQuantityValid()){
            errorLabel.setText("invalid input");
            return;
        }

        beanTransaction.setTicker(ticker);


        if(!price.equals("")) beanTransaction.setPrice(Double.parseDouble(price.replace("€", "")));

        ControllerGraficoRecap controllerGraficoRecap = new ControllerGraficoRecap();
        controllerGraficoRecap.setType(this.type);

        if(this.type.equals(ModelTransaction.Type.BUY)){
            ControllerApplicativoBuyMenu controllerApplicativoBuyMenu = new ControllerApplicativoBuyMenu();

            controllerApplicativoBuyMenu.bind(controllerGraficoRecap);


            if(!controllerApplicativoBuyMenu.proceedBuy(beanTransaction)){
                errorLabel.setText("insufficient balance");
                return;
            }
            controllerGraficoRecap.setQuantityLabel(quantityTextField.getText());
            controllerGraficoRecap.setCryptoLabel(ticker);
            controllerGraficoRecap.setPriceLabel(priceLabel.getText());
        }
        else if(this.type.equals(ModelTransaction.Type.SELL)){
            ControllerApplicativoPortfolioScreen controllerApplicativoPortfolioScreen = new ControllerApplicativoPortfolioScreen();
            controllerApplicativoPortfolioScreen.bind(controllerGraficoRecap);

            if(controllerApplicativoPortfolioScreen.proceedSell(beanTransaction)){
                controllerGraficoPortfolioScreen.updateRow(ticker);
            }
            else{
                this.errorLabel.setText("Not enough coins owned");
                return;
            }
            controllerGraficoRecap.setQuantityLabel(quantityTextField.getText());
            controllerGraficoRecap.setCryptoLabel(ticker);
            controllerGraficoRecap.setPriceLabel(priceLabel.getText());
        }
        else if(! HandleDepositOrWithdrawHelper.depositOrWithDraw(type, beanTransaction)){
            errorLabel.setText("not enough money on balance");
            return;
        }

        terminate();
    }
}