package burp.model;

import burp.IHttpRequestResponse;
import burp.IHttpService;
import burp.IMessageEditorController;
import burp.table.AuthTable;
import burp.table.FastjsonTable;
import burp.ui.AuthPane;
import burp.ui.FastjsonPane;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class AuthTableModel extends AbstractTableModel implements IMessageEditorController {
    public static List<AuthPane.TablesData> auth = new ArrayList<AuthPane.TablesData>();
    public  IHttpRequestResponse currentlyDisplayedItem;
    @Override
    public IHttpService getHttpService() {
        return currentlyDisplayedItem.getHttpService();
    }

    @Override
    public byte[] getRequest() {
        return currentlyDisplayedItem.getRequest();
    }

    @Override
    public byte[] getResponse() {
        return currentlyDisplayedItem.getResponse();
    }

    @Override
    public int getRowCount() {
        return auth.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "id";
            case 1:
                return "method";
            case 2:
                return "url";
            case 3:
                return "status";
            case 4:
                return "length";
            case 5:
                return "startTime";
        }
        return null;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AuthPane.TablesData datas = auth.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return datas.id;
            case 1:
                return datas.method;
            case 2:
                return datas.url;
            case 3:
                return datas.status;
            case 4:
                return datas.length;
            case 5:
                return datas.startTime;
        }
        return null;
    }


}
