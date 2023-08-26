package gz.helpp;

import picocli.CommandLine.Command;

public class CommandLineInterfaceMainMenu implements InterfacciaControllerGrafico{

    public void initializer(){

    }
}

@Command(name = "profile", mixinStandardHelpOptions = true, description = "profile command")
class profileCommand implements Runnable{

    public void run(){
        ModelSession modelSession = ModelSession.getInstance();
        System.out.println("Username: " + modelSession.getModelUser().getNickName());
        System.out.println("Balance: " + Double.toString(modelSession.getModelUser().getBalance()));
        System.out.println("Email: " + modelSession.getModelUser().getEmail());
    }
}

@Command(name = "buyMenu", mixinStandardHelpOptions = true, description = "buyMenu command")
class buyMenuCommand implements Runnable{

    public void run(){

    }
}