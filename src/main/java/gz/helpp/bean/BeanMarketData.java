package gz.helpp.bean;

import org.knowm.xchange.bitstamp.service.BitstampMarketDataService;

public class BeanMarketData{

    private BitstampMarketDataService bitstampMarketDataService;

    public BeanMarketData(BitstampMarketDataService bitstampMarketDataService){
        this.bitstampMarketDataService = bitstampMarketDataService;
    }

    public BitstampMarketDataService getBitstampMarketDataService() {
        return bitstampMarketDataService;
    }
}