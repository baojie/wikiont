package edu.iastate.cs.indus.gui.panel.tree;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.iastate.cs.indus.gui.EditorGUI;
import edu.iastate.cs.indus.owl.OntologyWithPackage;
import edu.iastate.cs.indus.owl.hierarchy.Tree;
import edu.iastate.cs.indus.owl.node.DefaultOntologyNode;
import edu.iastate.cs.indus.owl.node.OntologyDocumentNode;

/**
 * package view of the ontology
 *
 * @author Jie Bao
 * @version 1.0
 */

public class PackageTreePanel
    extends JPanel
{
    EditorGUI parent;

    private DefaultOntologyNode root;
    private Tree tree;
    private DefaultTreeModel model = null;

    public PackageTreePanel(EditorGUI parent)
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
    public void createTree(DefaultOntologyNode root)
    {
        if (root == null)
        {
            root = new OntologyDocumentNode(parent.getOntologyModel().getOntURI(),
                                            parent.getOntologyModel());
        }
        root.removeAllChildren();

        /*        OntologyNode node = new OntologyNode("Classes",
         OntologyNode.ALL_CLASSES, null);
                root.add(node);

         node = new OntologyNode("Properties", OntologyNode.ALL_PROPERTIES, null);
                root.add(node);

         node = new OntologyNode("Instances", OntologyNode.ALL_INSTANCES, null);
                root.add(node);
         */
        model = new DefaultTreeModel(root);
        tree = new Tree(root);
    }

    void jbInit() throws Exception
    {
        //setup the tree
        //initializes tree
        createTree(root);
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
                treeSelectedListener(e);
            }
        });
        tree.addMouseListener(new TreePopupMenuListener(tree, model,
            this));

        this.setLayout(new BorderLayout());
        // create a scroll pane
        JScrollPane treeView = new JScrollPane(tree);
        add(treeView, BorderLayout.CENTER);
    }

    /**
     * tree selection event
     *
     * @param e TreeSelectionEvent
     */
    void treeSelectedListener(TreeSelectionEvent e)
    {
        DefaultOntologyNode node = (DefaultOntologyNode)
            tree.getLastSelectedPathComponent();

        TreePath pathnode = (TreePath) tree.getLeadSelectionPath();
        if (tree.isVisible(pathnode))
        {
            parent.UpdateBrowserPane(node);

        }
    }

    public OntologyWithPackage getOntologyModel()
    {
        return parent.getOntologyModel();
    }
}
