package gz.helpp.bean;

public class BeanRegistration{

    private String userName;

    private String password;

    private String email;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isInputEmpty(){
        return (this.email.equals("") || this.password.equals("") || this.userName.equals(""));
    }
}