package gz.helpp.bean;


public class BeanQuantity {
    private String quantity;

    public void setQuantity(String quantity){
        this.quantity = quantity;
    }

    public double getQuantity(){
        double number;
        try{
            number = Double.parseDouble(quantity);
        } catch(NumberFormatException e){
            return -1;
        }
        if(number <= 0) {
            return -1;
        }
        return number;
    }

}