package edu.iastate.cs.indus.gui.action.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.iastate.cs.indus.owl.node.DefaultOntologyNode;

public class ShowPropertyAction
    implements ActionListener
{

    DefaultOntologyNode theNode;

    public ShowPropertyAction(DefaultOntologyNode theNode)
    {
        this.theNode = theNode;
    }

    /**
     * actionPerformed
     *
     * @param e ActionEvent
     */
    public void actionPerformed(ActionEvent e)
    {
        theNode.propertySetup();
    }

}
