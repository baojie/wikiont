/*
 SourceView.java
 :tabSize=4:indentSize=4:noTabs=true:
 :folding=explicit:collapseFolds=1:
 jsXe is the Java Simple XML Editor
 jsXe is a gui application that creates a tree view of an XML document.
 The user can then edit this tree and the content in the tree.
 This file contions the code source view in jsXe. This will eventually
 be scrapped and a jEdit syntax highlighting view will be added.
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
//{{{ DOM Classes
import java.io.*;

import java.awt.*;
import javax.swing.*;

import edu.isu.indus.gui.*;
import edu.isu.indus.xml.*;
import edu.isu.indus.gui.dlg.*;
//}}}

//}}}

public class SourceView
    extends DocumentView {

    protected SourceView() { //{{{

        panel = new JPanel();

        textarea = new JTextArea("");
        textarea.setTabSize(4);
        textarea.setCaretPosition(0);
        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textarea);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

    } //}}}

    public JMenu[] getMenus() { //{{{

        JMenu[] menus = new JMenu[1];

        //{{{ Construct Edit Menu
        JMenu menu = new JMenu("Edit");
        // These don't do anything yet.
        // JMenuItem menuItem = new JMenuItem("Undo");
        // menuItem.addActionListener( new EditUndoAction() );
        // menu.add( menuItem );
        // menuItem = new JMenuItem("Redo");
        // menuItem.addActionListener( new EditRedoAction() );
        // menu.add(menuItem);
        // menu.addSeparator();
        //JMenuItem menuItem = new JMenuItem(new EditCutAction());
        //menu.add(menuItem);
        //menuItem = new JMenuItem(new EditCopyAction());
        //menu.add(menuItem);
        //menuItem = new JMenuItem(new EditPasteAction());
        //menu.add(menuItem);
        //}}}

        menus[0] = menu;
        return menus;
    } //}}}

    public OptionsPanel getOptionsPanel() { //{{{
        return null;
    } //}}}

    public void setDocument(MainFrame view, XMLDocument document) throws
        IOException { //{{{

        currentdoc = document;
        textarea.setTabSize( (new Integer(document.getProperty("indent", "4"))).
                            intValue());
        textarea.setDocument(new SourceViewDocument(view, document));

    } //}}}

    public String getText() {
        return textarea.getText();
    }

    public XMLDocument getXMLDocument() { //{{{
        return currentdoc;
    } //}}}

    public void close(MainFrame view) { //{{{

    }

    public JTextArea getTextarea() {
        return textarea;
    } //}}}

    //{{{ Private members

   private XMLDocument currentdoc;
    private JTextArea textarea;
    private JPanel panel;
    //}}}

}
