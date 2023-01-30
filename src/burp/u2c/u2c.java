package burp.u2c;

import burp.IMessageEditorController;
import burp.IMessageEditorTab;
import burp.IMessageEditorTabFactory;
import burp.table.U2CTable;

import static burp.utils.Utils.cbs;
import static burp.utils.Utils.helpers;

public class u2c implements IMessageEditorTabFactory {
    @Override
    public IMessageEditorTab createNewInstance(IMessageEditorController iMessageEditorController, boolean b) {
        return new U2CTable(iMessageEditorController, false, helpers, cbs);
    }
}
