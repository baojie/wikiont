package edu.iastate.cs.indus.gui.action.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.tree.DefaultTreeModel;

import edu.iastate.cs.indus.owl.OntologyWithPackage;
import edu.iastate.cs.indus.owl.node.DefaultOntologyNode;

public class DeleteAction
    implements ActionListener
{
    DefaultOntologyNode theNode;
    OntologyWithPackage model;
    DefaultTreeModel treeModel;

    public DeleteAction(DefaultOntologyNode theNode, DefaultTreeModel treeModel,
                        OntologyWithPackage model)
    {
        this.theNode = theNode;
        this.model = model;
        this.treeModel = treeModel;
    }

    /**
     * actionPerformed
     *
     * @param e ActionEvent
     */
    public void actionPerformed(ActionEvent e)
    {
        actionDelete();
    }

    // delete the node
    // 2004-04-22
    void actionDelete()
    {
        if (theNode.getParent() != null)
        {
            treeModel.removeNodeFromParent(theNode);
        }
        model.deleteResource(theNode.getJenaLocalName());
    }
}
