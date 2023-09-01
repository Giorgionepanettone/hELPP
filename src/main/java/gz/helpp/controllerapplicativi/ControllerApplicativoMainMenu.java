package gz.helpp.controllerapplicativi;

import gz.helpp.observerpatternimpl.CryptoUpdater;
import gz.helpp.strategypattern.InterfacciaControllerGrafico;

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