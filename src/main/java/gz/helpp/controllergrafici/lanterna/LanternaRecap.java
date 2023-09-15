package gz.helpp.controllergrafici.lanterna;

import com.googlecode.lanterna.gui2.*;
import gz.helpp.model.ModelSession;
import gz.helpp.model.ModelTransactionType;
import gz.helpp.strategypattern.InterfacciaControllerGrafico;
import gz.helpp.utils.LanternaCommonCodeUtils;

public class LanternaRecap extends BasicWindow implements InterfacciaControllerGrafico {
    private String cryptoTicker;

    private double price;

    private double quantity;

    private ModelTransactionType.Type type;

    public LanternaRecap(String cryptoTicker, double price, double quantity, ModelTransactionType.Type type){
             this.cryptoTicker = cryptoTicker;
             this.price = price;
             this.quantity = quantity;
             this.type = type;
    }

    public void initializer(){
        try {
            Panel contentPanel = new Panel();
            contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

            Label title = new Label("Recap");
            title.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            contentPanel.addComponent(title);

            Label cryptoLabel = new Label("crypto: " +cryptoTicker);
            contentPanel.addComponent(cryptoLabel);

            Label priceLabel = new Label("price: " + price);
            contentPanel.addComponent(priceLabel);

            Label quantityLabel = new Label("quantity: " + quantity);
            contentPanel.addComponent(quantityLabel);

            Label congratulationLabel;

            if(type.equals(ModelTransactionType.Type.BUY)) congratulationLabel = new Label("Congratulations! Your purchase was successful");
            else congratulationLabel = new Label("Congratulations! Your sale was successful");

            congratulationLabel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            contentPanel.addComponent(congratulationLabel);

            InitializationResult initializationResult = LanternaCommonCodeUtils.createAndInitializeWindow("Recap", contentPanel);
            initializationResult.getTextGUI().addWindowAndWait(initializationResult.getWindow());
        } catch (Exception e) {
            ModelSession.getLogger().error("LanternaRecap initializer method error", e);
        }
    }
}