package burp.table;

import burp.IHttpRequestResponse;
import burp.model.FastjsonTableModel;

public class FastjsonTable {
    public int id;
    public String extensionMethod;
    public String url;
    public String status;
    public String res;
    public String startTime;
    public IHttpRequestResponse requestResponse;
    private FastjsonTableModel tableModel;

    public FastjsonTable(int id, String extensionMethod, String url, String status,
                         String res, String startTime,
                         IHttpRequestResponse requestResponse) {
        this.id = id;
        this.extensionMethod = extensionMethod;
        this.url = url;
        this.status = status;
        this.res = res;
        this.startTime = startTime;
        this.requestResponse = requestResponse;
    }


}
