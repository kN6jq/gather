package burp.menu;

import burp.IBurpExtenderCallbacks;
import burp.IContextMenuFactory;
import burp.IContextMenuInvocation;
import burp.IHttpRequestResponse;
import burp.entry.CustomLineEntry;
import burp.ui.FastjsonPane;
import burp.ui.NucleiPane;
import burp.utils.RobotInput;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static burp.utils.Commons.RequestToFile;
import static burp.utils.Utils.*;


public class ToolsMenu implements IContextMenuFactory {
    private IHttpRequestResponse baseRequestResponse;

    @Override
    public List<JMenuItem> createMenuItems(IContextMenuInvocation iContextMenuInvocation) {
        this.baseRequestResponse = iContextMenuInvocation.getSelectedMessages()[0];
        List<JMenuItem> menus = new ArrayList<>();
        IHttpRequestResponse[] messages = iContextMenuInvocation.getSelectedMessages();
        if (iContextMenuInvocation.getToolFlag() == IBurpExtenderCallbacks.TOOL_REPEATER || iContextMenuInvocation.getToolFlag() == IBurpExtenderCallbacks.TOOL_PROXY || iContextMenuInvocation.getToolFlag() == IBurpExtenderCallbacks.TOOL_EXTENDER) {
            JMenu tools = new JMenu("tools");
            for (CustomLineEntry c : loadConn.getCustom())
                if (!c.getName().equals("") && !c.getTemplate().equals("")) {
                    tools.add(new JMenuItem(new AbstractAction(c.getName()) {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Runnable DirsearchRunner = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        RobotInput ri = new RobotInput();
                                        if (messages != null) {
                                            RobotInput.startCmdConsole();//尽早启动减少出错概率
                                            String cmd = c.getTemplate();
                                            if (cmd.contains("{url}")) {
                                                String url = helpers.analyzeRequest(baseRequestResponse).getUrl().toString();
                                                cmd = cmd.replace("{url}", url);
                                            } else if (cmd.contains("{request}")) {
                                                IHttpRequestResponse message = baseRequestResponse;
                                                String requestFilePath = RequestToFile(message);
                                                cmd = cmd.replace("{request}", requestFilePath);
                                            } else if (cmd.contains("{host}")) {
                                                IHttpRequestResponse message = baseRequestResponse;
                                                String host = message.getHttpService().getHost();
                                                cmd = cmd.replace("{host}", host);
                                            }
                                            ri.inputString(cmd);
                                        }
                                    } catch (Exception e1) {
                                        e1.printStackTrace(stderr);
                                    }
                                }
                            };
                            new Thread(DirsearchRunner).start();
                        }
                    }));
                }
            menus.add(tools);

            // fastjson窗口的菜单
            JMenu fastjson_exp = new JMenu("fastjson");
            JMenuItem FastjsonEcho = new JMenuItem("Send to FastjsonEcho");
            fastjson_exp.add(FastjsonEcho);
            JMenuItem FastjsonInject = new JMenuItem("Send to FastjsonInject");
            fastjson_exp.add(FastjsonInject);
            JMenuItem FastjsonJndi = new JMenuItem("Send to FastjsonJndi");
            fastjson_exp.add(FastjsonJndi);
            FastjsonEcho.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 添加任务到面板中等待检测
                    int taskid = new FastjsonPane().initcheck(messages, baseRequestResponse);
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            try {
                                new FastjsonPane().checkEchoVul(baseRequestResponse, taskid);
                            } catch (Exception ex) {
                                stdout.println(ex.getMessage());
                            }
                        }
                    });
                    thread.start();
                }
            });
            FastjsonInject.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 添加任务到面板中等待检测
                    int taskid = new FastjsonPane().initcheck(messages, baseRequestResponse);
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            try {
                                new FastjsonPane().InjectVul(baseRequestResponse, taskid);
                            } catch (Exception ex) {
                                stdout.println(ex.getMessage());
                            }
                        }
                    });
                    thread.start();
                }
            });
            FastjsonJndi.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 添加任务到面板中等待检测
                    int taskid = new FastjsonPane().initcheck(messages, baseRequestResponse);
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            try {
                                new FastjsonPane().checkdnsVul(baseRequestResponse, taskid);
                            } catch (Exception ex) {
                                stdout.println(ex.getMessage());
                            }
                        }
                    });
                    thread.start();
                }
            });
            menus.add(fastjson_exp);

            // nuclei窗口的菜单
            JMenuItem nuclei_exp = new JMenuItem("nuclei");
            nuclei_exp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            try {
                                NucleiPane nucleiPane = new NucleiPane(iContextMenuInvocation);
                                String s = nucleiPane.Check();
                                // java 复制字符串到粘贴板
                                StringSelection stsel = new StringSelection(s);
                                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stsel, stsel);

                            } catch (Exception ex) {
                                stdout.println(ex.getMessage());
                            }
                        }
                    });
                    thread.start();
                }
            });
            menus.add(nuclei_exp);

        }
        return menus;
    }
}
