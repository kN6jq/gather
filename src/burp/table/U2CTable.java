package burp.table;

import burp.*;
import burp.u2c.HttpMessageCharSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class U2CTable implements IMessageEditorTab {
    private static final String[] encoding = new String[]{"UTF-8", "gbk", "gb2312", "GB18030", "Big5", "Unicode"};
    private static IExtensionHelpers helpers;
    JButton btnNewButton;
    private final ITextEditor txtInput;
    private final JPanel panel;
    private byte[] originContent;
    private String originalCharSet;
    private final byte[] displayContent = "Nothing to show".getBytes();
    private final List<String> encodingList;
    private String currentCharSet;
    private int charSetIndex;
    private byte[] ContentToDisplay;

    public U2CTable(IMessageEditorController controller, boolean editable, IExtensionHelpers helpers, IBurpExtenderCallbacks callbacks) {
        this.encodingList = Arrays.asList(encoding);
        this.currentCharSet = this.encodingList.get(0);
        this.txtInput = callbacks.createTextEditor();
        this.panel = this.createpanel();
        this.txtInput.setEditable(editable);
        U2CTable.helpers = helpers;
    }

    public static boolean needtoconvert(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str.toLowerCase());
        return matcher.find();
    }

    public static boolean isJSON(byte[] content, boolean isRequest) {
        if (isRequest) {
            IRequestInfo requestInfo = helpers.analyzeRequest(content);
            return requestInfo.getContentType() == 4;
        } else {
            IResponseInfo responseInfo = helpers.analyzeResponse(content);
            return responseInfo.getInferredMimeType().equals("JSON");
        }
    }

    private JPanel createpanel() {
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        this.btnNewButton = new JButton("Change Encoding");
        contentPane.add(this.btnNewButton, "North");
        contentPane.add(this.txtInput.getComponent(), "Center");
        this.btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                U2CTable.this.currentCharSet = U2CTable.this.nextCharSet();
                U2CTable.this.display(U2CTable.this.currentCharSet);
            }
        });
        return contentPane;
    }

    private void display(String currentCharSet) {
        try {
            String displayString = new String(this.ContentToDisplay, this.originalCharSet);
            this.txtInput.setText(displayString.getBytes(currentCharSet));
            String text = String.format("Change Encoding: (Using %s)", currentCharSet);
            this.btnNewButton.setText(text);
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    private String nextCharSet() {
        if (this.charSetIndex < this.encodingList.size() - 1) {
            ++this.charSetIndex;
        } else {
            this.charSetIndex = 0;
        }

        return this.encodingList.get(this.charSetIndex);
    }

    @Override
    public String getTabCaption() {
        return "U2C";
    }

    @Override
    public Component getUiComponent() {
        return this.panel;
    }

    @Override
    public boolean isEnabled(byte[] bytes, boolean b) {
        return true;
    }

    @Override
    public void setMessage(byte[] content, boolean isRequest) {
        if (content == null) {
            this.txtInput.setText(this.displayContent);
        } else {
            this.originContent = content;
            this.originalCharSet = HttpMessageCharSet.getCharset(content);
            this.ContentToDisplay = getContentToDisplay(content, isRequest, this.originalCharSet);
            this.display(this.currentCharSet);
        }
    }

    private byte[] getContentToDisplay(byte[] content, boolean isRequest, String originalCharSet) {
        byte[] displayContent = content;

        try {
            String contentStr = new String(content, originalCharSet);
            if (needtoconvert(contentStr)) {
                if (isJSON(content, isRequest)) {
                    try {
                        Getter getter = new Getter(helpers);
                        byte[] body = getter.getBody(isRequest, content);
                        List<String> headers = getter.getHeaderList(isRequest, content);
                        byte[] newBody = beauty(new String(body, originalCharSet)).getBytes(originalCharSet);
                        displayContent = helpers.buildHttpMessage(headers, newBody);
                        return displayContent;
                    } catch (Exception var9) {
                    }
                }

                int i = 0;

                do {
                    contentStr = StringEscapeUtils.unescapeJava(contentStr);
                    ++i;
                } while (needtoconvert(contentStr) && i < 3);

                displayContent = contentStr.getBytes(originalCharSet);
            }
        } catch (UnsupportedEncodingException var10) {
        }

        return displayContent;
    }

    private String beauty(String inputJson) {
        Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(inputJson);
        return gson.toJson(je);
    }

    @Override
    public byte[] getMessage() {
        return this.originContent;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public byte[] getSelectedData() {
        return this.txtInput.getSelectedText();
    }
}
