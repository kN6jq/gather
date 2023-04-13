package burp.ui;

import burp.*;
import burp.model.AuthTableModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static burp.BurpExtender.mainUi;
import static burp.utils.Utils.*;

public class AuthPane extends AuthTableModel implements IMessageEditorController {
    private IMessageEditor HRequestTextEditor;
    private IMessageEditor HResponseTextEditor;


    public AuthPane() {

    }

    public AuthPane(JTabbedPane tabs) {
        JPanel scanQueue = new JPanel(new BorderLayout());

        // 主分隔面板
        JSplitPane mjSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        // 任务栏面板
        URLTable utable = new URLTable(AuthPane.this);
        JScrollPane uscrollPane = new JScrollPane(utable);

        // 请求与响应界面的分隔面板规则
        JSplitPane hjSplitPane = new JSplitPane();
        hjSplitPane.setResizeWeight(0.5);

        // 请求的面板
        JTabbedPane ltable = new JTabbedPane();
        HRequestTextEditor = cbs.createMessageEditor(AuthPane.this, false);
        ltable.addTab("Request", HRequestTextEditor.getComponent());

        // 响应的面板
        JTabbedPane rtable = new JTabbedPane();
        HResponseTextEditor = cbs.createMessageEditor(AuthPane.this, false);
        rtable.addTab("Response", HResponseTextEditor.getComponent());

        // 自定义程序UI组件
        hjSplitPane.add(ltable, "left");
        hjSplitPane.add(rtable, "right");

        mjSplitPane.add(uscrollPane, "left");
        mjSplitPane.add(hjSplitPane, "right");
        MouseListener mouseListener = new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    // 清空表格数据 并重置
                    synchronized (auth) {
                        auth.clear();
                        mainUi.getAuthPane().fireTableDataChanged();
                    }
                }
            }
        };
        utable.addMouseListener(mouseListener);

        scanQueue.add(mjSplitPane);



        tabs.addTab("auth", scanQueue);
    }



    public static void addLog(IHttpRequestResponse baseRequestResponse,String method,String url,String status,String length) {
        synchronized (auth) {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTime = sdf.format(d);

            int id = auth.size();
            auth.add(
                    new AuthPane.TablesData(
                            id,
                            method,
                            url,
                            status,
                            length,
                            startTime,
                            baseRequestResponse
                    )
            );
            mainUi.getAuthPane().fireTableRowsInserted(id, id);
        }

    }


    /**
     * 界面显示数据存储模块
     */
    public static class TablesData {
        public final int id;
        public final String method;
        public final String url;
        public final String status;
        public final String length;
        public final String startTime;

        final IHttpRequestResponse requestResponse;

        public TablesData(int id, String method, String url, String status, String length, String startTime, IHttpRequestResponse requestResponse) {
            this.id = id;
            this.method = method;
            this.url = url;
            this.status = status;
            this.length = length;
            this.startTime = startTime;
            this.requestResponse = requestResponse;

        }
    }

    /**
     * 自定义Table
     */
    private class URLTable extends JTable {
        public URLTable(TableModel tableModel) {
            super(tableModel);
        }

        @Override
        public void changeSelection(int row, int col, boolean toggle, boolean extend) {
            AuthPane.TablesData tablesData = auth.get(convertRowIndexToModel(row));

            HRequestTextEditor.setMessage(tablesData.requestResponse.getRequest(), true);
            HResponseTextEditor.setMessage(tablesData.requestResponse.getResponse(), false);
            IHttpRequestResponse currentlyDisplayedItem = tablesData.requestResponse;
            super.changeSelection(row, col, toggle, extend);
        }
    }


}
