package edu.iastate.anthill.indus.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.iastate.anthill.indus.gui.EditorWithAction;
import edu.iastate.anthill.indus.owl.OntologyWithPackage;
import edu.iastate.anthill.indus.owl.node.OntologyDocumentNodeSetupDlg;
import edu.iastate.anthill.utils.gui.SetupDlg;

public class OntologyNewAction
    implements ActionListener
{
    EditorWithAction parent;

    public OntologyNewAction(EditorWithAction parent)
    {
        this.parent = parent;
    }

    public void actionPerformed(ActionEvent e)
    {
        // close current document
        OntologyCloseAction a = new OntologyCloseAction(parent);
        a.doClose();

        // create new
        doNew();
        parent.UpdateTitle("Untitled");
    }

    /**
     * 2004-04-25
     */
    void doNew()
    {
        // get ontology parameters
        OntologyDocumentNodeSetupDlg dlg = new OntologyDocumentNodeSetupDlg(
            null, "New Ontology",
            "http://somewhere.com/ont#", "Foo", "", true);
        dlg.show();

        if (dlg.getAction() == SetupDlg.OK)
        {
            // create the ontology model
            parent.newOntology(dlg.getUri(), dlg.getPrefix());

            if (dlg.getComment() != "")
            {
                parent.getOntologyModel().getResource(dlg.getUri()).
                    setComment(dlg.getComment(), null);

            }
            // create GUI
            parent.createPanels();
            // menu
            parent.mainMenu = parent.createMenuBar();
            parent.mainFrame.setJMenuBar(parent.mainMenu);
            parent.mainFrame.validate();
        }
    }

}
