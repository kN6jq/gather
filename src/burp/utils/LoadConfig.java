package burp.utils;

import burp.entry.CustomLineEntry;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static burp.utils.Utils.*;

public class LoadConfig {
    private static Yaml yaml;
    private static Map<String, Object> cache = new HashMap<>();
    DumperOptions options = new DumperOptions();

    public LoadConfig() {
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(options);
        checkConfigDir();
        File yamlSetting = new File(configfile);
        if (!yamlSetting.exists()) {
            // 初次使用创建配置文件
            initSetting();
        } else {
            readFromDisk();
        }
    }

    public static void checkConfigDir() {
        File dir = new File(workdir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public String getDnslog() {
        return cache.get("dnslog").toString();
    }

    public void setDnslog(String key) {
        cache.put("dnslog", key);
    }

    public List<CustomLineEntry> getCustom() {
        return (List<CustomLineEntry>) cache.get("Custom");
    }

    public void setCustom(List<CustomLineEntry> customLineEntries) {
        cache.put("Custom", customLineEntries);
    }

    private void initSetting() {
        cache.put("dnslog", "");
        cache.put("Custom", new ArrayList<CustomLineEntry>(Collections.singletonList(new CustomLineEntry("", ""))));
        saveToDisk();
    }

    private void readFromDisk() {
        try {
            cache = yaml.load(new InputStreamReader(new FileInputStream(configfile)));
        } catch (FileNotFoundException e) {
            stderr.println(e.getMessage());
        }
    }

    public void saveToDisk() {
        try {
            Writer ws = new OutputStreamWriter(new FileOutputStream(configfile), StandardCharsets.UTF_8);
            yaml.dump(cache, ws);
        } catch (FileNotFoundException e) {
            stderr.println(e.getMessage());
        }
    }

}
