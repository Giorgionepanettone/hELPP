package gz.helpp.ControllerApplicativi;

import gz.helpp.Bean.BeanQuantity;
import gz.helpp.Dao.UserDao;
import gz.helpp.Model.ModelSession;
import gz.helpp.Model.ModelUser;

import java.sql.SQLException;

public class ControllerApplicativoDepositMenu{

    public void deposit(BeanQuantity beanQuantity) throws SQLException {
        double quantity = beanQuantity.getQuantity();
        if(quantity == -1) return;

        ModelUser modelUser = ModelSession.getInstance().getModelUser();
        modelUser.setBalance(modelUser.getBalance() + quantity);

        ModelSession.getInstance().setModelUser(modelUser);

        UserDao userDao = new UserDao();
        userDao.update(modelUser);
    }
}