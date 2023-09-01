package gz.helpp.Model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.sql.Date;

@Document(collection = "transactions", schemaVersion = "1.0")
public class ModelTransaction{
    @Id
    private int transactionId;
    private String usernameAssociated;
    private String cryptoTicker;
    private double quantity;
    private double price; //expressed in euro
    public enum Type{
        BUY,SELL,DEPOSIT,WITHDRAW
    }
    private Type type;
    private Date date;

    public int getTransactionId() {

        return this.transactionId;
    }

    public String getUsernameAssociated(){

        return this.usernameAssociated;
    }

    public String getCryptoTicker(){

        return this.cryptoTicker;
    }

    public double getQuantity() {

        return this.quantity;
    }

    public Date getDate() {

        return this.date;
    }

    public double getPrice() {

        return this.price;
    }

    public Type getType() {

        return this.type;
    }

    public void setCryptoTicker(String cryptoTicker) {

        this.cryptoTicker = cryptoTicker;
    }

    public void setDate(Date date) {

        this.date = date;
    }

    public void setPrice(double price) {

        this.price = price;
    }

    public void setQuantity(double quantity) {

        this.quantity = quantity;
    }

    public void setType(Type type) {

        this.type = type;
    }

    public void setUsernameAssociated(String usernameAssociated) {

        this.usernameAssociated = usernameAssociated;
    }

    public void setTransactionId(int transactionId){

        this.transactionId = transactionId;
    }
}