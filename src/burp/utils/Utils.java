package burp.utils;

import burp.IBurpExtenderCallbacks;
import burp.IContextMenuInvocation;
import burp.IExtensionHelpers;
import burp.IHttpRequestResponse;

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
    public static final LoadConfig loadConn = new LoadConfig();

}
