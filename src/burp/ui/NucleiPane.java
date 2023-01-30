package burp.ui;

import burp.*;
import burp.entry.Nuclei;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.swing.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static burp.utils.Commons.replaceurl;
import static burp.utils.Utils.*;

public class NucleiPane {
    private static final String Template_get = "id: #{[id]}\n" +
            "\n" +
            "info: \n" +
            "  name: #{[name]}\n" +
            "  author: #{[author]}\n" +
            "  severity: #{[severity]}\n" +
            "  description: #{[description]}\n" +
            "  tags: #{[tags]}\n" +
            "\n" +
            "requests:\n" +
            "  - method: GET\n" +
            "    path:\n" +
            "      - \"{{BaseURL}}#{[path]}\"\n" +
            "    headers:  \n" +
            "      #{[headers]}\n" +
            "    req-condition: true\n" +
            "    matchers:\n" +
            "      - type: dsl\n" +
            "        dsl:\n" +
            "          - \"#{[dsl]}\"";
    private static final String Template_post = "id: #{[id]}\n" +
            "\n" +
            "info:\n" +
            "  name: #{[name]}\n" +
            "  author: #{[author]}\n" +
            "  severity: #{[severity]}\n" +
            "  description: #{[description]}\n" +
            "  reference:\n" +
            "    - #{[reference]}\n" +
            "  tags: #{[tags]}\n" +
            "\n" +
            "requests:\n" +
            "  - raw:\n" +
            "      - |\n" +
            "        #{[raw]}\n" +
            "\n" +
            "    matchers:\n" +
            "      - type: dsl\n" +
            "        dsl:\n" +
            "          - \"#{[dsl]}\"\n";
    private static IHttpRequestResponse selectedMessage;
    private static HelperPlus helperPlus;
    private static Getter gethelper;
    private static URL url;
    private static String dsl;
    private static String severity;
    private static String name;
    private static String author;
    private static IRequestInfo analyzeRequest;


    public NucleiPane(IContextMenuInvocation iContextMenuInvocation) {
        selectedMessage = iContextMenuInvocation.getSelectedMessages()[0];
        helperPlus = new HelperPlus(helpers);
        url = helperPlus.getFullURL(selectedMessage);
        analyzeRequest = helpers.analyzeRequest(selectedMessage);
        name = JOptionPane.showInputDialog(null, "请输入模板名称");
        author = JOptionPane.showInputDialog(null, "请输入作者名称");
        severity = JOptionPane.showInputDialog(null, "请选择漏洞等级", "选择框", 1, null, severitys, severitys[0]).toString();
        dsl = JOptionPane.showInputDialog(null, "请选择表达式demo", "选择框", 1, null, dslStr, dslStr[0]).toString();

    }

    public static String NucleiGet() {
        Nuclei nuclei = new Nuclei();
        nuclei.setId("1");
        nuclei.setName(name);
        nuclei.setAuthor(author);
        nuclei.setSeverity(severity);
        nuclei.setDescription("description");
        nuclei.setTags("tags");
        nuclei.setPath(replaceurl(helperPlus.getHeaderList(true, selectedMessage).get(0)));
        nuclei.setDsl("dsl");
        helperPlus.getHeaderList(true, selectedMessage).forEach(s -> {
            if (s.contains("User-Agent")) {
                nuclei.setHeader(s);
            } else {
                nuclei.setHeader("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            }
        });

        Map<String, Object> params = new HashMap<>();
        params.put("id", nuclei.getId());
        params.put("name", nuclei.getName());
        params.put("author", nuclei.getAuthor());
        params.put("severity", nuclei.getSeverity());
        params.put("description", nuclei.getDescription());
        params.put("tags", nuclei.getTags());
        params.put("path", nuclei.getPath());
        params.put("headers", nuclei.getHeader());
        params.put("dsl", dsl);
        ExpressionParser parser = new SpelExpressionParser();
        TemplateParserContext parserContext = new TemplateParserContext();
        return parser.parseExpression(Template_get, parserContext).getValue(params, String.class);

    }

    public static String NucleiPost() {
        Map<String, Object> params = new HashMap<>();
        Nuclei nuclei = new Nuclei();
        nuclei.setId("1");
        nuclei.setName(name);
        nuclei.setAuthor(author);
        nuclei.setSeverity(severity);
        nuclei.setReference("Reference");
        nuclei.setDescription("description");
        nuclei.setTags("tags");
        String raw_post = "";
        List<String> headers = analyzeRequest.getHeaders();
        for (int i = 0; i < headers.size(); i++) {
            if (!headers.get(i).contains("Host")){
                raw_post += headers.get(i) + "\n        ";
            }
        }
        int bodyOffset = analyzeRequest.getBodyOffset();
        byte[] byte_Request = selectedMessage.getRequest();

        String request = new String(byte_Request); //byte[] to String
        String body = "        "+request.substring(bodyOffset);
        raw_post += "\n"+body.replace("\r\n", "\r\n        ");
        nuclei.setRaw(raw_post);
        params.put("id", nuclei.getId());
        params.put("name", nuclei.getName());
        params.put("author", nuclei.getAuthor());
        params.put("severity", nuclei.getSeverity());
        params.put("reference", nuclei.getReference());
        params.put("description", nuclei.getDescription());
        params.put("tags", nuclei.getTags());
        params.put("raw", nuclei.getRaw());
        params.put("dsl", dsl);
        ExpressionParser parser = new SpelExpressionParser();
        TemplateParserContext parserContext = new TemplateParserContext();
        return parser.parseExpression(Template_post, parserContext).getValue(params, String.class);
    }

    public String Check() {
        if (helperPlus.getMethod(selectedMessage).equals("GET")) {
            return NucleiGet();
        } else if (helperPlus.getMethod(selectedMessage).equals("POST")) {
            return NucleiPost();
        } else {
            return "";
        }

    }
}
