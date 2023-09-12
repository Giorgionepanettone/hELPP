package gz.helpp.controllerapplicativi;

import gz.helpp.bean.BeanTransaction;
import gz.helpp.dao.UserDao;
import gz.helpp.model.ModelSession;
import gz.helpp.model.ModelUser;

import java.sql.SQLException;

public class ControllerApplicativoDepositMenu{

    public void deposit(BeanTransaction beanTransaction) throws SQLException {
        double quantity = beanTransaction.getQuantity();
        if(quantity == -1) return;

        ModelUser modelUser = ModelSession.getInstance().getModelUser();
        modelUser.setBalance(modelUser.getBalance() + quantity);

        ModelSession.getInstance().setModelUser(modelUser);

        UserDao userDao = new UserDao();
        userDao.update(modelUser);
    }
}