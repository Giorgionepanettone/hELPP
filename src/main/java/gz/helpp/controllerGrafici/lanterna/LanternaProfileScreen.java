package gz.helpp.controllerGrafici.lanterna;


import com.googlecode.lanterna.gui2.*;
import gz.helpp.model.ModelSession;
import gz.helpp.strategyPattern.InterfacciaControllerGrafico;
import gz.helpp.utils.LanternaCommonCodeUtils;

public class LanternaProfileScreen extends BasicWindow implements InterfacciaControllerGrafico {

    public void initializer(){
        try {
            Panel contentPanel = new Panel();
            contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
            contentPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

            Label usernameLabel = new Label("Username: " + ModelSession.getInstance().getModelUser().getUserName());
            contentPanel.addComponent(usernameLabel);

            Label balanceLabel = new Label("Balance: " + ModelSession.getInstance().getModelUser().getBalance());
            contentPanel.addComponent(balanceLabel);

            Label emailLabel = new Label("Email: " + ModelSession.getInstance().getModelUser().getEmail());
            contentPanel.addComponent(emailLabel);

            InitializationResult initializationResult = LanternaCommonCodeUtils.createAndInitializeWindow("Profile", contentPanel);
            initializationResult.getTextGUI().addWindowAndWait(initializationResult.getWindow());
        } catch (Exception e) {
            ModelSession.getLogger().error("LanternaProfileScreen initializer method error", e);
        }
    }
}