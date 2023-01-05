package burp;

import burp.menu.ToolsMenu;
import burp.ui.MainUi;

import java.io.PrintWriter;

import static burp.utils.Utils.*;

public class BurpExtender implements IBurpExtender {
    public static MainUi mainUi;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        cbs = callbacks;
        helpers = cbs.getHelpers();
        stdout = new PrintWriter(cbs.getStdout(), true);
        stderr = new PrintWriter(cbs.getStderr(), true);
        cbs.setExtensionName(EXTENSION_NAME);
        mainUi = new MainUi(cbs);
        cbs.customizeUiComponent(mainUi);
        cbs.addSuiteTab(mainUi);
        cbs.registerContextMenuFactory(new ToolsMenu());
    }
}


