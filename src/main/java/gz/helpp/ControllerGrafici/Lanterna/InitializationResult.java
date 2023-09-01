package gz.helpp.ControllerGrafici.Lanterna;


import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

public class InitializationResult {
    private final DefaultTerminalFactory terminalFactory;
    private final WindowBasedTextGUI textGUI;
    private final BasicWindow window;
    private final Panel contentPanel;

    public InitializationResult(DefaultTerminalFactory terminalFactory, WindowBasedTextGUI textGUI, BasicWindow window, Panel contentPanel){
        this.terminalFactory = terminalFactory;
        this.textGUI = textGUI;
        this.window = window;
        this.contentPanel = contentPanel;
    }

    public DefaultTerminalFactory getTerminalFactory(){
        return this.terminalFactory;
    }

    public WindowBasedTextGUI getTextGUI() {
        return textGUI;
    }

    public BasicWindow getWindow() {
        return window;
    }

    public Panel getContentPanel() {
        return contentPanel;
    }
}