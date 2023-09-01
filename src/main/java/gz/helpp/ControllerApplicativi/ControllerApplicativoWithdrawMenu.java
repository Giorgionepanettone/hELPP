package gz.helpp.ControllerApplicativi;

import gz.helpp.Bean.BeanQuantity;
import gz.helpp.Dao.UserDao;
import gz.helpp.Model.ModelSession;
import gz.helpp.Model.ModelUser;

import java.sql.SQLException;

public class ControllerApplicativoWithdrawMenu{

    public boolean withdraw(BeanQuantity beanQuantity) throws SQLException {
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