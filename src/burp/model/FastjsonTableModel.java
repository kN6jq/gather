package burp.model;

import burp.IHttpRequestResponse;
import burp.IHttpService;
import burp.IMessageEditorController;
import burp.table.FastjsonTable;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FastjsonTableModel extends AbstractTableModel implements IMessageEditorController {
    public static final List<FastjsonTable> Udatas = new ArrayList<FastjsonTable>();
    public static IHttpRequestResponse currentlyDisplayedItem;

    /**
     * 新增任务至任务栏面板
     *
     * @param extensionMethod
     * @param url
     * @param status
     * @param requestResponse
     * @return int id
     */
    public int add(String extensionMethod,
                   String url, String status, String res,
                   IHttpRequestResponse requestResponse) {
        synchronized (this.Udatas) {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTime = sdf.format(d);

            int id = this.Udatas.size();
            this.Udatas.add(
                    new FastjsonTable(
                            id,
                            extensionMethod,
                            url,
                            status,
                            res,
                            startTime,
                            requestResponse
                    )
            );
            fireTableRowsInserted(id, id);
            return id;
        }
    }

    /**
     * 更新任务状态至任务栏面板
     *
     * @param id
     * @param extensionMethod
     * @param url
     * @param status
     * @param res
     * @param requestResponse
     * @return int id
     */
    public int save(int id, String extensionMethod, String url, String status,
                    String res, IHttpRequestResponse requestResponse) {
        FastjsonTable dataEntry = this.Udatas.get(id);
        String startTime = dataEntry.startTime;
        synchronized (this.Udatas) {
            this.Udatas.set(
                    id,
                    new FastjsonTable(
                            id,
                            extensionMethod,
                            url,
                            status,
                            res,
                            startTime,
                            requestResponse
                    )
            );
            fireTableRowsUpdated(id, id);
            return id;
        }
    }

    /**
     * 自定义Table
     */


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
        return this.Udatas.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "#";
            case 1:
                return "extensionMethod";
            case 2:
                return "url";
            case 3:
                return "status";
            case 4:
                return "res";
            case 5:
                return "startTime";

        }
        return null;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        FastjsonTable datas = this.Udatas.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return datas.id;
            case 1:
                return datas.extensionMethod;
            case 2:
                return datas.url;
            case 3:
                return datas.status;
            case 4:
                return datas.res;
            case 5:
                return datas.startTime;
        }
        return null;
    }
}
