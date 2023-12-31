package gz.helpp.controllerapplicativi;

import gz.helpp.bean.BeanTransaction;
import gz.helpp.dao.UserDao;
import gz.helpp.model.ModelSession;
import gz.helpp.model.ModelUser;

import java.sql.SQLException;

public class ControllerApplicativoWithdrawMenu{

    public boolean withdraw(BeanTransaction beanTransaction) throws SQLException {
        double quantity = beanTransaction.getQuantity();
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