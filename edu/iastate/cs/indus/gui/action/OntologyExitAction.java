package edu.iastate.cs.indus.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.iastate.cs.indus.gui.EditorWithAction;

public class OntologyExitAction
    implements ActionListener
{
    EditorWithAction parent;

    public OntologyExitAction(EditorWithAction parent)
    {
        this.parent = parent;
    }

    public void actionPerformed(ActionEvent e)
    {
        doExit();
    }

    public void doExit()
    {
        OntologyCloseAction action = new OntologyCloseAction(parent);
        action.doClose();

        parent.mainFrame.dispose();
        System.exit(0);
    }

}
