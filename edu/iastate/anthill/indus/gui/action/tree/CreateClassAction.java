package edu.iastate.anthill.indus.gui.action.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.tree.DefaultTreeModel;

import com.hp.hpl.jena.ontology.Individual;
import edu.iastate.anthill.indus.owl.OntologyWithPackage;
import edu.iastate.anthill.indus.owl.node.ClassNode;
import edu.iastate.anthill.indus.owl.node.DefaultOntologyNode;
import edu.iastate.anthill.indus.owl.node.PackageNode;

public class CreateClassAction
    implements ActionListener
{
    DefaultOntologyNode theNode, parentPackage;
    OntologyWithPackage model;
    DefaultTreeModel treeModel;
    String newNodeValue;

    //parent should be a package
    public CreateClassAction(DefaultOntologyNode parent, String newNodeValue,
                             DefaultTreeModel treeModel,
                             OntologyWithPackage model)
    {
        this.model = model;
        this.treeModel = treeModel;
        this.newNodeValue = newNodeValue;
        this.parentPackage = parent;
    }

    public void actionPerformed(ActionEvent e)
    {
        actionAddNewClass();
    }

    /**
     * Create a new class
     *
     * @param parent MyNode
     * @param newNodeValue String
     * @return MyNode
     *
     * @author Jie Bao
     * @version 2004-04-22
     */
    void actionAddNewClass()
    {
        Individual pkg = PackageNode.findParentNode(parentPackage);
        // create the class and insert into the model
        // create the tree node
        theNode = new ClassNode(newNodeValue, pkg, model);
        // insert the node into tree
        treeModel.insertNodeInto(theNode, parentPackage,
                                 parentPackage.getChildCount());
    }

}
