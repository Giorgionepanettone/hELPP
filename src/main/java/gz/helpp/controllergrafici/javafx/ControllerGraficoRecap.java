package gz.helpp.controllergrafici.javafx;

import gz.helpp.model.ModelSession;
import gz.helpp.model.ModelTransaction;
import gz.helpp.model.ModelTransactionType;
import gz.helpp.strategypattern.InterfacciaControllerGrafico;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerGraficoRecap implements InterfacciaControllerGrafico {
    @FXML
    private Label priceLabel;

    @FXML
    private Label cryptoLabel;

    @FXML
    private Label quantityLabel;

    @FXML
    private Label congratulationsLabel;



    private ModelTransactionType.Type type;


    @Override
    public void initializer(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Recap.fxml"));
            fxmlLoader.setController(this);
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            if(type.equals(ModelTransactionType.Type.SELL)){
                this.congratulationsLabel.setText("Congratulations for your sale!");
            }
            else if(type.equals(ModelTransactionType.Type.BUY)){
                this.congratulationsLabel.setText("Congratulations for your purchase!");
            }
            else{
                this.congratulationsLabel.setText("ERROR");
            }
            stage.show();
        }
        catch(IOException e){
            ModelSession.getLogger().error("ControllergraficoRecap initializer method error", e);
        }
    }

    public void setPriceLabel(String string){
        priceLabel.setText(string);
    }

    public void setQuantityLabel(String string){
        quantityLabel.setText(string);
    }

    public void setCryptoLabel(String string){
        cryptoLabel.setText(string);
    }

    public void setCongratulationsLabel(String string){congratulationsLabel.setText(string);}

    public void setType(ModelTransactionType.Type type) {
        this.type = type;
    }

}