package gz.helpp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerGraficoRecap implements InterfacciaControllerGrafico{
    @FXML
    private Label priceLabel;

    @FXML
    private Label cryptoLabel;

    @FXML
    private Label quantityLabel;

    @FXML
    private Label congratulationsLabel;



    private ModelTransaction.Type type;


    @Override
    public void initializer(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Recap.fxml"));
            fxmlLoader.setController(this);
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            if(type.equals(ModelTransaction.Type.SELL)){
                this.congratulationsLabel.setText("Congratulations for your sale!");
            }
            else if(type.equals(ModelTransaction.Type.BUY)){
                this.congratulationsLabel.setText("Congratulations for your purchase!");
            }
            else{
                this.congratulationsLabel.setText("ERROR");
            }
            stage.show();
        }
        catch(IOException e){
            e.printStackTrace();
            throw new RuntimeException();
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

    public void setType(ModelTransaction.Type type) {
        this.type = type;
    }

}