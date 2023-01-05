package burp.utils;

import burp.Getter;
import burp.IHttpRequestResponse;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static burp.utils.Utils.*;


public class Commons {
    public static Random random = new Random();

    /**
     * 随机取 n 个字符
     *
     * @param n
     * @return String
     */
    public static String randomStr(int n) {
        StringBuilder s = new StringBuilder();
        char[] stringArray = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
                'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
                'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
                '7', '8', '9'};
        for (int i = 0; i < n; i++) {
            char num = stringArray[random.nextInt(stringArray.length)];
            s.append(num);
        }
        return s.toString();
    }

    /**
     * 随机获取long数值
     *
     * @return
     */
    public static long getRandomLong() {
        return Math.abs(random.nextLong());
    }

    /**
     * 随机获取 [min, max] 范围内的随机整数
     * eg: [1, 3] => 1, 2, 3
     * ps: min为0的时候会计算会少1，如：[0, 3] => 0, 1, 2
     *
     * @return random int
     */
    public static int getRandom(int min, int max) {
        return random.nextInt(max) % (max - min + 1) + min;
    }

    public static boolean isWindows() {
        String OS_NAME = System.getProperties().getProperty("os.name").toLowerCase();
        //System.out.println(OS_NAME);
        if (OS_NAME.contains("windows")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isWindows10() {
        String OS_NAME = System.getProperties().getProperty("os.name").toLowerCase();
        if (OS_NAME.equalsIgnoreCase("windows 10")) {
            return true;
        }
        return false;
    }

    public static boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        //Mac
        return (os.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        //linux or unix
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
    }

    public static String RequestToFile(IHttpRequestResponse message) {
        try {
            Getter getter = new Getter(helpers);
            String host = getter.getHost(message);
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("MMdd-HHmmss");
            String timeString = simpleDateFormat.format(new Date());
            String filename = host + "." + timeString + ".req";

            File requestFile = new File(workdir, filename);
            FileUtils.writeByteArrayToFile(requestFile, message.getRequest());
            return requestFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace(stderr);
            return null;
        }
    }

    public static String HostToFile(IHttpRequestResponse message) {
        try {
            Getter getter = new Getter(helpers);
            String host = getter.getHost(message);
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("MMdd-HHmmss");
            String timeString = simpleDateFormat.format(new Date());
            String filename = host + "." + timeString + ".req";

            File requestFile = new File(workdir, filename);
            FileUtils.writeStringToFile(requestFile, host);
            return requestFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace(stderr);
            return null;
        }
    }
}
