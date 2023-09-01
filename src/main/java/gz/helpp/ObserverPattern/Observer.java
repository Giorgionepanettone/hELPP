package gz.helpp.ObserverPattern;

import java.io.IOException;

public interface Observer{
    //method to update the observer, used by subject
    public void update() throws IOException;
}