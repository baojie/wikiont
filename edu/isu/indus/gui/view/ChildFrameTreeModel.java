/*
 DefaultViewTreeModel.java
 :tabSize=4:indentSize=4:noTabs=true:
 :folding=explicit:collapseFolds=1:
 jsXe is the Java Simple XML Editor
     jsXe is a gui application that can edit an XML document and create a tree view.
 The user can then edit this tree and the content in the tree and save the
 document.
 This file contains the adapter class that allows a XMLDocument class to serve
 as the model for a JTree.
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
//}}}
//{{{ DOM uses SAX Exceptions
//}}}
//{{{ Java base classes
import java.awt.Component;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import edu.isu.indus.xml.XMLDocument;
import edu.isu.indus.xml.XNode;

//}}}

//}}}

public class ChildFrameTreeModel
    implements TreeModel {

    protected ChildFrameTreeModel(Component parent, XMLDocument doc) { //{{{
        document = doc;
        view = parent;
    } //}}}

    // {{{ Implemented TreeModel methods

    public void addTreeModelListener(TreeModelListener listener) { //{{{
        if (listener != null && !treeListenerList.contains(listener)) {
            treeListenerList.addElement(listener);
        }
    } //}}}

    public Object getChild(Object parent, int index) { //{{{
        XNode node = (XNode) parent;

        boolean showComments = Boolean.valueOf(document.getProperty(
            "documentview.default.show.comment.nodes", "false")).booleanValue();
        boolean showEmpty = Boolean.valueOf(document.getProperty(
            "documentview.default.show.empty.nodes", "false")).booleanValue();

        boolean found = false;

        //massage the index so that it points returns
        //the correct child depending of if we are displaying
        //comments, empty nodes etc.
        for (int i = 0; i <= index; i++) {
            XNode child = node.child(i);

            if (child != null) {
                if (!showComments && child.getNodeType() == Node.COMMENT_NODE) {
                    index++;
                }
                if (!showEmpty && child.getNodeType() == Node.TEXT_NODE &&
                    child.getNodeValue().trim().equals("")) {
                    index++;
                }
            }

        }

        return node.child(index);
    } //}}}

    public int getChildCount(Object parent) { //{{{
        XNode node = (XNode) parent;
        int totalcount = node.childCount();
        int count = 0;
        for (int i = 0; i < totalcount; i++) {
            if (getChild(parent, i) != null) {
                count++;
            }
        }
        return count;
    } //}}}

    public int getIndexOfChild(Object parent, Object child) { //{{{
        XNode node = (XNode) parent;

        boolean showComments = Boolean.valueOf(document.getProperty(
            "show.comment.nodes", "false")).booleanValue();
        boolean showEmpty = Boolean.valueOf(document.getProperty(
            "show.empty.nodes", "false")).booleanValue();

        if (!showComments && node.getNodeType() == Node.COMMENT_NODE) {
            return -1;
        }
        if (!showEmpty && node.getNodeType() == Node.TEXT_NODE &&
            node.getNodeValue().trim() == "") {
            return -1;
        }

        return node.index( (XNode) child);
    } //}}}

    public Object getRoot() { //{{{
        return new XNode(document.getDocument());
    } //}}}

    public boolean isLeaf(Object aNode) { //{{{
        // Return true for any node with no children
        XNode node = (XNode) aNode;
        return (node.childCount() <= 0);
    } //}}}

    public void removeTreeModelListener(TreeModelListener listener) { //{{{
        if (listener != null) {
            treeListenerList.removeElement(listener);
        }
    } //}}}

    public void valueForPathChanged(TreePath path, Object newValue) { //{{{
        //get the nodes needed
        XNode node = (XNode) path.getLastPathComponent();
        try {
            node.setNodeName(newValue.toString());
            //notify the listeners that the tree structure has changed
            fireTreeNodesChanged(new TreeModelEvent(this, path));
        }
        catch (DOMException dome) {
            JOptionPane.showMessageDialog(view, dome, "Internal Error",
                                          JOptionPane.WARNING_MESSAGE);
        }
    } //}}}

    //}}}

    //{{{ Private members

    // {{{ Event notification methods

    private void fireTreeNodesChanged(TreeModelEvent e) { //{{{
        Enumeration listeners = treeListenerList.elements();
        while (listeners.hasMoreElements()) {
            TreeModelListener listener = (TreeModelListener) listeners.
                nextElement();
            listener.treeNodesChanged(e);
        }
    } //}}}

    private void fireTreeNodesInserted(TreeModelEvent e) { //{{{
        Enumeration listeners = treeListenerList.elements();
        while (listeners.hasMoreElements()) {
            TreeModelListener listener = (TreeModelListener) listeners.
                nextElement();
            listener.treeNodesInserted(e);
        }
    } //}}}

    private void fireTreeNodesRemoved(TreeModelEvent e) { //{{{
        Enumeration listeners = treeListenerList.elements();
        while (listeners.hasMoreElements()) {
            TreeModelListener listener = (TreeModelListener) listeners.
                nextElement();
            listener.treeNodesRemoved(e);
        }
    } //}}}

    private void fireTreeStructureChanged(TreeModelEvent e) { //{{{
        Enumeration listeners = treeListenerList.elements();
        while (listeners.hasMoreElements()) {
            TreeModelListener listener = (TreeModelListener) listeners.
                nextElement();
            listener.treeStructureChanged(e);
        }
    } //}}}

    // }}}

    private XMLDocument document;
    private Component view;
    private Vector treeListenerList = new Vector();
    //}}}
}
