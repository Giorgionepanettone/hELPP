package gz.helpp.observerpatternimpl;


import gz.helpp.model.ModelSession;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitstamp.BitstampExchange;
import org.knowm.xchange.bitstamp.service.BitstampMarketDataService;

import java.io.IOException;

public class DataRetrievingThread extends Thread{
    @Override
    public void run(){
        CryptoUpdater cryptoUpdater = CryptoUpdater.getInstance();
        while(true){
            synchronized (cryptoUpdater.lock) {
                while (cryptoUpdater.observers.isEmpty()) {
                    try {
                        cryptoUpdater.lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        ModelSession.getLogger().error("Model file, crypto retrieving data thread MyThread run method error", e);
                    }
                }
            }
            Exchange bitstamp = ExchangeFactory.INSTANCE.createExchange(BitstampExchange.class.getName());
            BitstampMarketDataService bitstampMarketDataService = (BitstampMarketDataService) bitstamp.getMarketDataService();
            cryptoUpdater.setState(bitstampMarketDataService);
            try {
                cryptoUpdater.notifyObservers();
            } catch (IOException e) {
                ModelSession.getLogger().error("Model file, crypto retrieving data thread MyThread notifyObservers() error", e);
            }
            try {
                Thread.sleep(1000); //change this depending on your needs
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                ModelSession.getLogger().error("Model file, crypto retrieving data thread MyThread Thread.sleep() error", e);
            }
        }
    }
}