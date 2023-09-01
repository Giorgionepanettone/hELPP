package gz.helpp.ObserverPatternImpl;

import gz.helpp.ObserverPattern.Observer;
import gz.helpp.ObserverPattern.Subject;
import org.knowm.xchange.bitstamp.service.BitstampMarketDataService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//class that handles updating cryyyypto
public class CryptoUpdater implements Subject { //singleton
    public final List<Observer> observers = new ArrayList<>();
    private static CryptoUpdater instance;
    public final Object lock = new Object();

    private DataRetrievingThread dataRetrievingThread;
    private BitstampMarketDataService bitstampMarketDataService;
    private CryptoUpdater(){
        dataRetrievingThread = new DataRetrievingThread();
        dataRetrievingThread.start();
    }
    public static CryptoUpdater getInstance(){
        if(instance == null){
            instance = new CryptoUpdater();
        }
        return instance;
    }

    public void setState(BitstampMarketDataService bitstampMarketDataService){

        this.bitstampMarketDataService = bitstampMarketDataService;
    }

    public BitstampMarketDataService getState(){

        return this.bitstampMarketDataService;
    }
    public synchronized void register(Observer obj){
        synchronized (lock) {
            observers.add(obj);
            lock.notifyAll();
        }
    }

    public synchronized void unregister(Observer obj){
        synchronized (lock) {
            observers.remove(obj);
        }

    }

    public synchronized void notifyObservers() throws IOException {
        for(Observer observer : observers){
            observer.update();
        }
    }
}