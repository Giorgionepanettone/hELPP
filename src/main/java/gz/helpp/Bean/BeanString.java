package gz.helpp.Bean;


public class BeanString{
    private String string;
    public void setString(String string){
        this.string = string;
    }
    public String getString(){
        return this.string;
    }
    public boolean checkValidity(){
        return(!this.string.equals(""));
    }
}