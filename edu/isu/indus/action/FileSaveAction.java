/*
 FileSaveAction.java
 :tabSize=4:indentSize=4:noTabs=true:
 :folding=explicit:collapseFolds=1:
 jsXe is the Java Simple XML Editor
 jsXe is a gui application that creates a tree view of an XML document.
 The user can then edit this tree and the content in the tree.
 This file contains the action taken when the users selects
 Save from the file menu.
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
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import edu.isu.indus.gui.MainFrame;
import edu.isu.indus.xml.XMLDocument;

//}}}

//}}}

public class FileSaveAction
    extends AbstractAction {

    public FileSaveAction(MainFrame parent) { //{{{
        putValue(Action.NAME, "Save");
        view = parent;
    } //}}}

    public void actionPerformed(ActionEvent e) { //{{{
        XMLDocument doc = view.getDocumentView().getXMLDocument();
        if (doc.isUntitled()) {
            //perform a saveAs action
            (new FileSaveAsAction(view)).actionPerformed(e);
        }
        else {
            try {

                doc.save();
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
