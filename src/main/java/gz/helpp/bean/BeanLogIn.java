package gz.helpp.bean;

public class BeanLogIn{
    private String userName;

    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public boolean isInputEmpty(){
        return (this.userName.equals("") || this.password.equals(""));
    }
}