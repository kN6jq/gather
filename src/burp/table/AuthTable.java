package burp.table;

import burp.IHttpRequestResponse;
import burp.IHttpService;
import burp.IMessageEditorController;




public class AuthTable implements IMessageEditorController {
    public int id;
    public String method;
    public String url;
    public String status;
    public String length;
    public String startTime;
    public IHttpRequestResponse requestResponse;


    public AuthTable(int id ,String method, String url, String status, String length, String startTime, IHttpRequestResponse requestResponse) {
        this.id = id;
        this.method = method;
        this.url = url;
        this.status = status;
        this.length = length;
        this.startTime = startTime;
        this.requestResponse = requestResponse;
    }

    @Override
    public IHttpService getHttpService() {
        return requestResponse.getHttpService();
    }

    @Override
    public byte[] getRequest() {
        return requestResponse.getRequest();
    }

    @Override
    public byte[] getResponse() {
        return requestResponse.getResponse();
    }


}
