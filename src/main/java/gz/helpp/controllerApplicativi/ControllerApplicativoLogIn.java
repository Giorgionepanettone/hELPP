package gz.helpp.controllerApplicativi;

import gz.helpp.bean.BeanString;
import gz.helpp.dao.UserDao;
import gz.helpp.model.ModelSession;
import gz.helpp.model.ModelUser;
import gz.helpp.strategyPattern.InterfacciaControllerGrafico;

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