package gz.helpp.controllerApplicativi;

import gz.helpp.observerPatternImpl.CryptoUpdater;
import gz.helpp.strategyPattern.InterfacciaControllerGrafico;

public class ControllerApplicativoMainMenu{
    private InterfacciaControllerGrafico interfacciaControllerGrafico;

    public void bind(InterfacciaControllerGrafico interfacciaControllerGrafico){
        this.interfacciaControllerGrafico = interfacciaControllerGrafico;
    }

    public void buy() {
        CryptoUpdater.getInstance();

        interfacciaControllerGrafico.initializer();
    }

    public void portfolioOrProfileOrWithdrawOrDeposit(){

        interfacciaControllerGrafico.initializer();
    }
}