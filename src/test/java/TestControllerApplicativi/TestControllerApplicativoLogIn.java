package TestControllerApplicativi;

import gz.helpp.bean.BeanLogIn;
import gz.helpp.controllerapplicativi.ControllerApplicativoLogIn;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestControllerApplicativoLogIn{
    private static final String username = "open";

    private static final String pwd = "open";

    private boolean result;

    BeanLogIn beanLogIn = new BeanLogIn();

    @Test
    void testLogInCorrect(){
        beanLogIn.setUserName(username);
        beanLogIn.setPassword(pwd);

        result = false;
        ControllerApplicativoLogIn controllerApplicativoLogIn = new ControllerApplicativoLogIn();

        assertThrows(NullPointerException.class, () ->controllerApplicativoLogIn.validateLogIn(beanLogIn));
    }

    @Test
    void testLogInWrong() throws SQLException {
        beanLogIn.setUserName("ImpossibleUsername");
        beanLogIn.setPassword("ImpossiblePassword1111111111111111111111");

        ControllerApplicativoLogIn controllerApplicativoLogIn = new ControllerApplicativoLogIn();
        result = controllerApplicativoLogIn.validateLogIn(beanLogIn);
        assertEquals(false, result);
    }

    @Test
    void testLogInBlankCredentials() throws SQLException { //edge case
        beanLogIn.setUserName("");
        beanLogIn.setPassword("");

        ControllerApplicativoLogIn controllerApplicativoLogIn = new ControllerApplicativoLogIn();
        result = controllerApplicativoLogIn.validateLogIn(beanLogIn);
        assertEquals(false, result);
    }

}