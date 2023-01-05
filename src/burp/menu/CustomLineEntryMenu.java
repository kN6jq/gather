package burp.menu;

import burp.entry.CustomLineEntry;
import burp.table.CustomTable;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CustomLineEntryMenu extends JPopupMenu {
    private static CustomTable customTable;

    public CustomLineEntryMenu(final CustomTable customTable, final int[] rows, final int columnIndex) {
        CustomLineEntryMenu.customTable = customTable;


        this.add(new JMenuItem(new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                customTable.getModel().getLineEntries().add(new CustomLineEntry("", ""));
                customTable.getModel().fireTableRowsInserted(customTable.getModel().getLineEntries().size() - 2, customTable.getModel().getLineEntries().size() - 1);
            }
        }));

        this.add(new JMenuItem(new AbstractAction("Del") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (customTable.getModel().getLineEntries().size() != 1)
                    for (int row : rows)
                        customTable.getModel().getLineEntries().remove(row);
            }
        }));
        this.add(new JMenuItem(new AbstractAction("Move Up") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (customTable.getModel().getLineEntries().size() != 1 && rows[0] != 0) {
                    CustomLineEntry tmp;
                    tmp = customTable.getModel().getLineEntries().get(rows[0] - 1);
                    customTable.getModel().getLineEntries().remove(rows[0] - 1);
                    customTable.getModel().getLineEntries().add(rows[rows.length - 1], tmp);
                    customTable.getModel().fireTableRowsInserted(rows[0] - 1, rows[rows.length - 1]);

                }
            }
        }));
        this.add(new JMenuItem(new AbstractAction("Move Down") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (customTable.getModel().getLineEntries().size() != 1 && rows[rows.length - 1] != customTable.getModel().getLineEntries().size() - 1) {
                    CustomLineEntry tmp;
                    tmp = customTable.getModel().getLineEntries().get(rows[rows.length - 1] + 1);
                    customTable.getModel().getLineEntries().remove(rows[rows.length - 1] + 1);
                    customTable.getModel().getLineEntries().add(rows[0], tmp);
                    customTable.getModel().fireTableRowsInserted(rows[0], rows[rows.length - 1] + 1);

                }
            }
        }));
    }
}
