package gz.helpp.ControllerApplicativi;

import gz.helpp.Bean.BeanString;
import gz.helpp.Dao.UserDao;
import gz.helpp.Model.ModelSession;
import gz.helpp.Model.ModelUser;
import gz.helpp.StrategyPattern.InterfacciaControllerGrafico;

import java.sql.SQLException;

public class ControllerApplicativoLogIn {

    private InterfacciaControllerGrafico interfacciaControllerGrafico;

    public void link(InterfacciaControllerGrafico interfacciaControllerGrafico){
        this.interfacciaControllerGrafico = interfacciaControllerGrafico;
    }

    public boolean validateLogIn(BeanString username, BeanString password) throws SQLException {

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