package gz.helpp.utils;


import gz.helpp.bean.BeanQuantity;
import gz.helpp.controllerApplicativi.ControllerApplicativoDepositMenu;
import gz.helpp.controllerApplicativi.ControllerApplicativoWithdrawMenu;
import gz.helpp.model.ModelTransaction;

import java.sql.SQLException;

public class HandleDepositOrWithdrawHelper{
    public static boolean depositOrWithDraw(ModelTransaction.Type type, BeanQuantity beanQuantity) throws SQLException {

        if(type.equals(ModelTransaction.Type.DEPOSIT)){
            ControllerApplicativoDepositMenu controllerApplicativoDepositMenu = new ControllerApplicativoDepositMenu();

            controllerApplicativoDepositMenu.deposit(beanQuantity);
            return true;
        }
        if(type.equals(ModelTransaction.Type.WITHDRAW)){
            ControllerApplicativoWithdrawMenu controllerApplicativoWithdrawMenu = new ControllerApplicativoWithdrawMenu();

            return controllerApplicativoWithdrawMenu.withdraw(beanQuantity);
        }
        return false;
    }
}