package edu.iastate.anthill.indus.gui.panel.tree;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreeSelectionModel;

import edu.iastate.anthill.indus.gui.EditorGUI;
import edu.iastate.anthill.indus.owl.hierarchy.SubClassHierarchy;
import edu.iastate.anthill.indus.owl.hierarchy.Tree;

/**
 * @author Jie Bao
 * @since 1.0
 */

public class SubclassTreePanel
    extends JPanel
{
    EditorGUI parent;

//    private DefaultOntologyNode root;
    private Tree tree;

    public SubclassTreePanel(EditorGUI parent)
    {
        this.parent = parent;
        try
        {
            jbInit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    //create the initial tree
    public void createTree()
    {
        SubClassHierarchy h = new SubClassHierarchy(parent.getOntologyModel());
        tree = h.createFromModel(parent.getOntologyModel());
        System.out.println(tree);
    }

    void jbInit() throws Exception
    {
        //setup the tree
        //initializes tree
        createTree();
        // set the tree attributes
        tree.setEditable(false);
        tree.setShowsRootHandles(true);
        tree.setRootVisible(true);
        tree.setCellRenderer(new TreeCellRendererEx());

        // creates tree selection event listener
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.
                                                  SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(new javax.swing.event.
                                      TreeSelectionListener()
        {
            public void valueChanged(TreeSelectionEvent e)
            {
                //treeSelectedListener(e);
            }
        });
        //tree.addMouseListener(new TreePopupMenuListener(tree, model,  this));

        this.setLayout(new BorderLayout());
        // create a scroll pane
        JScrollPane treeView = new JScrollPane(tree);
        add(treeView, BorderLayout.CENTER);
    }


}
