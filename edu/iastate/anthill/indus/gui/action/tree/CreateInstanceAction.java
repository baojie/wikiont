package edu.iastate.anthill.indus.gui.action.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import edu.iastate.anthill.indus.owl.OntologyWithPackage;
import edu.iastate.anthill.indus.owl.node.DefaultOntologyNode;
import edu.iastate.anthill.indus.owl.node.InstanceNode;
import edu.iastate.anthill.indus.owl.node.PackageNode;

public class CreateInstanceAction
    implements ActionListener
{
    DefaultOntologyNode theNode, parent;
    OntologyWithPackage model;
    DefaultTreeModel treeModel;
    String newNodeValue;

    //parent should be a package
    public CreateInstanceAction(DefaultOntologyNode parent, String newNodeValue,
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
        actionAddNewInstance();
    }

    // Create a new  instance
    void actionAddNewInstance()
    {
        Individual pkg = PackageNode.findParentNode(parent);

        String className = JOptionPane.showInputDialog("Give the class name");
        OntClass theClass = model.getClass(className);

        if (theClass == null)
        {
            JOptionPane.showMessageDialog(null, "Class not find");
        }
        else
        {
            theNode = new InstanceNode(newNodeValue, pkg, model, theClass);
            treeModel.insertNodeInto(theNode, parent, parent.getChildCount());
        }

    }

}
