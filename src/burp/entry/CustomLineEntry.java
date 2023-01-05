package burp.entry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class CustomLineEntry {

    private static LinkedHashMap<String, Integer> preferredWidths = new LinkedHashMap<String, Integer>();
    private String name;
    private String template;

    public CustomLineEntry() {
    }

    public CustomLineEntry(String name, String template) {
        this.name = name;
        this.template = template;
    }

    public static LinkedHashMap<String, Integer> fetchTableHeaderAndWidth() {
        preferredWidths.put("#", 4);
        preferredWidths.put("name", 15);
        preferredWidths.put("template", 100);
        return preferredWidths;
    }

    public static List<String> fetchTableHeaderList() {
        LinkedHashMap<String, Integer> headers = fetchTableHeaderAndWidth();
        List<String> keys = new ArrayList<>(headers.keySet());
        return keys;
    }

    public static void main(String args[]) {
        System.out.println(fetchTableHeaderList());
    }

    public Object fetchValue(String paraName) throws Exception {
        //Field[] fields = LineEntry.class.getDeclaredFields();
        Field field = CustomLineEntry.class.getDeclaredField(paraName);
        return field.get(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

}
