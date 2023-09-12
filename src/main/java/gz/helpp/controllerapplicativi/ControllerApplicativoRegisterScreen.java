package gz.helpp.controllerapplicativi;

import gz.helpp.bean.BeanRegistration;
import gz.helpp.dao.UserDao;
import gz.helpp.model.ModelUser;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ControllerApplicativoRegisterScreen{
    public void registerUser(BeanRegistration beanRegistration) throws SQLException {
        UserDao userDao = new UserDao();
        Map<String, Pair<Double, Double>> portfolio = new HashMap<>();
        String email = beanRegistration.getEmail();

        ModelUser modelUser = new ModelUser(beanRegistration.getUserName(), false, portfolio, 0, email);
        modelUser.setPassword(beanRegistration.getPassword());
        modelUser.setEmail(email);
        userDao.create(modelUser);
    }
}