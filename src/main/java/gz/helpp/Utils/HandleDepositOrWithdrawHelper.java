package gz.helpp.Utils;


import gz.helpp.Bean.BeanQuantity;
import gz.helpp.ControllerApplicativi.ControllerApplicativoDepositMenu;
import gz.helpp.ControllerApplicativi.ControllerApplicativoWithdrawMenu;
import gz.helpp.Model.ModelTransaction;

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