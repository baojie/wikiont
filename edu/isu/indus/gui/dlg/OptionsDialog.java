/*
 OptionsDialog.java
 :tabSize=4:indentSize=4:noTabs=true:
 :folding=explicit:collapseFolds=1:
 jsXe is the Java Simple XML Editor
 jsXe is a gui application that creates a tree view of an XML document.
 The user can then edit this tree and the content in the tree.
 This file contains the OptionsDialog class that defines the options dialog
 in jsXe.
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

package edu.isu.indus.gui.dlg;

//{{{ imports
/*
 All classes are listed explicitly so
 it is easy to see which package it
 belongs to.
 */

//{{{ jsXe classes
//}}}
//{{{ AWT components
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.Border;

import edu.isu.indus.XmlEditor;
import edu.isu.indus.gui.MainFrame;
import edu.isu.indus.gui.view.DocumentView;
import edu.isu.indus.xml.XMLDocument;

//}}}

//}}}

public class OptionsDialog
    extends JDialog {

    public OptionsDialog(MainFrame view) { //{{{
        super(view, true);

        JPanel frame = new JPanel();
        getContentPane().add(frame, BorderLayout.CENTER);

        DocumentView panel = view.getDocumentView();
        document = panel.getXMLDocument();

        JButton OKButton = new JButton("OK");
        JButton CancelButton = new JButton("Cancel");
        OKButton.addActionListener(new OKAction(this));
        CancelButton.addActionListener(new CancelAction(this));

        Border border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Border border2 = BorderFactory.createEmptyBorder(0, 10, 0, 10);
        frame.setBorder(border1);

        setTitle("Options");
        setSize(dialogWidth, dialogHeight);
        setLocationRelativeTo(view);

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        frame.setLayout(layout);

        // Create the North Panel

        OptionsPanel OptionsNorthPanel = XmlEditor.getOptionsPanel();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1.0f;
        constraints.insets = new Insets(1, 0, 1, 0);

        JLabel label;
        JSeparator sep;

        if (OptionsNorthPanel != null) {
            label = new JLabel(OptionsNorthPanel.getTitle());
            layout.setConstraints(label, constraints);
            frame.add(label);
            sep = new JSeparator(JSeparator.HORIZONTAL);
            layout.setConstraints(sep, constraints);
            frame.add(sep);

            OptionsNorthPanel.setBorder(border2);
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weighty = 1.0f;
            layout.setConstraints(OptionsNorthPanel, constraints);
            frame.add(OptionsNorthPanel);
            constraints.weighty = 0;
        }

        // Create the South Panel

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1.0f;
        constraints.insets = new Insets(1, 0, 1, 0);

        OptionsSouthPanel = panel.getOptionsPanel();

        if (OptionsSouthPanel != null) {
            label = new JLabel(OptionsSouthPanel.getTitle());
            layout.setConstraints(label, constraints);
            frame.add(label);
            sep = new JSeparator(JSeparator.HORIZONTAL);
            layout.setConstraints(sep, constraints);
            frame.add(sep);

            OptionsSouthPanel.setBorder(border2);
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weighty = 1.0f;
            layout.setConstraints(OptionsSouthPanel, constraints);
            frame.add(OptionsSouthPanel);
            constraints.weighty = 0;
        }

        // Create the Document specific panel

        DocumentOptionsPanel = new XMLDocumentOptionsPanel(document);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1.0f;
        constraints.insets = new Insets(1, 0, 1, 0);

        DocumentOptionsPanel.setBorder(border2);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weighty = 1.0f;
        layout.setConstraints(DocumentOptionsPanel, constraints);
        frame.add(DocumentOptionsPanel);
        constraints.weighty = 0;

        JPanel ButtonsPanel = new JPanel();
        ButtonsPanel.add(OKButton);
        ButtonsPanel.add(CancelButton);

        getContentPane().add(ButtonsPanel, BorderLayout.SOUTH);

    } //}}}

    //{{{ Private members

    private class OKAction
        implements ActionListener { //{{{
        public OKAction(Dialog p) {
            parent = p;
        }

        public void actionPerformed(ActionEvent e) {
            if (OptionsNorthPanel != null) {
                OptionsNorthPanel.saveOptions();
            }
            if (OptionsSouthPanel != null) {
                OptionsSouthPanel.saveOptions();
            }
            if (DocumentOptionsPanel != null) {
                DocumentOptionsPanel.saveOptions();
            }
            parent.dispose();
        }

        private Dialog parent;
    } //}}}

    private class CancelAction
        implements ActionListener { //{{{
        public CancelAction(Dialog p) {
            parent = p;
        }

        public void actionPerformed(ActionEvent e) {
            parent.dispose();
        }

        private Dialog parent;
    } //}}}

    private int dialogWidth = 450;
    private int dialogHeight = 450;
    private XMLDocument document;
    private OptionsPanel OptionsNorthPanel;
    private OptionsPanel OptionsSouthPanel;
    private OptionsPanel DocumentOptionsPanel;
    //}}}

}