package gz.helpp;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;

public class LanternaRecap extends BasicWindow implements InterfacciaControllerGrafico{
    private String cryptoTicker;

    private double price;

    private double quantity;

    private ModelTransaction.Type type;

    private Screen screen;

    public LanternaRecap(String cryptoTicker, double price, double quantity, ModelTransaction.Type type){
             this.cryptoTicker = cryptoTicker;
             this.price = price;
             this.quantity = quantity;
             this.type = type;
    }

    public void initializer(){
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        try {
            screen = terminalFactory.createScreen();
            screen.startScreen();
            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            final BasicWindow window = new BasicWindow("Recap");

            Panel contentPanel = new Panel();
            contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

            Label title = new Label("Recap");
            title.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            contentPanel.addComponent(title);

            Label cryptoLabel = new Label(cryptoTicker);
            contentPanel.addComponent(cryptoLabel);

            Label priceLabel = new Label(Double.toString(price));
            contentPanel.addComponent(priceLabel);

            Label quantityLabel = new Label(Double.toString(quantity));
            contentPanel.addComponent(quantityLabel);

            Label congratulationLabel;

            if(type.equals(ModelTransaction.Type.BUY)) congratulationLabel = new Label("Congratulations! Your purchase was successful");
            else congratulationLabel = new Label("Congratulations! Your sale was successful");

            congratulationLabel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            contentPanel.addComponent(congratulationLabel);

            window.setComponent(contentPanel);

            textGUI.addWindowAndWait(window);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeOpenResources() throws IOException {
        screen.close();
    }
}