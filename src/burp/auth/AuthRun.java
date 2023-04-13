package burp.auth;

import burp.IHttpRequestResponse;
import burp.IRequestInfo;
import burp.ui.AuthPane;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static burp.utils.Utils.*;

public class AuthRun{
    private final IHttpRequestResponse baseRequestResponse;
    private AuthRequest authRequest;

    public AuthRun(IHttpRequestResponse baseRequestResponse) {
        this.baseRequestResponse = baseRequestResponse;
    }

    public AuthRun(IHttpRequestResponse baseRequestResponse, AuthRequest authRequest, String request, String method, String path, String url) {
        this.baseRequestResponse = baseRequestResponse;
        this.authRequest = authRequest;
        String urlWithoutQuery = "";
        try {
            URL url1 = new URL(url);
            String protocol = url1.getProtocol(); // 获取协议部分，这里是 http
            String host = url1.getHost(); // 获取主机名部分，这里是 192.168.11.3
            int port = url1.getPort(); // 获取端口号部分，这里是 7001
            urlWithoutQuery = protocol + "://" + host + ":" + port;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        List<AuthRequest> authRequests = new ArrayList<>();
        authRequests.addAll(new AuthPayload().prefix(method, path));
        List<AuthRequest> headers = new AuthPayload().headers(method, url);
        if (Objects.equals(method, "GET")) {
            for (AuthRequest value : authRequests) {
                authRequest = value;
                if (Objects.equals(authRequest.getMethod(), "GET")) {
                    String new_request = request.replaceFirst(path, authRequest.getPath());
                    IHttpRequestResponse response = cbs.makeHttpRequest(baseRequestResponse.getHttpService(), helpers.stringToBytes(new_request));
                    String requrl = urlWithoutQuery + authRequest.getPath();
                    String statusCode = String.valueOf(helpers.analyzeResponse(response.getResponse()).getStatusCode());
                    String length = String.valueOf(response.getResponse().length);
                    AuthPane.addLog(response, method, requrl,statusCode, length);
                }
            }
            for (AuthRequest header : headers) {
                IRequestInfo analyzeRequest = helpers.analyzeRequest(this.baseRequestResponse);
                List<String> httpheader = analyzeRequest.getHeaders();
                byte[] byte_Request = this.baseRequestResponse.getRequest();
                int bodyOffset = analyzeRequest.getBodyOffset();
                int len = byte_Request.length;
                byte[] body = Arrays.copyOfRange(byte_Request, bodyOffset, len);

                httpheader.add(header.getHeaders());
                byte[] message = helpers.buildHttpMessage(httpheader, body);
                IHttpRequestResponse response = cbs.makeHttpRequest(baseRequestResponse.getHttpService(), message);
                // 发送请求
                String requrl = urlWithoutQuery + authRequest.getPath();
                String statusCode = String.valueOf(helpers.analyzeResponse(response.getResponse()).getStatusCode());
                String length = String.valueOf(response.getResponse().length);
                AuthPane.addLog(response, method, requrl,statusCode, length);
            }
        } else if (Objects.equals(method, "POST")) {
            for (AuthRequest value : authRequests) {
                authRequest = value;
                if (Objects.equals(authRequest.getMethod(), "POST")) {
                    String new_request = request.replaceFirst(path, authRequest.getPath());
                    IHttpRequestResponse response = cbs.makeHttpRequest(baseRequestResponse.getHttpService(), helpers.stringToBytes(new_request));
                    String requrl = urlWithoutQuery + authRequest.getPath();
                    String statusCode = String.valueOf(helpers.analyzeResponse(response.getResponse()).getStatusCode());
                    String length = String.valueOf(response.getResponse().length);
                    AuthPane.addLog(response, method, requrl,statusCode, length);
                }
            }
            for (AuthRequest header : headers) {
                IRequestInfo analyzeRequest = helpers.analyzeRequest(this.baseRequestResponse);
                List<String> httpheader = analyzeRequest.getHeaders();
                byte[] byte_Request = this.baseRequestResponse.getRequest();
                int bodyOffset = analyzeRequest.getBodyOffset();
                int len = byte_Request.length;
                byte[] body = Arrays.copyOfRange(byte_Request, bodyOffset, len);

                httpheader.add(header.getHeaders());
                byte[] message = helpers.buildHttpMessage(httpheader, body);
                IHttpRequestResponse response = cbs.makeHttpRequest(baseRequestResponse.getHttpService(), message);
                // 发送请求
                String requrl = urlWithoutQuery + authRequest.getPath();
                String statusCode = String.valueOf(helpers.analyzeResponse(response.getResponse()).getStatusCode());
                String length = String.valueOf(response.getResponse().length);
                AuthPane.addLog(response, method, requrl,statusCode, length);
            }
        } else {
            stdout.println("Method not supported");
        }
    }

    public void run() {
        String method = helpers.analyzeRequest(baseRequestResponse).getMethod();
        String path = helpers.analyzeRequest(baseRequestResponse).getUrl().getPath();
        String request = helpers.bytesToString(baseRequestResponse.getRequest());
        String url = helpers.analyzeRequest(baseRequestResponse).getUrl().toString();
        new AuthRun(baseRequestResponse,authRequest,request, method, path, url);
    }
}
