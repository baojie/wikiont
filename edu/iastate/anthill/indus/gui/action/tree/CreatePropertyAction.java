package edu.iastate.anthill.indus.gui.action.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.tree.DefaultTreeModel;

import com.hp.hpl.jena.ontology.Individual;
import edu.iastate.anthill.indus.owl.OntologyWithPackage;
import edu.iastate.anthill.indus.owl.node.DefaultOntologyNode;
import edu.iastate.anthill.indus.owl.node.PackageNode;
import edu.iastate.anthill.indus.owl.node.PropertyNode;

public class CreatePropertyAction
    implements ActionListener
{
    DefaultOntologyNode theNode, parent;
    OntologyWithPackage model;
    DefaultTreeModel treeModel;
    String newNodeValue;

    //parent should be a package
    public CreatePropertyAction(DefaultOntologyNode parent, String newNodeValue,
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
        actionAddNewProperty();
    }

    /**
     * Create a new property
     *
     * @param parent MyNode
     * @param newNodeValue String
     * @return MyNode
     *
     * @author Jie Bao
     * @version 2004-04-22
     */
    void actionAddNewProperty()
    {
        Individual pkg = PackageNode.findParentNode(parent);

        // create the property and insert into the model
        // create the tree node
        theNode = new PropertyNode(newNodeValue, pkg, model);
        // insert the node into tree
        treeModel.insertNodeInto(theNode, parent, parent.getChildCount());
    }

}
