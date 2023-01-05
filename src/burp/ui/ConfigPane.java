package burp.ui;

import burp.model.CustomTableModel;
import burp.table.CustomTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static burp.utils.Utils.loadConn;

/**
 * 靠  ui真难写
 */
public class ConfigPane extends JPanel {
    private JPanel customPanel;
    private JLabel customLabel;
    private JLabel customAddOrSub;
    private CustomTableModel customTableModel;
    private JLabel dnslogLabel;
    private JTextField dnslogBox;
    private JButton configSaveBtn;

    public ConfigPane() {
        init();
    }

    private void init() {
        customPanel = new JPanel();
        customLabel = new JLabel();
        customAddOrSub = new JLabel();
        customTableModel = new CustomTableModel();
        configSaveBtn = new JButton();
        dnslogLabel = new JLabel();
        dnslogBox = new JTextField();

        setLayout(new GridBagLayout());
        ((GridBagLayout) getLayout()).columnWidths = new int[]{0, 0, 0};
        ((GridBagLayout) getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0, 0};
        ((GridBagLayout) getLayout()).columnWeights = new double[]{0.0, 1.0, 1.0E-4};
        ((GridBagLayout) getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};


        // dnslog
        dnslogLabel.setText("dnslog:");
        add(dnslogLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.VERTICAL, new Insets(10, 5, 5, 5), 0, 0));
        //-+-+-+-+-+-+-+-+-+-+ configIdBox -+-+-+-+-+-+-+-+-+-+
        add(dnslogBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 5), 0, 0));


        //-+-+-+-+-+-+-+-+-+-+ custom -+-+-+-+-+-+-+-+-+-+
        customPanel.setLayout(new GridBagLayout());
        ((GridBagLayout) customPanel.getLayout()).columnWidths = new int[]{0, 0, 0};
        ((GridBagLayout) customPanel.getLayout()).rowHeights = new int[]{0};
        ((GridBagLayout) customPanel.getLayout()).columnWeights = new double[]{0.1, 0.9, 0};
        ((GridBagLayout) customPanel.getLayout()).rowWeights = new double[]{0.0};
        customLabel.setText("自定义send to:");
        customAddOrSub.setText("+");
        add(customLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.VERTICAL, new Insets(10, 5, 5, 5), 0, 0));

        add(new CustomTable(customTableModel).getTable(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 5), 0, 0));

        // config保存
        configSaveBtn.setText("Save");
        configSaveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {

                loadConn.setDnslog(dnslogBox.getText());
                loadConn.setCustom(customTableModel.getLineEntries());
                loadConn.saveToDisk();
            }
        });
        add(configSaveBtn, new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(10, 0, 5, 5), 0, 0));

        dnslogBox.setText(loadConn.getDnslog());
        customTableModel.addLineEntries(loadConn.getCustom());
    }

}
