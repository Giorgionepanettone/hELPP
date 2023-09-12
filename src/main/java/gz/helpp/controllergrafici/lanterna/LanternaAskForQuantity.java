package gz.helpp.controllergrafici.lanterna;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import gz.helpp.bean.BeanTransaction;
import gz.helpp.controllerapplicativi.ControllerApplicativoBuyMenu;
import gz.helpp.controllerapplicativi.ControllerApplicativoPortfolioScreen;
import gz.helpp.model.ModelSession;
import gz.helpp.model.ModelTransaction;
import gz.helpp.strategypattern.InterfacciaControllerGrafico;
import gz.helpp.utils.HandleDepositOrWithdrawHelper;
import gz.helpp.utils.LanternaCommonCodeUtils;

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
                    ModelSession.getLogger().error("LanternaAskForQuantity initializer method errror", e);
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

        BeanTransaction beanTransaction = new BeanTransaction();
        beanTransaction.setQuantity(quantityTextField.getText());


        if(!beanTransaction.isQuantityValid()){
            errorLabel.setText("invalid input");
            return;
        }

        beanTransaction.setTicker(ticker);

        beanTransaction.setPrice(price);

        LanternaRecap lanternaRecap = new LanternaRecap(ticker, price, beanTransaction.getQuantity(), this.type);


        if(this.type.equals(ModelTransaction.Type.BUY)){
            ControllerApplicativoBuyMenu controllerApplicativoBuyMenu = new ControllerApplicativoBuyMenu();
            controllerApplicativoBuyMenu.bind(lanternaRecap);

            if(!controllerApplicativoBuyMenu.proceedBuy(beanTransaction)){
                errorLabel.setText("insufficient balance");
                return;
            }

        }
        else if(this.type.equals(ModelTransaction.Type.SELL)){
            ControllerApplicativoPortfolioScreen controllerApplicativoPortfolioScreen = new ControllerApplicativoPortfolioScreen();
            controllerApplicativoPortfolioScreen.bind(lanternaRecap);

            if(controllerApplicativoPortfolioScreen.proceedSell(beanTransaction)){
                lanternaPortfolioScreen.updateRow(beanTransaction);
            }
            else{
                this.errorLabel.setText("Not enough coins owned");
                return;
            }
        }
        else if(! HandleDepositOrWithdrawHelper.depositOrWithDraw(type, beanTransaction)){
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