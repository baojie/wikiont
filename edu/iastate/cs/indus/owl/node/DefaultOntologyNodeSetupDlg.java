package edu.iastate.cs.indus.owl.node;

import java.awt.Frame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.iastate.cs.utils.gui.SetupDlg;

/**
 The common behavior of the node set up dialog
 @author Jie Bao
 @since 2004-04-29
 */
abstract public class DefaultOntologyNodeSetupDlg
    extends SetupDlg
{
    protected JTextField inPackageName = new JTextField();
    protected JTextField packageName = new JTextField();
    protected JTextArea comment = new JTextArea(3, 20);

    public String getComment()
    {
        return comment.getText();
    }

    public String getNodeName()
    {
        return inPackageName.getText();
    }

    public String getNodePackage()
    {
        return packageName.getText();
    }

    public DefaultOntologyNodeSetupDlg(Frame frame, String title,
                                       String nameStr,
                                       String packageStr, String commentStr)
    {
        super(frame, title);
        addItem("Name:", inPackageName, nameStr);
        addItem("Package:", packageName, packageStr);
        addItem("Comment:", comment, commentStr);
    }
}
