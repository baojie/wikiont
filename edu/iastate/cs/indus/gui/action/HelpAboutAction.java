package edu.iastate.cs.indus.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.iastate.cs.indus.gui.EditorWithAction;
import edu.iastate.cs.indus.gui.dlg.AboutBoxDialog;

public class HelpAboutAction
    implements ActionListener
{
    EditorWithAction parent;

    public HelpAboutAction(EditorWithAction parent)
    {
        this.parent = parent;
    }

    public void actionPerformed(ActionEvent e)
    {
        AboutBoxDialog dlg = new AboutBoxDialog();
        dlg.showAboutBox();
    }

}
