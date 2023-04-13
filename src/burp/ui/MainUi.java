package burp.ui;

import burp.IBurpExtenderCallbacks;
import burp.ITab;

import javax.swing.*;
import java.awt.*;

import static burp.utils.Utils.*;

public class MainUi extends JPanel implements ITab {
    private JTabbedPane PaneRoot;
    private FastjsonPane fastjsonPane;
    private ConfigPane configPane;
    private AuthPane authPane;

    public MainUi(IBurpExtenderCallbacks callbacks) {
        initComponents();
    }

    public FastjsonPane getFastjsonPane() {
        return fastjsonPane;
    }

    public AuthPane getAuthPane() {
        return authPane;
    }

    public ConfigPane getConfigPane() {
        return configPane;
    }

    /**
     * 初始化ui组件
     */
    private void initComponents() {
        PaneRoot = new JTabbedPane();
        fastjsonPane = new FastjsonPane(PaneRoot);
        authPane = new AuthPane(PaneRoot);
        configPane = new ConfigPane();
        setLayout(new BorderLayout());

        {
            PaneRoot.addTab("Config", configPane);
            add(PaneRoot);
        }
        stdout.println(EXTENSION_NAME + " init success");
        stdout.println("AUTHOR: "+ EXTENSION_AUTHOR);
        stdout.println("VERSION: "+ EXTENSION_VERSION);
    }

    /**
     * 注册选项卡名称
     *
     * @return
     */
    @Override
    public String getTabCaption() {
        return EXTENSION_NAME;
    }

    /**
     * 注册选项卡组件
     *
     * @return
     */
    @Override
    public Component getUiComponent() {
        return this;
    }
}
