package edu.iastate.cs.indus.gui.panel.tree;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import edu.iastate.cs.indus.gui.action.tree.CreateClassAction;
import edu.iastate.cs.indus.gui.action.tree.CreateInstanceAction;
import edu.iastate.cs.indus.gui.action.tree.CreatePackageAction;
import edu.iastate.cs.indus.gui.action.tree.CreatePropertyAction;
import edu.iastate.cs.indus.gui.action.tree.DeleteAction;
import edu.iastate.cs.indus.gui.action.tree.RenameAction;
import edu.iastate.cs.indus.gui.action.tree.ShowPropertyAction;
import edu.iastate.cs.indus.owl.node.DefaultOntologyNode;
import edu.iastate.cs.indus.owl.hierarchy.Tree;

public // popup menu on the tree
    class TreePopupMenuListener
    extends MouseAdapter
{
    Tree tree;
    DefaultTreeModel treeModel;
    PackageTreePanel fatherPanel;

    public TreePopupMenuListener(Tree tree, DefaultTreeModel model,
                                 PackageTreePanel panel)
    {
        this.tree = tree;
        this.treeModel = model;
        this.fatherPanel = panel;
    }

    public void mousePressed(MouseEvent e)
    {
        showMenu(e);
    }

    public void mouseReleased(MouseEvent e)
    {
        showMenu(e);
        updateOWLPane();
    }

    private void addMenuItem(String text, Icon icon, ActionListener listener,
                             JPopupMenu menu, String shortKey)
    {
        JMenuItem jMenuItem = (icon == null) ?
            new JMenuItem(text) : new JMenuItem(text, icon);
        jMenuItem.addActionListener(listener);
        if (shortKey != null)
        {
            jMenuItem.setAccelerator(KeyStroke.getKeyStroke(shortKey));
        }
        menu.add(jMenuItem);
    }

    private void showMenu(MouseEvent e)
    {
        TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
        if (e.isPopupTrigger() && selPath != null)
        {

            tree.setSelectionPath(selPath);

            final DefaultOntologyNode selectedNode = (DefaultOntologyNode)
                selPath.
                getLastPathComponent();

            JPopupMenu popup = new JPopupMenu();
            boolean showpopup = false;

            // 1- all things
            // node property
            addMenuItem("Property", null, new ShowPropertyAction(selectedNode),
                        popup, null);

            // 2- all editable things
            if (selectedNode.getType() > DefaultOntologyNode.ALL_INSTANCES)
            {
                // rename
                // forbit package rename for this moment
                if (selectedNode.getType() != DefaultOntologyNode.PACKAGE)
                {
                    addMenuItem("Rename", null,
                                new RenameAction(selectedNode,
                                                 fatherPanel.getOntologyModel()),
                                popup, null);
                }
                // delete
                addMenuItem("Delete", null,
                            new DeleteAction(selectedNode, treeModel,
                                             fatherPanel.getOntologyModel()),
                            popup, null);
            }

            // 3- classes and packages
            if (selectedNode.getType() == DefaultOntologyNode.ROOT |
                selectedNode.getType() == DefaultOntologyNode.PACKAGE)
            {
                // new package
                addMenuItem("Create new package", null,
                            new CreatePackageAction(selectedNode,
                    "NewPackage" + getTime(), treeModel,
                    fatherPanel.getOntologyModel()),
                            popup, null);

                // new class
                addMenuItem("Create new class", null,
                            new CreateClassAction(selectedNode,
                                                  "NewClass" + getTime(),
                                                  treeModel,
                                                  fatherPanel.getOntologyModel()),
                            popup, null);

                //new property
                addMenuItem("Create new property", null,
                            new CreatePropertyAction(selectedNode,
                    "NewProperty" + getTime(), treeModel,
                    fatherPanel.getOntologyModel()),
                            popup, null);

                addMenuItem("Create new instance", null,
                            new CreateInstanceAction(selectedNode,
                    "NewInstance" + getTime(), treeModel,
                    fatherPanel.getOntologyModel()), popup, null);
            }

            // show the menu
            showpopup = true;
            if (showpopup)
            {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }

            updateTree();
        }
    }

    /**
     * updateOWLPane
     */
    void updateOWLPane()
    {
        fatherPanel.parent.UpdateOWLPane();
    }

    /**
     * update the tree from the owl model
     *
     * @param top MyNode
     */
    private void updateTree()
    {
        tree.validate();
        tree.updateUI();
    }

    private String getTime()
    {
        SimpleDateFormat m_datefmt = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar date = Calendar.getInstance();
        return m_datefmt.format(date.getTime());
    }

} //}}}
