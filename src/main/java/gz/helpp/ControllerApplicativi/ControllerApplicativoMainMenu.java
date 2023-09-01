package gz.helpp.ControllerApplicativi;

import gz.helpp.ObserverPatternImpl.CryptoUpdater;
import gz.helpp.StrategyPattern.InterfacciaControllerGrafico;

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