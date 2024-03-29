package edu.iastate.anthill.indus.gui.action;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import edu.iastate.anthill.indus.gui.EditorWithAction;

/**
 * close the edting ontology
 *
 * @author Jie Bao
 * @since 2004-05-01
 */

public class OntologyCloseAction
    implements ActionListener
{
    EditorWithAction parent;

    public OntologyCloseAction(EditorWithAction parent)
    {
        this.parent = parent;
    }

    public void actionPerformed(ActionEvent e)
    {
        doClose();
        parent.UpdateTitle("");
    }

    // 2004-05-01
    void doClose()
    {
        // do something to check if the ontology is saved
        if (parent.ifNeedSave())
        {
            int ret = JOptionPane.showConfirmDialog(parent.mainFrame,
                "Document modified, do you want to save it?", "Save",
                JOptionPane.YES_NO_CANCEL_OPTION);

            if (ret == JOptionPane.YES_OPTION)
            {
                OntologySaveAction action = new OntologySaveAction(parent);
                action.doSave();
            }
            else if (ret == JOptionPane.CANCEL_OPTION)
            {
                return;
            }
        }
        // close the opened one, rebuild interface.
        parent.clearPanels();
        // menu
        parent.mainMenu = parent.createBasicMenu();
        parent.mainFrame.setJMenuBar(parent.mainMenu);
        parent.mainFrame.validate();

        //set ontology model to null
        parent.closeOntology();

    }

}
