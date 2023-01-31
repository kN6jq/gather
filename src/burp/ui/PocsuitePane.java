package burp.ui;

import burp.HelperPlus;
import burp.IContextMenuInvocation;
import burp.IHttpRequestResponse;
import burp.IRequestInfo;
import burp.entry.Pocsuite;
import org.apache.commons.io.FileUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.swing.*;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static burp.utils.Commons.replaceurl;
import static burp.utils.Utils.*;

public class PocsuitePane {
    private static final String Template_get = "#!/usr/bin/env python3\n" +
            "# -*- coding: utf-8 -*-\n" +
            "\n" +
            "from pocsuite3.api import (\n" +
            "    minimum_version_required, POCBase, register_poc, requests, logger,\n" +
            "    OptString, OrderedDict,Output,\n" +
            "    random_str,\n" +
            ")\n" +
            "\n" +
            "minimum_version_required('2.0.2')\n" +
            "\n" +
            "\n" +
            "class DemoPOC(POCBase):\n" +
            "    vulID = '0'\n" +
            "    version = '1'\n" +
            "    author = '#{[author]}'\n" +
            "    vulDate = '#{[time]}'\n" +
            "    createDate = '#{[time]}'\n" +
            "    updateDate = '#{[time]}'\n" +
            "    references = []\n" +
            "    name = '#{[name]}'\n" +
            "    appPowerLink = ''\n" +
            "    appName = ''\n" +
            "    appVersion = ''\n" +
            "    vulType = '#{[vulType]}'\n" +
            "    desc = 'Vulnerability description'\n" +
            "    samples = ['']\n" +
            "    install_requires = ['']\n" +
            "    pocDesc = 'User manual of poc'\n" +
            "    dork = {'zoomeye': ''}\n" +
            "    suricata_request = ''\n" +
            "    suricata_response = ''\n" +
            "\n" +
            "    def _options(self):\n" +
            "        o = OrderedDict()\n" +
            "        # o['user'] = OptString('', description='The username to authenticate as', require=True)\n" +
            "        # o['pwd'] = OptString('', description='The password for the username', require=True)\n" +
            "        # o['cmd'] = OptString('uname -a', description='The command to execute')\n" +
            "        return o\n" +
            "\n" +
            "    def parse_output(self, result):\n" +
            "        output = Output(self)\n" +
            "        if result:\n" +
            "            output.success(result)\n" +
            "        else:\n" +
            "            output.fail('target is not vulnerable')\n" +
            "        return output\n" +
            "\n" +
            "    def _exploit(self):\n" +
            "        result = {}\n" +
            "        # if not self._check(dork=''):\n" +
            "        #     return False\n" +
            "        #\n" +
            "        # user = self.get_option('user')\n" +
            "        # pwd = self.get_option('pwd')\n" +
            "        headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:103.0) Gecko/20100101 Firefox/103.0'}\n" +
            "        resp = requests.get(self.url + \"#{[uri]}\", headers=headers)\n" +
            "        if resp.status_code == 404 and (\"#{[hit]}\" in resp.text):\n" +
            "            result['VerifyInfo'] = {}\n" +
            "            result['VerifyInfo']['URL'] = self.url\n" +
            "            result['VerifyInfo']['Result'] = \"isExist\"\n" +
            "        return result\n" +
            "\n" +
            "    def _verify(self):\n" +
            "        pass\n" +
            "\n" +
            "    def _attack(self):\n" +
            "        # param = self.get_option('cmd')\n" +
            "        res = self._exploit()\n" +
            "        return self.parse_output(res)\n" +
            "\n" +
            "    def _shell(self):\n" +
            "        pass\n" +
            "\n" +
            "\n" +
            "register_poc(DemoPOC)\n";
    private static final String Template_post = "#!/usr/bin/env python3\n" +
            "# -*- coding: utf-8 -*-\n" +
            "\n" +
            "from pocsuite3.api import (\n" +
            "    minimum_version_required, POCBase, register_poc, requests, logger,\n" +
            "    OptString, OrderedDict,Output,\n" +
            "    random_str,\n" +
            ")\n" +
            "\n" +
            "minimum_version_required('2.0.2')\n" +
            "\n" +
            "class DemoPOC(POCBase):\n" +
            "    vulID = '0'\n" +
            "    version = '1'\n" +
            "    author = '#{[author]}'\n" +
            "    vulDate = '#{[time]}'\n" +
            "    createDate = '#{[time]}'\n" +
            "    updateDate = '#{[time]}'\n" +
            "    references = []\n" +
            "    name = '#{[name]}'\n" +
            "    appPowerLink = ''\n" +
            "    appName = ''\n" +
            "    appVersion = ''\n" +
            "    vulType = '#{[vulType]}'\n" +
            "    desc = 'Vulnerability description'\n" +
            "    samples = ['']\n" +
            "    install_requires = ['']\n" +
            "    pocDesc = 'User manual of poc'\n" +
            "    dork = {'zoomeye': ''}\n" +
            "    suricata_request = ''\n" +
            "    suricata_response = ''\n" +
            "\n" +
            "    def _options(self):\n" +
            "        o = OrderedDict()\n" +
            "        # o['user'] = OptString('', description='The username to authenticate as', require=True)\n" +
            "        # o['pwd'] = OptString('', description='The password for the username', require=True)\n" +
            "        # o['cmd'] = OptString('uname -a', description='The command to execute')\n" +
            "        return o\n" +
            "\n" +
            "    def parse_output(self, result):\n" +
            "        output = Output(self)\n" +
            "        if result:\n" +
            "            output.success(result)\n" +
            "        else:\n" +
            "            output.fail('target is not vulnerable')\n" +
            "        return output\n" +
            "\n" +
            "    def _exploit(self):\n" +
            "        '''\n" +
            "        需要手动修改请求uri,传递参数,命中及结果代码\n" +
            "        '''\n" +
            "        result = {}\n" +
            "        # if not self._check(dork=''):\n" +
            "        #     return False\n" +
            "        #\n" +
            "        # user = self.get_option('user')\n" +
            "        # pwd = self.get_option('pwd')\n" +
            "        #{[hit]}\n" +
            "\n" +
            "        return result\n" +
            "\n" +
            "\n" +
            "    def _verify(self):\n" +
            "        pass\n" +
            "\n" +
            "    def _attack(self):\n" +
            "        # param = self.get_option('cmd')\n" +
            "        res = self._exploit()\n" +
            "        return self.parse_output(res)\n" +
            "\n" +
            "    def _shell(self):\n" +
            "        pass\n" +
            "\n" +
            "\n" +
            "register_poc(DemoPOC)\n";
    private static IHttpRequestResponse selectedMessage;
    private static IRequestInfo analyzeRequest;
    private static HelperPlus helperPlus;
    private static String author;
    private static String time;
    private static String uri;
    private static String name;
    private static String vulType;
    private static String hit;
    private static byte[] request;

    public PocsuitePane(IContextMenuInvocation iContextMenuInvocation) {
        selectedMessage = iContextMenuInvocation.getSelectedMessages()[0];
        helperPlus = new HelperPlus(helpers);
        uri = helperPlus.getHeaderList(true, selectedMessage).get(0);
        analyzeRequest = helpers.analyzeRequest(selectedMessage);
        request = selectedMessage.getRequest();
        name = JOptionPane.showInputDialog(null, "请输入模板名称");
        author = JOptionPane.showInputDialog(null, "请输入作者名称");
        vulType = JOptionPane.showInputDialog(null, "请选择漏洞类型", "选择框", 1, null, vulTypes, vulTypes[0]).toString();
    }

    public String check() {
        if (helperPlus.getMethod(selectedMessage).equals("GET")) {
            return PocsuteGet();
        } else if (helperPlus.getMethod(selectedMessage).equals("POST")) {
            return PocsutePost();
        } else {
            return "";
        }
    }

    private static String PocsutePost() {
        Map<String, Object> params = new HashMap<>();
        Pocsuite pocsuite = new Pocsuite();
        pocsuite.setAuthor(author);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        pocsuite.setTime(formatter.format(date));
        pocsuite.setName(name);
        pocsuite.setVulType(vulType);
        byte contentType = analyzeRequest.getContentType();
        if (contentType == 1) {
            pocsuite.setHit(xFormData);
        } else if (contentType == 2) {
            pocsuite.setHit(file);
        }else if (contentType == 4) {
            pocsuite.setHit(json);
        }else{
            pocsuite.setHit(xFormData);
        }
        params.put("author", pocsuite.getAuthor());
        params.put("time", pocsuite.getTime());
        params.put("name", pocsuite.getName());
        params.put("vulType", pocsuite.getVulType());
        String string = new String(request, StandardCharsets.UTF_8);
        params.put("request", string);
        params.put("hit", pocsuite.getHit());
        ExpressionParser parser = new SpelExpressionParser();
        TemplateParserContext parserContext = new TemplateParserContext();
        return parser.parseExpression(Template_post, parserContext).getValue(params, String.class);
    }

    private static String PocsuteGet() {
        Map<String, Object> params = new HashMap<>();
        Pocsuite pocsuite = new Pocsuite();
        pocsuite.setAuthor(author);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        pocsuite.setTime(formatter.format(date));
        pocsuite.setName(name);
        pocsuite.setVulType(vulType);
        pocsuite.setUri(replaceurl(uri));
        pocsuite.setHit(hit);

        params.put("author", pocsuite.getAuthor());
        params.put("time", pocsuite.getTime());
        params.put("name", pocsuite.getName());
        params.put("vulType", pocsuite.getVulType());
        params.put("uri", pocsuite.getUri());
        params.put("hit", pocsuite.getHit());

        ExpressionParser parser = new SpelExpressionParser();
        TemplateParserContext parserContext = new TemplateParserContext();
        return parser.parseExpression(Template_get, parserContext).getValue(params, String.class);
    }
}
