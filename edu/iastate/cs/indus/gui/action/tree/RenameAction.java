package edu.iastate.cs.indus.gui.action.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import edu.iastate.cs.indus.owl.OntologyWithPackage;
import edu.iastate.cs.indus.owl.node.DefaultOntologyNode;

public class RenameAction
    implements ActionListener
{
    DefaultOntologyNode theNode;
    OntologyWithPackage model;

    public RenameAction(DefaultOntologyNode theNode, OntologyWithPackage model)
    {
        this.theNode = theNode;
        this.model = model;
    }

    /**
     * rename a node
     *
     * actionPerformed
     *
     * @param e ActionEvent
     */
    public void actionPerformed(ActionEvent e)
    {
        String oldName = theNode.getInPackageName();
        String newName = JOptionPane.showInputDialog(
            "Give a new name (in package)",
            oldName);
        theNode.rename(newName);
    }

}
