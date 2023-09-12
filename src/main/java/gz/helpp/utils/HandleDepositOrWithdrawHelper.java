package gz.helpp.utils;

import gz.helpp.bean.BeanTransaction;
import gz.helpp.controllerapplicativi.ControllerApplicativoDepositMenu;
import gz.helpp.controllerapplicativi.ControllerApplicativoWithdrawMenu;
import gz.helpp.model.ModelTransaction;

import java.sql.SQLException;

public class HandleDepositOrWithdrawHelper{

    private HandleDepositOrWithdrawHelper(){
        throw new IllegalStateException("cant instantiate util class HandleDepositOrWithdrawHelper");
    }
    public static boolean depositOrWithDraw(ModelTransaction.Type type, BeanTransaction beanTransaction) throws SQLException {

        if(type.equals(ModelTransaction.Type.DEPOSIT)){
            ControllerApplicativoDepositMenu controllerApplicativoDepositMenu = new ControllerApplicativoDepositMenu();

            controllerApplicativoDepositMenu.deposit(beanTransaction);
            return true;
        }
        if(type.equals(ModelTransaction.Type.WITHDRAW)){
            ControllerApplicativoWithdrawMenu controllerApplicativoWithdrawMenu = new ControllerApplicativoWithdrawMenu();

            return controllerApplicativoWithdrawMenu.withdraw(beanTransaction);
        }
        return false;
    }
}