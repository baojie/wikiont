/*
 TabbedView.java
 :tabSize=4:indentSize=4:noTabs=true:
 :folding=explicit:collapseFolds=1:
 jsXe is the Java Simple XML Editor
 jsXe is a gui application that creates a tree view of an XML document.
 The user can then edit this tree and the content in the tree.
 This file contains the code for the frame in jsXe that contains
 the JTabbedPane handles the DocumentViews.
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

package edu.isu.indus.gui;

//{{{ imports
/*
 All classes are listed explicitly so
 it is easy to see which package it
 belongs to.
 */

//{{{ jsXe classes
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.iastate.cs.utils.Debug;
import edu.isu.indus.XmlEditor;
import edu.isu.indus.action.FileCloseAction;
import edu.isu.indus.action.FileExitAction;
import edu.isu.indus.action.FileNewAction;
import edu.isu.indus.action.FileOpenAction;
import edu.isu.indus.action.FileSaveAction;
import edu.isu.indus.action.FileSaveAsAction;
import edu.isu.indus.action.HelpAboutAction;
import edu.isu.indus.action.ToolsOptionsAction;
import edu.isu.indus.gui.view.DocumentView;
import edu.isu.indus.gui.view.DocumentViewFactory;
import edu.isu.indus.xml.XMLDocument;

//}}}

//}}}

/**
 * The view container that holds the JTabbedPane that holds
 * all open DocumentViews.
 *
 * @author Ian Lewis (<a href="mailto:IanLewis@member.fsf.org">IanLewis@member.fsf.org</a>)
 * @version $Id: MainFrame.java,v 1.2 2004/06/20 04:37:26 kangdaeki Exp $
 */
public class MainFrame
    extends JFrame {

    /**
     * Constructs a new TabbedView
     */
    public MainFrame() { //{{{

        try {
            UIManager.setLookAndFeel(UIManager.
                                     getCrossPlatformLookAndFeelClassName());
            // If you want the System L&F instead, comment out the above line and
            // uncomment the following:
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception exc) {
            System.err.println("Error loading L&F: " + exc);
        }

        int width = 800;
        int height = 600;
        int x = 0;
        int y = 0;

        DocumentViewFactory factory = DocumentViewFactory.newInstance();
        // docView = factory.newDocumentView();

        // updateMenuBar();

        tabbedPane.addChangeListener( //{{{
            new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                //it's possible to change to another file
                //that is using another view.
                updateMenuBar();
                updateTitle();
            }
        }); //}}}

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        pack();

        //Set window options
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowHandler());

        setIconImage(XmlEditor.getIcon().getImage());

        setBounds(new Rectangle(x, y, width, height));
    } //}}}

    /**
     * Gets the current DocumentView that is being displayed
     * by the JTabbedPane
     * @return the current DocumentView
     */
    public DocumentView getDocumentView() { //{{{
        return (DocumentView) tabbedPane.getSelectedComponent();
    } //}}}

    /**
     * Adds a document to the main view. This is essentially opening
     * the document in jsXe.
     * @param doc The XMLDocument to add to the view
     */
    public void addDocument(XMLDocument doc) throws IOException { //{{{
        if (doc != null) {
            DocumentViewFactory factory = DocumentViewFactory.newInstance();
            DocumentView newDocView = factory.newDocumentView();
            newDocView.setDocument(this, doc);
            tabbedPane.add(doc.getName(), newDocView);
            tabbedPane.setSelectedComponent(newDocView);
            updateTitle();
            updateMenuBar();
        }
    } //}}}

    /**
     * Sets the current document and makes sure it is displayed. If
     * the document is not already open then this method does nothing.
     * @param doc The document to set
     */
    public void setDocument(XMLDocument doc) throws IOException { //{{{
        if (doc != null) {
            XMLDocument[] docs = XmlEditor.getXMLDocuments();
            for (int i = 0; i < docs.length; i++) {
                if (docs[i] == doc) {
                    tabbedPane.setSelectedIndex(i);
                }
            }
            updateTitle();
            updateMenuBar();
        }
    } //}}}

    /**
     * Removes a document from the view. If the doc passed is not
     * already open this method does nothing.
     * @param doc The document to remove
     */
    public void removeDocument(XMLDocument doc) { //{{{
        if (doc != null) {
            XMLDocument[] docs = XmlEditor.getXMLDocuments();
            for (int i = 0; i < docs.length; i++) {
                if (docs[i] == doc) {
                    tabbedPane.remove(i);
                    //if the tab removed is not the rightmost tab
                    //stateChanged is not called for some
                    //reason.
                    updateTitle();
                    Debug.trace("stateChanged is still not checked");
                }
            }
        }
    } //}}}

    /**
     * Gets the number of open documents.
     * @return The number of documents open in this view.
     */
    public int getDocumentCount() { //{{{
        return tabbedPane.getTabCount();
    } //}}}

    /**
     * Updates the view.
     */
    public void update() { //{{{
        updateTitle();
        XMLDocument[] docs = XmlEditor.getXMLDocuments();
        for (int i = 0; i < docs.length; i++) {
            tabbedPane.setTitleAt(i, docs[i].getName());
        }
    } //}}}

    /**
     * Closes the view.
     */
    public void close() { //{{{

        XMLDocument[] docs = XmlEditor.getXMLDocuments();
        DocumentView currentDocView = null;

        //sequentially close all the document views
        for (int i = 0; i < docs.length; i++) {
            currentDocView = (DocumentView) tabbedPane.getComponentAt(0);
            currentDocView.close(this);
            tabbedPane.remove(0);
        }
    } //}}}

    //{{{ Private members

    private void updateTitle() {
        DocumentView currentDocView = getDocumentView();
        if (currentDocView != null) {
            XMLDocument document = currentDocView.getXMLDocument();
            String name = "";
            if (document != null) {
                name = document.getName();
            }
            setTitle(name);
        }
        else {
            setTitle("");
        }
    }

    /**
     * Updates the menubar. Useful when the DocumentView has changed.
     */
    private void updateMenuBar() { //{{{
        JMenuBar menubar = new JMenuBar();
        DocumentView currentDocView = getDocumentView();

        if (currentDocView != null) {

            //{{{ Add File Menu
            JMenu fileMenu = new JMenu("File");
            JMenuItem menuItem = new JMenuItem(new FileNewAction(this));
            menuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
            fileMenu.add(menuItem);
            menuItem = new JMenuItem(new FileOpenAction(this));
            menuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
            fileMenu.add(menuItem);
            fileMenu.addSeparator();
            menuItem = new JMenuItem(new FileSaveAction(this));
            menuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
            fileMenu.add(menuItem);
            menuItem = new JMenuItem(new FileSaveAsAction(this));
            fileMenu.add(menuItem);
            fileMenu.addSeparator();
            menuItem = new JMenuItem(new FileCloseAction(this));
            menuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl W"));
            fileMenu.add(menuItem);
            menuItem = new JMenuItem(new FileExitAction(this));
            fileMenu.add(menuItem);
            menubar.add(fileMenu); //}}}

            //{{{ Add View Specific Menus
            JMenu[] menus = currentDocView.getMenus();
            if (menus != null) {
                for (int i = 0; i < menus.length; i++) {
                    menubar.add(menus[i]);
                }
            }
            //}}}

            //{{{ Add Tools Menu
            JMenu toolsMenu = new JMenu("Tools");
            menuItem = new JMenuItem(new ToolsOptionsAction(this));
            toolsMenu.add(menuItem);
            fileMenu.addSeparator();

            menubar.add(toolsMenu); //}}}

            //{{{ Add Help Menu
            JMenu helpMenu = new JMenu("Help");
            menuItem = new JMenuItem(new HelpAboutAction());
            helpMenu.add(menuItem);
            menubar.add(helpMenu); //}}}

            setJMenuBar(menubar);

            //Need to cause a repaint after menubar is changed.
            getRootPane().revalidate();
        }
    } //}}}

    /**
     * Sets the DocumentView.
     */
    private void setDocumentView(DocumentView newView) { //{{{

        XMLDocument[] docs = XmlEditor.getXMLDocuments();
        DocumentView oldView = getDocumentView();
        int index = tabbedPane.getSelectedIndex();

        XMLDocument currentDoc = docs[index];

        if (oldView != null) {
            try {
                //close the previous view
                oldView.close(this);

                //try to open the document in the new view
                newView.setDocument(this, currentDoc);

                //no exceptions? cool. register the new view
                tabbedPane.remove(oldView);
                tabbedPane.add(newView, index);
                tabbedPane.setTitleAt(index, currentDoc.getName());
                tabbedPane.setSelectedIndex(index);
                updateMenuBar();
            }
            catch (IOException ioe) {
                //Some sort of error occured
                //We didn't register the new view yet.
                //So do nothing but display an error.
                JOptionPane.showMessageDialog(this, ioe, "I/O Error",
                                              JOptionPane.WARNING_MESSAGE);
            }
        }
    } //}}}

    private class WindowHandler
        extends WindowAdapter { //{{{
        public void windowClosing(WindowEvent e) {
            XmlEditor.exit(MainFrame.this);
        }
    } //}}}

    private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    //The current document
    private JPanel panel;
    //}}}
}