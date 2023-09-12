package gz.helpp.bean;

public class BeanTransaction{

    private String quantity;

    private double price;

    private String ticker;

    public double getQuantity() {
        return Double.parseDouble(quantity);
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public boolean isQuantityValid(){
        double number;
        try{
            number = Double.parseDouble(quantity);
        } catch(NumberFormatException e){
            return false;
        }
        if(number <= 0) {
            return false;
        }
        return true;
    }
}