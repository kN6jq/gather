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
}
