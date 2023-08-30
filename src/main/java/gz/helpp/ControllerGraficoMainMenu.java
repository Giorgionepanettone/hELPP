package gz.helpp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class ControllerGraficoMainMenu implements InterfacciaControllerGrafico, BalanceObserver{

    private String nickname;

    @FXML
    private ImageView imageLogo;

    @FXML
    private Label balanceLabel;

    @Override
    public void initializer(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));

        ModelSession modelSession = ModelSession.getInstance();
        this.nickname = modelSession.getModelUser().getUserName();



        try{
            fxmlLoader.setController(this);
            Parent root = fxmlLoader.load();

            balanceLabel.setText("Balance: €" + modelSession.getModelUser().getBalance());
            imageLogo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo_transparent.png"))));
            ModelSession.getInstance().getModelUser().addObserver(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(event -> ModelSession.getInstance().getModelUser().removeObserver(this));
            stage.show();
        }
        catch(IOException e){
            ModelSession.getLogger().error("ControllerGraficoMainMenu initializer method error", e);
        }
    }

    @Override
    public void balanceChanged(double newBalance) {
        balanceLabel.setText("Balance: €" + newBalance);
    }

    public void setNickname(String nickname){

        this.nickname = nickname;
    }

    @FXML
    protected void profileButtonClick(){
        ControllerApplicativoMainMenu controllerApplicativoMainMenu = new ControllerApplicativoMainMenu();
        ControllerGraficoProfileMenu controllerGraficoProfileMenu = new ControllerGraficoProfileMenu();
        controllerApplicativoMainMenu.bind(controllerGraficoProfileMenu);
        controllerApplicativoMainMenu.portfolioOrProfileOrWithdrawOrDeposit();
    }

    @FXML
    protected void depositButtonClick(){
        ControllerApplicativoMainMenu controllerApplicativoMainMenu = new ControllerApplicativoMainMenu();
        ControllerGraficoAskForQuantity controllerGraficoAskForQuantity = new ControllerGraficoAskForQuantity("", ModelTransaction.Type.DEPOSIT, "");

        controllerApplicativoMainMenu.bind(controllerGraficoAskForQuantity);
        controllerApplicativoMainMenu.portfolioOrProfileOrWithdrawOrDeposit();
    }

    @FXML
    protected void withdrawButtonClick(){
        ControllerApplicativoMainMenu controllerApplicativoMainMenu = new ControllerApplicativoMainMenu();
        ControllerGraficoAskForQuantity controllerGraficoAskForQuantity = new ControllerGraficoAskForQuantity("", ModelTransaction.Type.WITHDRAW, "");
        controllerApplicativoMainMenu.bind(controllerGraficoAskForQuantity);
        controllerApplicativoMainMenu.portfolioOrProfileOrWithdrawOrDeposit();
    }


    @FXML
    protected void buyButtonClick() throws IOException {
        ControllerApplicativoMainMenu controllerApplicativoMainMenu = new ControllerApplicativoMainMenu();
        ControllerGraficoBuyMenu controllerGraficoBuyMenu = new ControllerGraficoBuyMenu();
        controllerApplicativoMainMenu.bind(controllerGraficoBuyMenu);

        controllerApplicativoMainMenu.buy();
    }



    @FXML
    protected void portfolioButtonClick(){
        ControllerApplicativoMainMenu controllerApplicativoMainMenu = new ControllerApplicativoMainMenu();
        ControllerGraficoPortfolioScreen controllerGraficoPortfolioScreen = new ControllerGraficoPortfolioScreen();
        controllerApplicativoMainMenu.bind(controllerGraficoPortfolioScreen);
        controllerApplicativoMainMenu.portfolioOrProfileOrWithdrawOrDeposit();
    }

}
