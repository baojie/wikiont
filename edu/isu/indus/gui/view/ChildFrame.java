/*
 DefaultView.java
 :tabSize=4:indentSize=4:noTabs=true:
 :folding=explicit:collapseFolds=1:
 jsXe is the Java Simple XML Editor
     jsXe is a gui application that can edit an XML document and create a tree view.
 The user can then edit this tree and the content in the tree and save the
 document.
 This file contains all the code for the default panel shows the xml document.
 Since you don't want a panel for every document each with its own splitpane
 dimensions this is a singleton. It may not be forever as jsXe may one day
 may be multi-threaded and have multiple TabbedViews.
 This file written by Ian Lewis (IanLewis@member.fsf.org)
 Copyright (C) 2002 Ian Lewis
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 Optionally, you may find a copy of the GNU General Public License
 from http://www.fsf.org/copyleft/gpl.txt
 */

package edu.isu.indus.gui.view;

//{{{ imports
/*
 All classes are listed explicitly so
 it is easy to see which package it
 belongs to.
 */

//{{{ jsXe classes
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import edu.iastate.cs.utils.Debug;
import edu.isu.indus.action.ViewRefreshAction;
import edu.isu.indus.gui.MainFrame;
import edu.isu.indus.gui.TreeCellRendererEx;
import edu.isu.indus.gui.dlg.OptionsPanel;
import edu.isu.indus.xml.XMLDocument;
import edu.isu.indus.xml.XNode;
import edu.isu.indus.xml.XTree;

//}}}
//}}}

public class ChildFrame
    extends DocumentView {

    protected ChildFrame() { //{{{

        setLayout(new BorderLayout());

// 1 - setup the left pane
        JPanel jLeftUpper = new JPanel();

        // 1.1 - setup the lef-upper part

        // 1.1.1 - setup the tree
        try {
            tree = new XTree();
        }
        catch (ParserConfigurationException ex) {
        }
        JScrollPane treeView = new JScrollPane(tree);

        // set the tree attributes
        tree.addTreeSelectionListener(new DefaultTreeSelectionListener(this));
        tree.setCellRenderer(new TreeCellRendererEx());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.
                                                  SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.setEditable(false);
        /* Make tree ask for the height of each row. */
        tree.setRowHeight( -1);
        ToolTipManager.sharedInstance().registerComponent(tree);

        // 1.1.2 setup option pane
        JPanel jLeftUpperOption = constructOptionsPanel();

        // 1.1.3 Create left upper  pane
        jLeftUpper.setLayout(new BorderLayout());
        jLeftUpper.add("Center", treeView);
        jLeftUpper.add("South", jLeftUpperOption);

        // 1.2 - setup the left-lower part
        JPanel jLeftLower = new JPanel();

        // 1.2.1 create a table model
        JScrollPane attrView = new JScrollPane(attributesTable);

        // 1.2.2 create the value editor
//        JScrollPane valueView = new JScrollPane(valueText);
        JScrollPane valueView = new JScrollPane(valuePane);
        valuePane.setEditable(true);

        // 1.2.3 Create left lower  pane
        jLeftLower.setLayout(new GridLayout(2, 1));
        jLeftLower.add(valueView);
        jLeftLower.add(attrView);

        // 1.3 Create and set up the left splitpanes
        leftPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jLeftUpper,
                                  jLeftLower);
        leftPane.setContinuousLayout(false);
        leftPane.setOneTouchExpandable(true);
        leftPane.setDividerLocation(200);

// 2- setup the right pane - Create html editor pane
        JScrollPane rightPane = new JScrollPane(sourceXML);

// 3- setup the whole pane
        wholePane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                   leftPane, rightPane);
        wholePane.setContinuousLayout(false);
        wholePane.setOneTouchExpandable(true);

        //set to arbitrary size.
        wholePane.setDividerLocation(200);

        add(wholePane, BorderLayout.CENTER);

        //}}}

        tree.addMouseListener(new TreePopupListener());
        attributesTable.addMouseListener(new TablePopupListener());

    } //}}}

    /** Constructs a JPanel containing check boxes for the different
     * options that tree supports.
     *
     * @return panel
     *  */

    private JPanel constructOptionsPanel() {
        JPanel jRetPanel;
        JPanel jBorderPane;

        JCheckBox aCheckbox;
        JLabel label;

        jRetPanel = new JPanel(false);
        jBorderPane = new JPanel(false);

        jBorderPane.setLayout(new BorderLayout());
        jRetPanel.setLayout(new FlowLayout());

        label = new JLabel("Show Mode:");
        jRetPanel.add(label);

        aCheckbox = new JCheckBox("show handles");
        aCheckbox.setSelected(tree.getShowsRootHandles());
        aCheckbox.addChangeListener(new ShowHandlesChangeListener());
        jRetPanel.add(aCheckbox);

        aCheckbox = new JCheckBox("show root");
        aCheckbox.setSelected(tree.isRootVisible());
        aCheckbox.addChangeListener(new ShowRootChangeListener());
        jRetPanel.add(aCheckbox);

        aCheckbox = new JCheckBox("editable");
        aCheckbox.setSelected(tree.isEditable());
        aCheckbox.addChangeListener(new TreeEditableChangeListener());
        aCheckbox.setToolTipText("Triple click to edit");
        jRetPanel.add(aCheckbox);

        jBorderPane.add(jRetPanel, BorderLayout.CENTER);

        /* Create a set of radio buttons that dictate what selection should
           be allowed in the tree. */
        ButtonGroup group = new ButtonGroup();
        JPanel buttonPane = new JPanel(false);
        JRadioButton button;

        buttonPane.setLayout(new FlowLayout());

        label = new JLabel("Selection Mode:");
        buttonPane.add(label);

        button = new JRadioButton("Single");
        button.addActionListener(new AbstractAction() {
            public boolean isEnabled() {
                return true;
            }

            public void actionPerformed(ActionEvent e) {
                tree.getSelectionModel().setSelectionMode
                    (TreeSelectionModel.SINGLE_TREE_SELECTION);
            }
        });
        group.add(button);
        buttonPane.add(button);
        button = new JRadioButton("Contiguous");
        button.addActionListener(new AbstractAction() {
            public boolean isEnabled() {
                return true;
            }

            public void actionPerformed(ActionEvent e) {
                tree.getSelectionModel().setSelectionMode
                    (TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
            }
        });
        group.add(button);
        buttonPane.add(button);
        button = new JRadioButton("Discontiguous");
        button.addActionListener(new AbstractAction() {
            public boolean isEnabled() {
                return true;
            }

            public void actionPerformed(ActionEvent e) {
                tree.getSelectionModel().setSelectionMode
                    (TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
            }
        });
        button.setSelected(true);
        group.add(button);
        buttonPane.add(button);

        jBorderPane.add(buttonPane, BorderLayout.SOUTH);

        return jBorderPane;
    }

    public void setDocument(MainFrame view, XMLDocument document) throws
        IOException { //{{{

        if (mainWin == null) {
            mainWin = view;
        }

        try {
            document.validate();
        }
        catch (Exception e) {
            String errormsg = "Could not validate XML Document.\n" +
                "Default View requires XML documents to be well-formed.\n\n" +
                e.toString();
            Debug.trace(e.toString());
            throw new IOException(errormsg);
        }

        close(view);

        XNode adapter = new XNode(document.getDocument());

        ChildFrameTreeModel treeModel = new ChildFrameTreeModel(this,
            document);
        ChildFrameTableModel tableModel = new ChildFrameTableModel(this,
            adapter);
        ChildFrameValueEditor styledDoc = new ChildFrameValueEditor(adapter);
        //This adapter may have the listener already.
        //addTreeModelListener does not add the listener
        //again if it is already added.
        tree.setModel(treeModel);
        attributesTable.setModel(tableModel);
        treeModel.addTreeModelListener(treeListener);
        tableModel.addTableModelListener(tableListener);

        valuePane.setDocument(styledDoc);
        styledDoc.addDocumentListener(docListener);
        //Clear the right hand pane of previous values.
        valuePane.setText("");

        // Jie Bao , setup the source view
        sourceXML.setDocument(view, document);

        //get the splitpane layout options
        boolean layout = Boolean.valueOf(document.getProperty(viewname +
            ".continuous.layout", "false")).booleanValue();
        leftPane.setContinuousLayout(layout);
        wholePane.setContinuousLayout(layout);
        leftPane.setDividerLocation(Integer.valueOf(document.getProperty(
            viewname + ".splitpane.vert.loc", "200")).intValue());
        wholePane.setDividerLocation(Integer.valueOf(document.getProperty(
            viewname + ".splitpane.horiz.loc", "200")).intValue());

        //update the UI so that the components
        //are redrawn.
        attributesTable.updateUI();
        tree.updateUI();
        updateUI();
        currentDoc = document;
    } //}}}

    public JMenu[] getMenus() { //{{{
        // Edit Menu doesn't work yet.
        JMenu[] menus = new JMenu[2];
        // //{{{ Create Edit Menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem menuItem = new JMenuItem("Undo");
        menuItem.addActionListener(new EditUndoAction());
        editMenu.add(menuItem);
        menuItem = new JMenuItem("Redo");
        menuItem.addActionListener(new EditRedoAction());
        editMenu.add(menuItem);
        editMenu.addSeparator();
        menuItem = new JMenuItem("Cut");
        menuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl X"));
        menuItem.addActionListener(new EditCutAction());
        editMenu.add(menuItem);
        menuItem = new JMenuItem("Copy");
        menuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        menuItem.addActionListener(new EditCopyAction());
        editMenu.add(menuItem);
        menuItem = new JMenuItem("Paste");
        menuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl V"));
        menuItem.addActionListener(new EditPasteAction());
        editMenu.add(menuItem);

        menus[0] = editMenu;

        //{{{ Add View Menu
        JMenu viewMenu = new JMenu("View");
        menuItem = new JMenuItem(new ViewRefreshAction(this));
        menuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl T"));
        viewMenu.add(menuItem);
        menuItem = new JMenuItem("Refresh Source");
        menuItem.addActionListener(new ViewRefreshSourceAction());
        menuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
        viewMenu.add(menuItem);
        menus[1] = viewMenu;
        //}}}

        return menus;

    } //}}}

    public OptionsPanel getOptionsPanel() { //{{{
        return new DefaultViewOptionsPanel();
    } //}}}

    public XMLDocument getXMLDocument() { //{{{
        return currentDoc;
    } //}}}

    public void close(MainFrame view) { //{{{
        if (currentDoc != null) {
            String vert = Integer.toString(leftPane.getDividerLocation());
            String horiz = Integer.toString(wholePane.getDividerLocation());

            currentDoc.setProperty(viewname + ".splitpane.vert.loc", vert);
            currentDoc.setProperty(viewname + ".splitpane.horiz.loc", horiz);
        }
    } //}}}

    //{{{ Private Members

    private boolean canEditInJTree(XNode node) { //{{{
        return (node.getNodeType() == Node.ELEMENT_NODE);
    } //}}}

    private boolean canEditInJEditorPane(XNode node) { //{{{
        return (node.getNodeValue() != null);
    } //}}}

    private class DefaultViewOptionsPanel
        extends OptionsPanel { //{{{

        public DefaultViewOptionsPanel() { //{{{

            GridBagLayout layout = new GridBagLayout();
            GridBagConstraints constraints = new GridBagConstraints();

            setLayout(layout);

            int gridY = 0;

            boolean showCommentNodes = Boolean.valueOf(currentDoc.getProperty(
                viewname + ".show.comment.nodes", "false")).booleanValue();
            boolean showEmptyNodes = Boolean.valueOf(currentDoc.getProperty(
                viewname + ".show.empty.nodes", "false")).booleanValue();
            boolean continuousLayout = Boolean.valueOf(currentDoc.getProperty(
                viewname + ".continuous.layout", "false")).booleanValue();

            showCommentsCheckBox = new JCheckBox("Show comment nodes",
                                                 showCommentNodes);
            showEmptyNodesCheckBox = new JCheckBox("Show whitespace-only nodes",
                showEmptyNodes);
            ContinuousLayoutCheckBox = new JCheckBox(
                "Continuous layout for split-panes", continuousLayout);

            constraints.gridy = gridY++;
            constraints.gridx = 1;
            constraints.gridheight = 1;
            constraints.gridwidth = 1;
            constraints.weightx = 1.0f;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.insets = new Insets(1, 0, 1, 0);

            layout.setConstraints(showCommentsCheckBox, constraints);
            add(showCommentsCheckBox);

            constraints.gridy = gridY++;
            constraints.gridx = 1;
            constraints.gridheight = 1;
            constraints.gridwidth = 1;
            constraints.weightx = 1.0f;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.insets = new Insets(1, 0, 1, 0);

            layout.setConstraints(showEmptyNodesCheckBox, constraints);
            add(showEmptyNodesCheckBox);

            constraints.gridy = gridY++;
            constraints.gridx = 1;
            constraints.gridheight = 1;
            constraints.gridwidth = 1;
            constraints.weightx = 1.0f;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.insets = new Insets(1, 0, 1, 0);

            layout.setConstraints(ContinuousLayoutCheckBox, constraints);
            add(ContinuousLayoutCheckBox);
        } //}}}

        public void saveOptions() { //{{{
            currentDoc.setProperty(viewname + ".show.comment.nodes",
                                   (new Boolean(showCommentsCheckBox.isSelected())).
                                   toString());
            currentDoc.setProperty(viewname + ".show.empty.nodes",
                                   (new Boolean(showEmptyNodesCheckBox.
                                                isSelected())).toString());

            boolean layout = ContinuousLayoutCheckBox.isSelected();
            currentDoc.setProperty(viewname + ".continuous.layout",
                                   (new Boolean(layout)).toString());
            leftPane.setContinuousLayout(layout);
            wholePane.setContinuousLayout(layout);
            tree.updateUI();
        } //}}}

        public String getTitle() { //{{{
            return "Tree View Options";
        } //}}}

        private JCheckBox showCommentsCheckBox;
        private JCheckBox showEmptyNodesCheckBox;
        private JCheckBox ContinuousLayoutCheckBox;

    } //}}}

    private class DefaultTreeSelectionListener
        implements TreeSelectionListener { //{{{

        DefaultTreeSelectionListener(Component p) {
            parent = p;
        }

        public void valueChanged(TreeSelectionEvent e) {
            TreePath selPath = e.getPath();
            XNode selectedNode = (XNode) selPath.
                getLastPathComponent();
            if (selectedNode != null) {

                //if the selected node can be edited in either the tree
                //or the text pane
                tree.setEditable(canEditInJTree(selectedNode));
                valuePane.setEditable(canEditInJEditorPane(selectedNode));

                //update the attributes table with the current info.
                ChildFrameTableModel tableModel = new ChildFrameTableModel(
                    parent, selectedNode);
                attributesTable.setModel(tableModel);
                tableModel.addTableModelListener(tableListener);
                attributesTable.updateUI();

                //update the text pane with the current info
                ChildFrameValueEditor styledDoc = new ChildFrameValueEditor(
                    selectedNode);
                valuePane.setDocument(styledDoc);
                styledDoc.addDocumentListener(docListener);
                valuePane.updateUI();

            }
            else {
                valuePane.setDocument(null);
            }
        }

        private Component parent;

    } //}}}

    private class TreePopupListener
        extends MouseAdapter { //{{{
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
            if (e.isPopupTrigger() && selPath != null) {

                tree.setSelectionPath(selPath);

                XNode selectedNode = (XNode) selPath.
                    getLastPathComponent();
                JMenuItem popupMenuItem;
                JPopupMenu popup = new JPopupMenu();
                boolean showpopup = false;

                if (selectedNode.getNodeType() == Node.ELEMENT_NODE) {
                    popupMenuItem = new JMenuItem("Add Element Node");
                    popupMenuItem.addActionListener(new AddElementNodeAction());
                    popup.add(popupMenuItem);
                    popupMenuItem = new JMenuItem("Add Text Node");
                    popupMenuItem.addActionListener(new AddTextNodeAction());
                    popup.add(popupMenuItem);
                    showpopup = true;
                }
                //if the node is not the document or the document root.
                if (selectedNode.getNodeType() != Node.DOCUMENT_NODE &&
                    selectedNode.getParentNode().getNodeType() !=
                    Node.DOCUMENT_NODE) {
                    popupMenuItem = new JMenuItem("Remove Node");
                    popupMenuItem.addActionListener(new RemoveNodeAction());
                    popup.add(popupMenuItem);
                    showpopup = true;
                }
                if (showpopup) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    } //}}}

    private class AddElementNodeAction
        implements ActionListener { //{{{

        public void actionPerformed(ActionEvent e) {
            try {
                TreePath selPath = tree.getLeadSelectionPath();
                if (selPath != null) {
                    XNode selectedNode = (XNode) selPath.
                        getLastPathComponent();
                    XNode newNode = selectedNode.addNode("New_Element",
                        "", Node.ELEMENT_NODE);
                    //The TreeModel doesn't automatically treeNodesInserted() yet
                    updateComponents();
                }
            }
            catch (DOMException dome) {
                JOptionPane.showMessageDialog(ChildFrame.this, dome,
                                              "XML Error",
                                              JOptionPane.WARNING_MESSAGE);
            }
        }

    } //}}}

    private class AddTextNodeAction
        implements ActionListener { //{{{

        public void actionPerformed(ActionEvent e) {
            try {
                TreePath selPath = tree.getLeadSelectionPath();
                if (selPath != null) {
                    XNode selectedNode = (XNode) selPath.
                        getLastPathComponent();
                    selectedNode.addNode("", "New Text Node", Node.TEXT_NODE);
                    //The TreeModel doesn't automatically treeNodesInserted() yet
                    updateComponents();
                }
            }
            catch (DOMException dome) {
                JOptionPane.showMessageDialog(ChildFrame.this, dome,
                                              "XML Error",
                                              JOptionPane.WARNING_MESSAGE);
            }
        }

    } //}}}

    private class RemoveNodeAction
        implements ActionListener { //{{{

        public void actionPerformed(ActionEvent e) {
            try {
                TreePath selPath = tree.getLeadSelectionPath();
                if (selPath != null) {
                    XNode selectedNode = (XNode) selPath.
                        getLastPathComponent();
                    selectedNode.remove();
                    //The TreeModel doesn't automatically treeNodesRemoved() yet
                    updateComponents();
                }
            }
            catch (DOMException dome) {
                JOptionPane.showMessageDialog(ChildFrame.this, dome,
                                              "XML Error",
                                              JOptionPane.WARNING_MESSAGE);
            }
        }

    } //}}}

    private class RemoveAttributeAction
        implements ActionListener { //{{{

        public RemoveAttributeAction(int r) {
            row = r;
        }

        public void actionPerformed(ActionEvent e) {
            ChildFrameTableModel model = (ChildFrameTableModel)
                attributesTable.getModel();
            XNode node = model.getAdapterNode();
            node.removeAttributeAt(row);
            updateComponents();
        }

        private int row;
    } //}}}

    private class AddAttributeAction
        implements ActionListener { //{{{

        public void actionPerformed(ActionEvent e) {
            attributesTable.editCellAt(attributesTable.getRowCount() - 1, 0);
            updateSourceArea();
        }

    } //}}}

    private class TablePopupListener
        extends MouseAdapter { //{{{
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            int row = attributesTable.getSelectedRow();
            if (e.isPopupTrigger() && row != -1) {

                ChildFrameTableModel model = (ChildFrameTableModel)
                    attributesTable.getModel();
                XNode node = model.getAdapterNode();
                JPopupMenu popup = new JPopupMenu();
                JMenuItem popupMenuItem;

                popupMenuItem = new JMenuItem("Add Attribute");
                popupMenuItem.addActionListener(new AddAttributeAction());
                popup.add(popupMenuItem);

                if (row != attributesTable.getRowCount() - 1) {
                    popupMenuItem = new JMenuItem("Remove Attribute");
                    popupMenuItem.addActionListener(new RemoveAttributeAction(
                        row));
                    popup.add(popupMenuItem);
                }
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    } //}}}

    private void updateTree() { //{{{
        //We must settle for this but this doesn't
        //update the tree properly. When the text node
        //is changed the tree cell size doesn't change
        //resulting in ... characters in the tree.
        //Being able to update the tree the way we want
        //to is going to be require more complex use of
        //tree rendering I think.
        tree.treeDidChange();
        updateSourceArea();
    } //}}}

    /**
     * Update Source View
     *
     * Jie Bao 2003-10-04
     */
    void updateSourceArea() {
        try {
            sourceXML.setDocument(mainWin, getXMLDocument());
        }
        catch (Exception ex) {
            Debug.trace(ex.toString());
        }
    }

    /**
     * This is temporary. It causes GUI components to be updated but does
     * not retain their state.
     */
    private void updateComponents() { //{{{

        tree.updateUI();
        //Clear the right hand pane of previous values.
        valuePane.setText("");

        //set the attributes table to the document itself
        XNode adapter = new XNode(currentDoc.getDocument());
        ChildFrameTableModel tableModel = new ChildFrameTableModel(this,
            adapter);
        attributesTable.setModel(tableModel);
        attributesTable.updateUI();

        // Jie Bao 2003-10-04 update source view
        updateSourceArea();
    } //}}}

    private XTree tree;
    private MainFrame mainWin = null;

    private JEditorPane valuePane = new JEditorPane("text/plain", "");
    private JTable attributesTable = new JTable();
    private SourceView sourceXML = new SourceView();

    private JSplitPane leftPane;
    private JSplitPane wholePane;
    private XMLDocument currentDoc;

    private static final String viewname = "documentview.default";
    private TableModelListener tableListener = new TableModelListener() { //{{{
        public void tableChanged(TableModelEvent e) {
            attributesTable.updateUI();
        }
    }; //}}}
    private TreeModelListener treeListener = new TreeModelListener() { //{{{

        public void treeNodesChanged(TreeModelEvent e) {
//            Debug.trace("TreeModelListener");
            updateTree();
        }

        //These aren't called yet.
        public void treeNodesInserted(TreeModelEvent e) {
            updateComponents();
        }

        public void treeNodesRemoved(TreeModelEvent e) {
            updateComponents();
        }

        public void treeStructureChanged(TreeModelEvent e) {
            updateComponents();
        }

    }; //}}}
    private DocumentListener docListener = new DocumentListener() { //{{{

        public void changedUpdate(DocumentEvent e) {
//            Debug.trace("DocumentListener:changedUpdate");
            updateTree();
        }

        public void insertUpdate(DocumentEvent e) {
//            Debug.trace("DocumentListener:insertUpdate");
            updateTree();
        }

        public void removeUpdate(DocumentEvent e) {
//            Debug.trace("DocumentListener:removeUpdate");
            updateTree();
        };

    }; //}}}
    /**
     * ShowRootChangeListener implements the ChangeListener interface
     * to toggle the state of showing the root node in the tree.
     */
    class ShowRootChangeListener
        extends Object
        implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            tree.setRootVisible( ( (JCheckBox) e.getSource()).isSelected());
        }

    } // End of class XMLViewer.ShowRootChangeListener

    /**
     * TreeEditableChangeListener implements the ChangeListener interface
     * to toggle between allowing editing and now allowing editing in
     * the tree.
     */
    class TreeEditableChangeListener
        extends Object
        implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            tree.setEditable( ( (JCheckBox) e.getSource()).isSelected());
        }

    } // End of class XMLViewer.TreeEditableChangeListener

    public void refreshAction(ActionEvent ae) {
//        Debug.trace("refreshAction");
        /*        try {
//            Debug.trace("source: "+sourceXML.getXMLDocument().hashCode()+
//                        " this: "+this.getXMLDocument().hashCode());
             ChildFrameTreeModel treeModel = new ChildFrameTreeModel(this,
                        sourceXML.getXMLDocument());
//        Debug.trace(treeModel.toString());
                    treeModel.addTreeModelListener(treeListener);
                    tree.updateUI();
                    updateComponents();
                }
                catch (Exception ex) {
                }
         */

        XMLDocument document = getXMLDocument();
        try {
            document.validate();
        }
        catch (Exception e) {
            Debug.trace(e.toString());
        }

        XNode adapter = new XNode(document.getDocument());

        ChildFrameTreeModel treeModel = new ChildFrameTreeModel(this,
            document);
        ChildFrameTableModel tableModel = new ChildFrameTableModel(this,
            adapter);
        ChildFrameValueEditor styledDoc = new ChildFrameValueEditor(adapter);
//This adapter may have the listener already.
//addTreeModelListener does not add the listener
//again if it is already added.
        tree.setModel(treeModel);
        attributesTable.setModel(tableModel);
        treeModel.addTreeModelListener(treeListener);
        tableModel.addTableModelListener(tableListener);

        valuePane.setDocument(styledDoc);
        styledDoc.addDocumentListener(docListener);
//Clear the right hand pane of previous values.
        valuePane.setText("");

//update the UI so that the components
//are redrawn.
        attributesTable.updateUI();
        tree.updateUI();
        updateUI();
    }

    /**
     * ShowHandlesChangeListener implements the ChangeListener interface
     * to toggle the state of showing the handles in the tree.
     */
    private class ShowHandlesChangeListener
        extends Object
        implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            tree.setShowsRootHandles( ( (JCheckBox) e.getSource()).isSelected());
        }

    } // End of class XMLViewer.ShowHandlesChangeListener

    //}}}
    private class EditUndoAction
        implements ActionListener { //{{{
        public void actionPerformed(ActionEvent e) {
            //undo does nothing for now
            JOptionPane.showMessageDialog(null, "Not implemented yet",
                                          "Error",
                                          JOptionPane.WARNING_MESSAGE);
        }
    } //}}}

    private class EditRedoAction
        implements ActionListener { //{{{
        public void actionPerformed(ActionEvent e) {
            //redo action does nothing for now.
            JOptionPane.showMessageDialog(null, "Not implemented yet",
                                          "Error",
                                          JOptionPane.WARNING_MESSAGE);
        }
    } //}}}

    private class EditCutAction
        extends AbstractAction { //{{{
        public EditCutAction() {
            putValue(Action.NAME, "Cut");
        }

        public void actionPerformed(ActionEvent e) {
            sourceXML.getTextarea().cut();
        }
    } //}}}

    private class EditCopyAction
        extends AbstractAction { //{{{
        public EditCopyAction() {
            putValue(Action.NAME, "Copy");
        }

        public void actionPerformed(ActionEvent e) {
            sourceXML.getTextarea().copy();
        }
    } //}}}

    private class EditPasteAction
        extends AbstractAction { //{{{
        public EditPasteAction() {
            putValue(Action.NAME, "Paste");
        }

        public void actionPerformed(ActionEvent e) {
            sourceXML.getTextarea().paste();
        }
    } //}}}

    /**
     * @author Jie Bao
     * @version 2003-10-04
     */
    private class ViewRefreshSourceAction
        extends AbstractAction { //{{{
        public ViewRefreshSourceAction() {
            putValue(Action.NAME, "Refresh Source");
        }
        public void actionPerformed(ActionEvent e) {
            updateSourceArea();
        }
    } //}}}
}

