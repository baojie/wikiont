/*
 DocumentView.java
 :tabSize=4:indentSize=4:noTabs=true:
 :folding=explicit:collapseFolds=1:
 jsXe is the Java Simple XML Editor
     jsXe is a gui application that can edit an XML document and create a tree view.
 The user can then edit this tree and the content in the tree and save the
 document.
 This file contains the abstract class that defines the interface for
 views that are used by jsXe.
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
import java.io.*;

import javax.swing.*;

import edu.isu.indus.gui.*;
import edu.isu.indus.gui.dlg.*;
import edu.isu.indus.xml.*;

//}}}

//}}}

public abstract class DocumentView
    extends JPanel {

    public abstract void setDocument(MainFrame view, XMLDocument document) throws
        IOException;

    public abstract JMenu[] getMenus();

    public abstract OptionsPanel getOptionsPanel();

    public abstract XMLDocument getXMLDocument();

    public abstract void close(MainFrame view);

}
