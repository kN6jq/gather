package burp.model;

import burp.entry.CustomLineEntry;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

import static burp.utils.Utils.stderr;

public class CustomTableModel extends AbstractTableModel {
    private static final List<String> titletList = CustomLineEntry.fetchTableHeaderList();
    private CustomLineEntry currentlyDisplayedItem;
    private List<CustomLineEntry> lineEntries = new ArrayList<>();

    public static List<String> getTitletList() {
        return titletList;
    }

    public List<CustomLineEntry> getLineEntries() {
        return lineEntries;
    }

    public void setLineEntries(List<CustomLineEntry> lineEntries) {
        this.lineEntries = lineEntries;
    }

    public void addLineEntries(List<CustomLineEntry> lineEntries) {
        this.lineEntries.addAll(lineEntries);
    }

    @Override
    public int getRowCount() {
        return lineEntries.size();
    }

    @Override
    public int getColumnCount() {
        return titletList.size();//the one is the request String + response String,for search
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {

        if (columnIndex == titletList.indexOf("#")) {
            return Integer.class;//id
        }
        return String.class;
    }

    //define header of table???
    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex >= 0 && columnIndex < titletList.size()) {
            return titletList.get(columnIndex);
        } else {
            return "";
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (col == 1) {
            lineEntries.get(row).setName((String) value);
        } else if (col == 2) {
            lineEntries.get(row).setTemplate((String) value);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0)
            return rowIndex;
        CustomLineEntry entries = lineEntries.get(rowIndex);
        if (entries == null) return "";
        try {
            return entries.fetchValue(CustomLineEntry.fetchTableHeaderList().get(columnIndex));
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace(stderr);
            return "error";
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }

    public void setCurrentlyDisplayedItem(CustomLineEntry currentlyDisplayedItem) {
        this.currentlyDisplayedItem = currentlyDisplayedItem;
    }
}
