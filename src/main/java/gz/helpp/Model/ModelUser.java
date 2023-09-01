package gz.helpp.Model;

import gz.helpp.ObserverPattern.BalanceObserver;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelUser{
    private int userId;
    private String password;
    private String userName;
    private boolean isFinancialAdvisor;
    private double balance;
    private Map<String, Pair<Double, Double>> portfolio;
    private String email;
    private List<BalanceObserver> observers = new ArrayList<>();

    public ModelUser( String userName, boolean isFinancialAdvisor, Map<String, Pair<Double, Double>> portfolio, double balance){
        this.userName = userName;
        this.isFinancialAdvisor = isFinancialAdvisor;
        this.portfolio = portfolio;
        this.balance = balance;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    public void setIsFinancialAdvisor(boolean isFinancialAdvisor){

        this.isFinancialAdvisor = isFinancialAdvisor;
    }

    public void setEmail(String email){

        this.email = email;
    }

    public void setBalance(double newBalance){
        this.balance = newBalance;
        notifyObservers(newBalance);
    }

    public void addObserver(BalanceObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(BalanceObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(double newBalance) {
        for (BalanceObserver observer : observers) {
            try{
                observer.balanceChanged(newBalance);
            }
            catch(Exception e){
                removeObserver(observer);
            }
        }
    }
    public void setPassword(String password){

        this.password = password;
    }
    public void setPortfolio(Map<String, Pair<Double, Double>> portfolio){

        this.portfolio = portfolio;
    }

    public void setUserId(int id){
        this.userId = id;
    }
    public int getUserId(){

        return this.userId;
    }

    public String getEmail(){

        return this.email;
    }
    public String getUserName(){

        return this.userName;
    }

    public String getPassword(){

        return this.password;
    }
    public boolean getIsFinancialAdvisor(){

        return this.isFinancialAdvisor;
    }

    public Map<String, Pair<Double, Double>> getPortfolio(){

        return this.portfolio;
    }

    public double getBalance(){

        return this.balance;
    }

    public static int toNumeralString(final Boolean input) {

        return Boolean.TRUE.equals(input) ? 1 : 0;
    }
}