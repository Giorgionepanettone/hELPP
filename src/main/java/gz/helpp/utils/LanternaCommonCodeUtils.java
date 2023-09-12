package gz.helpp.utils;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import gz.helpp.controllergrafici.lanterna.InitializationResult;
import gz.helpp.exceptions.LanternaScreenCreationException;

import java.io.IOException;

public class LanternaCommonCodeUtils{

    private LanternaCommonCodeUtils(){
        throw new IllegalStateException("utility class");
    }
    public static InitializationResult createAndInitializeWindow(String windowName, Panel contentPanel) throws IOException {
        Screen screen = null;
        try {
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
            screen = terminalFactory.createScreen();
            screen.startScreen();
            WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            BasicWindow window = new BasicWindow(windowName);


            window.setComponent(contentPanel);

            return new InitializationResult(terminalFactory, textGUI, window, contentPanel);
        }
        catch(IOException e){
            if(screen != null) screen.close();
            throw new LanternaScreenCreationException(windowName + "initialization error");
        }
    }
}