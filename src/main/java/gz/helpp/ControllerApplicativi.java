package gz.helpp;

import javafx.util.Pair;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;



class ControllerApplicativoLogIn {

    private InterfacciaControllerGrafico interfacciaControllerGrafico;

    public void link(InterfacciaControllerGrafico interfacciaControllerGrafico){
        this.interfacciaControllerGrafico = interfacciaControllerGrafico;
    }

    public boolean validateLogIn(BeanString username, BeanString password) throws SQLException{

        UserDao userDao = new UserDao();
        if(userDao.checkLogIn(username.getString(), password.getString())){

            ModelUser modelUser = userDao.read(username.getString());
            ModelSession.getInstance().setModelUser(modelUser);
            interfacciaControllerGrafico.initializer();
            return true;
        }
        else return false;
    }
}

class ControllerApplicativoRegisterScreen{
    public void registerUser(BeanString username, BeanString email, BeanString password) throws SQLException {
        UserDao userDao = new UserDao();
        Map<String, Pair<Double, Double>> portfolio = new HashMap<>();
        ModelUser modelUser = new ModelUser(username.getString(), false, portfolio, 0);
        modelUser.setPassword(password.getString());
        modelUser.setEmail(email.getString());
        userDao.create(modelUser);
    }
}

class ControllerApplicativoMainMenu{
    private InterfacciaControllerGrafico interfacciaControllerGrafico;

    public void bind(InterfacciaControllerGrafico interfacciaControllerGrafico){
        this.interfacciaControllerGrafico = interfacciaControllerGrafico;
    }

    public void buy() {
        CryptoUpdater.getInstance();

        //call to controller.initializer
        interfacciaControllerGrafico.initializer();
    }

    public void portfolioOrProfileOrWithdrawOrDeposit(){

        interfacciaControllerGrafico.initializer();
    }
}

class ControllerApplicativoBuyMenu{

    private InterfacciaControllerGrafico interfacciaControllerGrafico;

    public void bind(InterfacciaControllerGrafico interfacciaControllerGrafico){
        this.interfacciaControllerGrafico = interfacciaControllerGrafico;
    }

    public boolean proceedBuy(QuantityBean quantityBean, BeanString usernameBean, BeanString tickerBean, BeanDouble priceBean) throws SQLException {
        double quantity = quantityBean.getQuantity();
        String username = usernameBean.getString();
        String ticker = tickerBean.getString();
        double price = priceBean.getNumber();

        ModelUser currUser = ModelSession.getInstance().getModelUser();

        if(currUser.getBalance() < price * quantity){
            return false;
        }

        ModelTransaction transaction = new ModelTransaction();
        transaction.setUsernameAssociated(username);
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setType(ModelTransaction.Type.BUY);
        transaction.setCryptoTicker(ticker);
        long millis=System.currentTimeMillis();
        java.sql.Date date=new java.sql.Date(millis);
        transaction.setDate(date);

        TransactionDAO transactionDAO = new TransactionDAO();
        transactionDAO.create(transaction); //register transaction

        Double value = price * quantity;
        currUser.setBalance(currUser.getBalance() - value);
        Map<String, Pair<Double, Double>> portfolio = currUser.getPortfolio();


        if(portfolio.get(ticker) != null) {
            quantity += portfolio.get(ticker).getKey();
            value += portfolio.get(ticker).getValue();
        }
        Pair<Double, Double> pair = new Pair<>(quantity, value);

        portfolio.put(ticker, pair);
        currUser.setPortfolio(portfolio);
        UserDao userDao = new UserDao();
        userDao.update(currUser);

        interfacciaControllerGrafico.initializer();

        return true;

    }
}

class ControllerApplicativoPortfolioScreen{
    private InterfacciaControllerGrafico interfacciaControllerGrafico;

    public void bind(InterfacciaControllerGrafico interfacciaControllerGrafico) {
        this.interfacciaControllerGrafico = interfacciaControllerGrafico;
    }

    public boolean proceedSell(QuantityBean quantityBean, BeanString usernameBean, BeanString tickerBean, BeanDouble priceBean) throws SQLException {
        double quantity = quantityBean.getQuantity();
        String username = usernameBean.getString();
        String ticker = tickerBean.getString();
        double price = priceBean.getNumber();

        ModelUser currUser = ModelSession.getInstance().getModelUser();

        if(currUser.getPortfolio().get(ticker).getKey() < quantity)
        {
            return false;
        }

        ModelTransaction transaction = new ModelTransaction();
        transaction.setUsernameAssociated(username);
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setType(ModelTransaction.Type.SELL);
        transaction.setCryptoTicker(ticker);
        long millis=System.currentTimeMillis();
        java.sql.Date date=new java.sql.Date(millis);
        transaction.setDate(date);

        TransactionDAO transactionDAO = new TransactionDAO();
        transactionDAO.create(transaction); //register transaction

        Double value = price * quantity;
        currUser.setBalance(currUser.getBalance() + value);
        Map<String, Pair<Double, Double>> portfolio = currUser.getPortfolio();

        Pair <Double, Double> portfolioPair = portfolio.get(ticker);
        double percentage = 1 - quantity/portfolioPair.getKey();
        value = portfolioPair.getValue() * percentage;
        quantity = portfolioPair.getKey() - quantity;

        Pair<Double, Double> pair = new Pair<>(quantity, value);
        portfolio.put(ticker, pair);

        UserDao userDao = new UserDao();
        userDao.update(currUser);

        interfacciaControllerGrafico.initializer();
        return true;
    }

}

class ControllerApplicativoWithdrawMenu{

    public boolean withdraw(QuantityBean beanQuantity) throws SQLException {
        double quantity = beanQuantity.getQuantity();
        if(quantity == -1) return false;

        double balance = ModelSession.getInstance().getModelUser().getBalance();
        if(quantity > balance){
            return false;
        }

        ModelUser modelUser = ModelSession.getInstance().getModelUser();
        UserDao userDao = new UserDao();
        modelUser.setBalance(balance - quantity);
        userDao.update(modelUser);
        ModelSession.getInstance().setModelUser(modelUser);
        return true;
    }
}

class ControllerApplicativoDepositMenu{

    public void deposit(QuantityBean beanQuantity) throws SQLException {
        double quantity = beanQuantity.getQuantity();
        if(quantity == -1) return;

        ModelUser modelUser = ModelSession.getInstance().getModelUser();
        modelUser.setBalance(modelUser.getBalance() + quantity);

        ModelSession.getInstance().setModelUser(modelUser);

        UserDao userDao = new UserDao();
        userDao.update(modelUser);
    }
}

class BeanDouble{
    private Double number;

    public void setNumber(double number){
        this.number = number;
    }

    public double getNumber(){
        return this.number;
    }
}