package edu.iastate.cs.indus.gui.action.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.tree.DefaultTreeModel;

import com.hp.hpl.jena.ontology.Individual;

import edu.iastate.cs.indus.owl.OntologyWithPackage;
import edu.iastate.cs.indus.owl.node.DefaultOntologyNode;
import edu.iastate.cs.indus.owl.node.PackageNode;

//2004-04-27
public class CreatePackageAction
    implements ActionListener
{

    DefaultOntologyNode theNode, parent;
    OntologyWithPackage model;
    DefaultTreeModel treeModel;
    String newNodeValue;

    public CreatePackageAction(DefaultOntologyNode parent, String newNodeValue,
                               DefaultTreeModel treeModel,
                               OntologyWithPackage model)
    {
        this.model = model;
        this.treeModel = treeModel;
        this.newNodeValue = newNodeValue;
        this.parent = parent;
    }

    public void actionPerformed(ActionEvent e)
    {
        actionAddNew();
    }

    void actionAddNew()
    {
        Individual pkg = PackageNode.findParentNode(parent);

        theNode = new PackageNode(newNodeValue, pkg, model);
        treeModel.insertNodeInto(theNode, parent, parent.getChildCount());
    }
}
