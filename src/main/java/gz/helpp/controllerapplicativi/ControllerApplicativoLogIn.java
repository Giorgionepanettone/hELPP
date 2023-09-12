package gz.helpp.controllerapplicativi;

import gz.helpp.bean.BeanLogIn;
import gz.helpp.dao.UserDao;
import gz.helpp.model.ModelSession;
import gz.helpp.model.ModelUser;
import gz.helpp.strategypattern.InterfacciaControllerGrafico;

import java.sql.SQLException;

public class ControllerApplicativoLogIn {

    private InterfacciaControllerGrafico interfacciaControllerGrafico;

    public void link(InterfacciaControllerGrafico interfacciaControllerGrafico){
        this.interfacciaControllerGrafico = interfacciaControllerGrafico;
    }

    public boolean validateLogIn(BeanLogIn beanLogIn) throws SQLException {

        UserDao userDao = new UserDao();
        String user = beanLogIn.getUserName();

        if(userDao.checkLogIn(user, beanLogIn.getPassword())){

            ModelUser modelUser = userDao.read(user);
            ModelSession.getInstance().setModelUser(modelUser);
            interfacciaControllerGrafico.initializer();
            return true;
        }
        else return false;
    }
}