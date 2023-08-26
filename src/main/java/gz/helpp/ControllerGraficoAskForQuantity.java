package gz.helpp;

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

public class ControllerGraficoAskForQuantity implements InterfacciaControllerGrafico{

    private String user;
    private String ticker;
    private ModelTransaction.Type type;
    @FXML
    private Label priceLabel;

    private ControllerGraficoPortfolioScreen controllerGraficoPortfolioScreen;

    @FXML
    private TextField quantityTextField;
    @FXML
    private Label errorLabel;

    @FXML
    private Button proceedButton;


    @Override
    public void initializer(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AskForQuantity.fxml"));
            fxmlLoader.setController(this);

            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            proceedButton.setOnAction(e -> {
                try {
                    proceedButtonClick();
                } catch (SQLException | IOException ex) {
                    ex.printStackTrace();
                }
            });
            stage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setError(String string){
        this.errorLabel.setText(string);
    }

    public void setType(ModelTransaction.Type type){this.type = type;}

    public void setControllerGraficoPortfolioScreen(ControllerGraficoPortfolioScreen controllerGraficoPortfolioScreen) {
        this.controllerGraficoPortfolioScreen = controllerGraficoPortfolioScreen;
    }

    public void setPriceLabel(String string){
        this.priceLabel.setText(string);
    }
    public void terminate(){

        ((Stage) priceLabel.getScene().getWindow()).close();
    }
    @FXML
    protected void proceedButtonClick() throws SQLException, IOException {
        QuantityBean quantityBean = new QuantityBean();
        quantityBean.setQuantity(quantityTextField.getText());

        if(quantityBean.getQuantity() == -1){
            errorLabel.setText("invalid input");
            return;
        }

        ModelSession modelSession = ModelSession.getInstance();

        BeanString beanUser = new BeanString();
        beanUser.setString(modelSession.getModelUser().getNickName());

        BeanString beanTicker = new BeanString();
        beanTicker.setString(ticker);

        BeanDouble beanPrice = new BeanDouble();
        if(!priceLabel.getText().equals("")) {

            beanPrice.setNumber((Double.parseDouble(priceLabel.getText().replace("€", ""))));
        }

        ControllerGraficoRecap controllerGraficoRecap = new ControllerGraficoRecap();
        controllerGraficoRecap.setType(this.type);

        if(this.type.equals(ModelTransaction.Type.BUY)){
            ControllerApplicativoBuyMenu controllerApplicativoBuyMenu = new ControllerApplicativoBuyMenu();

            controllerApplicativoBuyMenu.bind(controllerGraficoRecap);


            if(controllerApplicativoBuyMenu.proceedBuy(quantityBean, beanUser, beanTicker, beanPrice)){

            }
            else{
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

            if(controllerApplicativoPortfolioScreen.proceedSell(quantityBean, beanUser, beanTicker, beanPrice)){
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
        else if(this.type.equals(ModelTransaction.Type.DEPOSIT)){
            ControllerApplicativoDepositMenu controllerApplicativoDepositMenu = new ControllerApplicativoDepositMenu();

            QuantityBean beanQuantity = new QuantityBean();
            beanQuantity.setQuantity(quantityTextField.getText());
            controllerApplicativoDepositMenu.Deposit(beanQuantity);
        }
        else if(this.type.equals(ModelTransaction.Type.WITHDRAW)){
            ControllerApplicativoWithdrawMenu controllerApplicativoWithdrawMenu = new ControllerApplicativoWithdrawMenu();
            QuantityBean beanQuantity = new QuantityBean();
            beanQuantity.setQuantity(quantityTextField.getText());
           if(controllerApplicativoWithdrawMenu.Withdraw(quantityBean)){

           }
           else{
               this.errorLabel.setText("not enough money");
               return;
           }

        }

        //ControllerGraficoMainMenu.balanceLabel.setText(Double.toString(ModelSession.getInstance().getModelUser().getBalance()));
        terminate();
    }




    public void setUser(String user){
        this.user = user;
    }

    public void setTicker(String ticker){
        this.ticker = ticker;
    }
}