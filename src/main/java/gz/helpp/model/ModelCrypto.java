package gz.helpp.model;

public class ModelCrypto{
    private final String ticker;
    private final String name;
    private Double price;


    public ModelCrypto(String ticker, String name){
        this.ticker = ticker;
        this.name = name;
    }
    public String getTicker(){

        return this.ticker;
    }

    public String getName(){

        return this.name;
    }

    public double getPrice(){

        return this.price;
    }

    public void setPrice(double price){

        this.price = price;
    }
}