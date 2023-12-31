package gz.helpp.observerpattern;

import java.io.IOException;

public interface Subject{

    public void register(Observer obj);
    public void unregister(Observer obj);

    public void notifyObservers() throws IOException;
}