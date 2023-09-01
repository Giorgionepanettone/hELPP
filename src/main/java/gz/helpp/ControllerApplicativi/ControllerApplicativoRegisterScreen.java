package gz.helpp.ControllerApplicativi;

import gz.helpp.Bean.BeanString;
import gz.helpp.Dao.UserDao;
import gz.helpp.Model.ModelUser;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ControllerApplicativoRegisterScreen{
    public void registerUser(BeanString username, BeanString email, BeanString password) throws SQLException {
        UserDao userDao = new UserDao();
        Map<String, Pair<Double, Double>> portfolio = new HashMap<>();
        ModelUser modelUser = new ModelUser(username.getString(), false, portfolio, 0);
        modelUser.setPassword(password.getString());
        modelUser.setEmail(email.getString());
        userDao.create(modelUser);
    }
}