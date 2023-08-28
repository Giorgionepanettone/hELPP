package gz.helpp;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.sql.SQLException;


public class LanternaAskForQuantity extends BasicWindow implements InterfacciaControllerGrafico{
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
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        try {
            screen = terminalFactory.createScreen();
            screen.startScreen();
            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            window = new BasicWindow("Ask for Quantity");

            Panel contentPanel = new Panel();
            contentPanel.setLayoutManager(new GridLayout(2));

            Label priceLabel = new Label(Double.toString(price));
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

            window.setComponent(contentPanel);

            textGUI.addWindowAndWait(window);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void proceedButtonFunc() throws SQLException, IOException {
        QuantityBean quantityBean = new QuantityBean();
        String quantityString = quantityTextField.getText();
        quantityBean.setQuantity(quantityString);

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
        beanPrice.setNumber(price);

        LanternaRecap lanternaRecap = new LanternaRecap(ticker, price, Double.parseDouble(quantityString), this.type);


        if(this.type.equals(ModelTransaction.Type.BUY)){
            ControllerApplicativoBuyMenu controllerApplicativoBuyMenu = new ControllerApplicativoBuyMenu();
            controllerApplicativoBuyMenu.bind(lanternaRecap);

            if(!controllerApplicativoBuyMenu.proceedBuy(quantityBean, beanUser, beanTicker, beanPrice)){
                errorLabel.setText("insufficient balance");
                return;
            }

        }
        else if(this.type.equals(ModelTransaction.Type.SELL)){
            ControllerApplicativoPortfolioScreen controllerApplicativoPortfolioScreen = new ControllerApplicativoPortfolioScreen();
            controllerApplicativoPortfolioScreen.bind(lanternaRecap);

            if(controllerApplicativoPortfolioScreen.proceedSell(quantityBean, beanUser, beanTicker, beanPrice)){
                lanternaPortfolioScreen.updateRow(beanTicker, quantityBean);
            }
            else{
                this.errorLabel.setText("Not enough coins owned");
                return;
            }
        }
        else if(this.type.equals(ModelTransaction.Type.DEPOSIT)){
            ControllerApplicativoDepositMenu controllerApplicativoDepositMenu = new ControllerApplicativoDepositMenu();

            QuantityBean beanQuantity = new QuantityBean();
            beanQuantity.setQuantity(quantityTextField.getText());
            controllerApplicativoDepositMenu.deposit(beanQuantity);
        }
        else if(this.type.equals(ModelTransaction.Type.WITHDRAW)){
            ControllerApplicativoWithdrawMenu controllerApplicativoWithdrawMenu = new ControllerApplicativoWithdrawMenu();
            QuantityBean beanQuantity = new QuantityBean();
            beanQuantity.setQuantity(quantityTextField.getText());
            if(!controllerApplicativoWithdrawMenu.withdraw(quantityBean)){
                this.errorLabel.setText("not enough money");
                return;
            }
        }
        window.close();
        screen.close();
    }

    public void bindLanternaPortfolioScreen(LanternaPortfolioScreen lanternaPortfolioScreen){
        this.lanternaPortfolioScreen = lanternaPortfolioScreen;
    }

    public void closeOpenResources() throws IOException {
        screen.close();
    }
}