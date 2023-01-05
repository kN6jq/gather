package burp.table;

import burp.entry.CustomLineEntry;
import burp.menu.CustomLineEntryMenu;
import burp.model.CustomTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import static burp.utils.Utils.stderr;

public class CustomTable extends JTable {
    CustomTableModel customTableModel;
    private JScrollPane tableJpanel;

    public CustomTable(CustomTableModel customTableModel) {
        this.customTableModel = customTableModel;
        this.setModel(customTableModel);
        tableinit();
        tableJpanel = table();
        registerListeners();
    }

    public JScrollPane getTable() {
        return tableJpanel;
    }

    public JScrollPane table() {
        JScrollPane scrollPaneRequests = new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //允许横向滚动条
        //scrollPaneRequests.setViewportView(titleTable);//titleTable should lay here.
        scrollPaneRequests.setViewportView(this);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//配合横向滚动条
        return scrollPaneRequests;
    }

    public void tableinit() {
        //Font f = new Font("Arial", Font.PLAIN, 12);
        Font f = this.getFont();
        FontMetrics fm = this.getFontMetrics(f);
        int width = fm.stringWidth("A");//一个字符的宽度

        Map<String, Integer> preferredWidths = CustomLineEntry.fetchTableHeaderAndWidth();

        for (String header : CustomTableModel.getTitletList()) {
            try {//避免动态删除表字段时，出错
                int multiNumber = preferredWidths.get(header);
                this.getColumnModel().getColumn(this.getColumnModel().getColumnIndex(header)).setPreferredWidth(width * multiNumber);
            } catch (Exception e) {
                e.printStackTrace(stderr);
            }
        }
        //this.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//配合横向滚动条
    }

    @Override
    public CustomTableModel getModel() {
        //return (LineTableModel) super.getModel();
        return customTableModel;
    }

    @Override
    public void changeSelection(int row, int col, boolean toggle, boolean extend) {
        // show the log entry for the selected row
        CustomLineEntry Entry = this.customTableModel.getLineEntries().get(row);

        this.customTableModel.setCurrentlyDisplayedItem(Entry);
        super.changeSelection(row, col, toggle, extend);
    }

    private void registerListeners() {
        CustomTable.this.setRowSelectionAllowed(true);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override//title表格中的鼠标右键菜单
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                        int[] rows = getSelectedRows();
                        int col = ((CustomTable) e.getSource()).columnAtPoint(e.getPoint()); // 获得列位置
                        if (rows.length == 1) {// 单行被选中，重新获取鼠标点击处为被选中行
                            int row = ((CustomTable) e.getSource()).rowAtPoint(e.getPoint());
                            int column = ((CustomTable) e.getSource()).columnAtPoint(e.getPoint());
                            ((CustomTable) e.getSource()).setColumnSelectionInterval(column, column);
                            ((CustomTable) e.getSource()).setRowSelectionInterval(row, row);
                        } else if (rows.length > 1) {
                            int row = ((CustomTable) e.getSource()).rowAtPoint(e.getPoint());
                            for (int j : rows) {
                                if (row == j) {
                                    row = -1;
                                    break;
                                }
                            }
                            if (row != -1) {
                                row = ((CustomTable) e.getSource()).rowAtPoint(e.getPoint());
                                int column = ((CustomTable) e.getSource()).columnAtPoint(e.getPoint());
                                ((CustomTable) e.getSource()).setColumnSelectionInterval(column, column);
                                ((CustomTable) e.getSource()).setRowSelectionInterval(row, row);
                            }
                        } else {
                            int row = ((CustomTable) e.getSource()).rowAtPoint(e.getPoint());
                            int column = ((CustomTable) e.getSource()).columnAtPoint(e.getPoint());
                            ((CustomTable) e.getSource()).setColumnSelectionInterval(column, column);
                            ((CustomTable) e.getSource()).setRowSelectionInterval(row, row);
                        }
                        rows = getSelectedRows();
                        new CustomLineEntryMenu(CustomTable.this, rows, col).show(e.getComponent(), e.getX(), e.getY());

                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mouseReleased(e);
            }
        });
    }

}