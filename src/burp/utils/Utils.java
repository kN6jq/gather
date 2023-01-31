package burp.utils;

import burp.IBurpExtenderCallbacks;
import burp.IExtensionHelpers;

import java.io.File;
import java.io.PrintWriter;

public class Utils {
    public static final String EXTENSION_NAME = "gather";
    public static final String EXTENSION_VERSION = "1.0.0";
    public static final String EXTENSION_AUTHOR = "xm17";
    public static IBurpExtenderCallbacks cbs;
    public static PrintWriter stdout;
    public static PrintWriter stderr;
    public static IExtensionHelpers helpers;
    public static String workdir = System.getProperty("user.home") + File.separator + ".gather";
    public static String configfile = workdir + File.separator + "gather.yaml";
    public static LoadConfig loadConn = new LoadConfig();
    public static String[] dslStr = {"contains(body_1, 'bingo')", "status_code_1 == 200 && !contains(body_3, 'bingo')", "regex('root:.*:0:0:', body)", "contains(body, 'bingo')", "contains(all_headers_1, 'text/html')"};
    public static String[] severitys = {"critical", "high", "medium", "low", "info"};
    public static String[] vulTypes = {"Arbitrary File Read", "Code Execution", "Command Execution", "Denial Of Service", "Information Disclosure", "Login Bypass", "Path Traversal", "SQL Injection", "SSRF", "XSS"};

    public static String xFormData = "headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:103.0) Gecko/20100101 Firefox/103.0',\n" +
            "                   'Content-Type': 'application/x-www-form-urlencoded',\n" +
            "                   }\n" +
            "        if self._check() == True:\n" +
            "            resp = requests.post(self.url + \"\", data=\"key=value\", headers=headers, timeout=10)\n" +
            "            if resp.status_code == 404 and (\"hit\" in resp.text):\n" +
            "                result['VerifyInfo'] = {}\n" +
            "                result['VerifyInfo']['URL'] = self.url\n" +
            "                result['VerifyInfo']['Result'] = \"isExist\"";
    public static String json = "headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:103.0) Gecko/20100101 Firefox/103.0',\n" +
            "                   \"Content-Type\": \"application/json\",\n" +
            "                   }\n" +
            "        if self._check() == True:\n" +
            "            resp = requests.post(self.url + \"\", data={\"a\": \"a\"}, headers=headers, timeout=10)\n" +
            "            if resp.status_code == 404 and (\"hit\" in resp.text):\n" +
            "                result['VerifyInfo'] = {}\n" +
            "                result['VerifyInfo']['URL'] = self.url\n" +
            "                result['VerifyInfo']['Result'] = \"isExist\"";
    public static String file = "headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:103.0) Gecko/20100101 Firefox/103.0', }\n" +
            "        data = {'typeStr': 'File'}\n" +
            "        PAYLOAD = '<?php phpinfo();?>'\n" +
            "        files = {'uploadFile': ('a.php', PAYLOAD, 'text/plain')}\n" +
            "        if self._check() == True:\n" +
            "            resp = requests.post(self.url + \"\", data=data, files=files, headers=headers, timeout=10)\n" +
            "            if resp.status_code == 404 and (\"hit\" in resp.text):\n" +
            "                result['VerifyInfo'] = {}\n" +
            "                result['VerifyInfo']['URL'] = self.url\n" +
            "                result['VerifyInfo']['Result'] = \"isExist\"";
}
