/*
 DefaultViewDocument.java
 :tabSize=4:indentSize=4:noTabs=true:
 :folding=explicit:collapseFolds=1:
 jsXe is the Java Simple XML Editor
     jsXe is a gui application that can edit an XML document and create a tree view.
 The user can then edit this tree and the content in the tree and save the
 document.
 This file contains the model for the right hand pane (for editing the contents
 of text nodes) of the default view.
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
import java.awt.*;
import javax.swing.text.*;

import org.w3c.dom.*;
import edu.isu.indus.xml.*;

//}}}

public class ChildFrameValueEditor
    extends DefaultStyledDocument {

    protected ChildFrameValueEditor(XNode n) { //{{{
        super(new GapContent(), new StyleContext());
        try {
            if (n != null) {
                String value = n.getNodeValue();
                if (value != null) {
                    super.insertString(0, value, new SimpleAttributeSet());
                }
            }
        }
        catch (BadLocationException ble) {}
        node = n;
    } //}}}

    public void insertString(int offs, String str, AttributeSet a) throws
        BadLocationException { //{{{

        try {
            //the node needs to be updated before we call insertString
            //so that the listeners are invoked after all changes have
            //been made.
            String newNodeValue = super.getText(0, offs) + str +
                super.getText(offs, super.getLength() - offs);
            node.setNodeValue(newNodeValue);
            super.insertString(offs, str, a);
        }
        catch (DOMException dome) {
            Toolkit.getDefaultToolkit().beep();
        }

    } //}}}

    public void remove(int offs, int len) throws BadLocationException { //{{{

        try {
            //the node needs to be updated before we call remove
            //so that the listeners are invoked after all changes have
            //been made.
            String newNodeValue = super.getText(0, offs) +
                super.getText(offs + len, super.getLength() - offs - len);
            node.setNodeValue(newNodeValue);
            super.remove(offs, len);
        }
        catch (DOMException dome) {
            Toolkit.getDefaultToolkit().beep();
        }

    } //}}}

    //{{{ Private members
    XNode node;
    //}}}

}
