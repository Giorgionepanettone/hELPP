package gz.helpp.supportedcrypto;


public class CryptoList{
    private String[][] supportedCryptoList;

    public CryptoList(){
        supportedCryptoList = new String[][]{
                {"BTC", "Bitcoin"},
                {"ETH", "Ethereum"},
                {"LTC", "Litecoin"},
                {"XRP", "Ripple"},
                {"BCH", "Bitcoin Cash"},
                {"USDT", "Tether"},
                {"ADA", "Cardano"},
                {"MATIC", "Polygon"},
                {"DOGE", "Dogecoin"},
                {"SOL", "Solana"}
        };
    }

    public String[][] getCryptoList(){
        return this.supportedCryptoList;
    }
}