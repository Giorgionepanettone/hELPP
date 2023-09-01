package TestControllerApplicativiJava;


import gz.helpp.Bean.BeanString;
import gz.helpp.ControllerApplicativi.ControllerApplicativoLogIn;
import org.junit.jupiter.api.Test;


import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class TestControllerApplicativoLogIn{
    private static final String username = "open";

    private static final String pwd = "open";

    private boolean result;

    BeanString beanUsername = new BeanString();
    BeanString beanpwd = new BeanString();

    @Test
    void testLogInCorrect(){
        beanUsername.setString(username);
        beanpwd.setString(pwd);

        result = false;
        ControllerApplicativoLogIn controllerApplicativoLogIn = new ControllerApplicativoLogIn();

        assertThrows(NullPointerException.class, () ->controllerApplicativoLogIn.validateLogIn(beanUsername, beanpwd));
    }

    @Test
    void testLogInWrong() throws SQLException {
        beanUsername.setString("ImpossibleUsername");
        beanpwd.setString("ImpossiblePassword1111111111111111111111");

        ControllerApplicativoLogIn controllerApplicativoLogIn = new ControllerApplicativoLogIn();
        result = controllerApplicativoLogIn.validateLogIn(beanUsername, beanpwd);
        assertEquals(false, result);
    }

    @Test
    void testLogInBlankCredentials() throws SQLException { //edge case
        beanUsername.setString("");
        beanpwd.setString("");

        ControllerApplicativoLogIn controllerApplicativoLogIn = new ControllerApplicativoLogIn();
        result = controllerApplicativoLogIn.validateLogIn(beanUsername, beanpwd);
        assertEquals(false, result);
    }

}