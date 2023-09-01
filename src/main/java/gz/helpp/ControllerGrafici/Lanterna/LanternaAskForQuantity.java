package gz.helpp.ControllerGrafici.Lanterna;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import gz.helpp.Bean.BeanDouble;
import gz.helpp.Bean.BeanString;
import gz.helpp.Bean.BeanQuantity;
import gz.helpp.ControllerApplicativi.ControllerApplicativoBuyMenu;
import gz.helpp.ControllerApplicativi.ControllerApplicativoPortfolioScreen;
import gz.helpp.Model.ModelSession;
import gz.helpp.Model.ModelTransaction;
import gz.helpp.StrategyPattern.InterfacciaControllerGrafico;
import gz.helpp.Utils.HandleDepositOrWithdrawHelper;
import gz.helpp.Utils.LanternaCommonCodeUtils;

import java.io.IOException;
import java.sql.SQLException;


public class LanternaAskForQuantity extends BasicWindow implements InterfacciaControllerGrafico {
    private String ticker;

    private double price;

    private ModelTransaction.Type type;

    private TextBox quantityTextField;

    private Label errorLabel;

    private LanternaPortfolioScreen lanternaPortfolioScreen;

    private BasicWindow window;

    private Screen screen;

    public LanternaAskForQuantity(String ticker, double price, ModelTransaction.Type type){
        this.ticker = ticker;
        this.price = price;
        this.type = type;
    }

    public void initializer(){
        try {
            Panel contentPanel = new Panel();
            contentPanel.setLayoutManager(new GridLayout(2));

            Label priceLabel = new Label("");
            if(type.equals(ModelTransaction.Type.BUY) || type.equals(ModelTransaction.Type.SELL)) priceLabel.setText(Double.toString(price));

            contentPanel.addComponent(priceLabel);

            quantityTextField = new TextBox().setPreferredSize(new TerminalSize(20, 1));
            contentPanel.addComponent(quantityTextField);

            Button proceedButton = new Button("Proceed", () -> {
                try {
                    proceedButtonFunc();
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            });
            contentPanel.addComponent(proceedButton);

            errorLabel = new Label("");
            contentPanel.addComponent(errorLabel);

            InitializationResult initializationResult = LanternaCommonCodeUtils.createAndInitializeWindow("Insert quantity", contentPanel);
            WindowBasedTextGUI textGUI = initializationResult.getTextGUI();
            this.screen = textGUI.getScreen();
            this.window = initializationResult.getWindow();
            textGUI.addWindowAndWait(window);

        } catch (Exception e) {
            ModelSession.getLogger().error("LanternaAskForQuantity initializer error", e);
        }
    }

    private void proceedButtonFunc() throws SQLException, IOException {
        BeanQuantity beanQuantity = new BeanQuantity();
        String quantityString = quantityTextField.getText();
        beanQuantity.setQuantity(quantityString);

        if(beanQuantity.getQuantity() == -1){
            errorLabel.setText("invalid input");
            return;
        }

        ModelSession modelSession = ModelSession.getInstance();

        BeanString beanUser = new BeanString();
        beanUser.setString(modelSession.getModelUser().getUserName());

        BeanString beanTicker = new BeanString();
        beanTicker.setString(ticker);

        BeanDouble beanPrice = new BeanDouble();
        beanPrice.setNumber(price);

        LanternaRecap lanternaRecap = new LanternaRecap(ticker, price, Double.parseDouble(quantityString), this.type);


        if(this.type.equals(ModelTransaction.Type.BUY)){
            ControllerApplicativoBuyMenu controllerApplicativoBuyMenu = new ControllerApplicativoBuyMenu();
            controllerApplicativoBuyMenu.bind(lanternaRecap);

            if(!controllerApplicativoBuyMenu.proceedBuy(beanQuantity, beanUser, beanTicker, beanPrice)){
                errorLabel.setText("insufficient balance");
                return;
            }

        }
        else if(this.type.equals(ModelTransaction.Type.SELL)){
            ControllerApplicativoPortfolioScreen controllerApplicativoPortfolioScreen = new ControllerApplicativoPortfolioScreen();
            controllerApplicativoPortfolioScreen.bind(lanternaRecap);

            if(controllerApplicativoPortfolioScreen.proceedSell(beanQuantity, beanUser, beanTicker, beanPrice)){
                lanternaPortfolioScreen.updateRow(beanTicker, beanQuantity);
            }
            else{
                this.errorLabel.setText("Not enough coins owned");
                return;
            }
        }
        else if(!new HandleDepositOrWithdrawHelper().depositOrWithDraw(type, beanQuantity)){
                errorLabel.setText("not enough money on balance");
                return;
        }

        window.close();
        screen.close();
    }

    public void bindLanternaPortfolioScreen(LanternaPortfolioScreen lanternaPortfolioScreen){
        this.lanternaPortfolioScreen = lanternaPortfolioScreen;
    }
}