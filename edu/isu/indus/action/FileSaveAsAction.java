/*
 FileSaveAsAction.java
 :tabSize=4:indentSize=4:noTabs=true:
 :folding=explicit:collapseFolds=1:
 jsXe is the Java Simple XML Editor
 jsXe is a gui application that creates a tree view of an XML document.
 The user can then edit this tree and the content in the tree.
 This file contains the action taken when a user selects
 Save As... from the file menu.
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

package edu.isu.indus.action;

//{{{ imports
/*
 All classes are listed explicitly so
 it is easy to see which package it
 belongs to.
 */

//{{{ jsXe classes
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import edu.iastate.cs.utils.FileFilterEx;
import edu.isu.indus.XmlEditor;
import edu.isu.indus.gui.MainFrame;
import edu.isu.indus.xml.XMLDocument;

//}}}

//}}}

public class FileSaveAsAction
    extends AbstractAction {

    public FileSaveAsAction(MainFrame parent) { //{{{
        putValue(Action.NAME, "Save As...");
        view = parent;
    } //}}}

    public void actionPerformed(ActionEvent e) { //{{{

        XMLDocument currentdoc = view.getDocumentView().getXMLDocument();
        //  if XMLFile is null, defaults to home directory
        JFileChooser saveDialog = new JFileChooser(currentdoc.getFile());
        saveDialog.setDialogType(JFileChooser.SAVE_DIALOG);
        saveDialog.setDialogTitle("Save As");

        //Add a filter to display only XML files
        FileFilterEx firstFilter = new FileFilterEx("xml", "XML Documents");
        saveDialog.addChoosableFileFilter(firstFilter);

        //The "All Files" file filter is added to the dialog
        //by default. Put it at the end of the list.
        FileFilter all = saveDialog.getAcceptAllFileFilter();
        saveDialog.removeChoosableFileFilter(all);
        saveDialog.addChoosableFileFilter(all);
        saveDialog.setFileFilter(firstFilter);

        int returnVal = saveDialog.showSaveDialog(view);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {

                File selectedFile = saveDialog.getSelectedFile();

                XMLDocument doc = XmlEditor.getOpenXMLDocument(selectedFile);

                //If the document is already open and
                //it isn't the current document
                if (doc != null &&
                    doc.equals(view.getDocumentView().getXMLDocument())) {
                    XmlEditor.closeXMLDocument(view, doc);
                    currentdoc.saveAs(selectedFile);
                }
                else {
                    currentdoc.saveAs(selectedFile);
                }
                view.update();

            }
            catch (SAXParseException spe) {
                JOptionPane.showMessageDialog(view,
                    "Document must be well-formed XML\n" + spe, "Parse Error",
                                              JOptionPane.WARNING_MESSAGE);
            }
            catch (SAXException sxe) {
                JOptionPane.showMessageDialog(view,
                    "Document must be well-formed XML\n" + sxe, "Parse Error",
                                              JOptionPane.WARNING_MESSAGE);
            }
            catch (ParserConfigurationException pce) {
                JOptionPane.showMessageDialog(view, pce,
                                              "Parser Configuration Error",
                                              JOptionPane.WARNING_MESSAGE);
            }
            catch (IOException ioe) {
                JOptionPane.showMessageDialog(view, ioe, "I/O Error",
                                              JOptionPane.WARNING_MESSAGE);
            }
        }
    } //}}}

    //{{{ Private members
    private MainFrame view;
    //}}}
}
