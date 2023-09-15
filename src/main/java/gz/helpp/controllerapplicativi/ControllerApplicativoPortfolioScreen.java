package gz.helpp.controllerapplicativi;

import gz.helpp.bean.BeanTransaction;
import gz.helpp.dao.TransactionDAO;
import gz.helpp.dao.UserDao;
import gz.helpp.model.ModelSession;
import gz.helpp.model.ModelTransaction;
import gz.helpp.model.ModelTransactionType;
import gz.helpp.model.ModelUser;
import gz.helpp.strategypattern.InterfacciaControllerGrafico;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.Map;

public class ControllerApplicativoPortfolioScreen{
    private InterfacciaControllerGrafico interfacciaControllerGrafico;

    public void bind(InterfacciaControllerGrafico interfacciaControllerGrafico) {
        this.interfacciaControllerGrafico = interfacciaControllerGrafico;
    }

    public boolean proceedSell(BeanTransaction beanTransaction) throws SQLException {
        double quantity = beanTransaction.getQuantity();
        String ticker = beanTransaction.getTicker();
        double price = beanTransaction.getPrice();

        ModelUser currUser = ModelSession.getInstance().getModelUser();

        if(currUser.getPortfolio().get(ticker).getKey() < quantity)
        {
            return false;
        }

        ModelTransaction transaction = new ModelTransaction();
        transaction.setUsernameAssociated(currUser.getUserName());
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setType(ModelTransactionType.Type.SELL);
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