package edu.iastate.cs.indus.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import edu.iastate.cs.indus.gui.EditorWithAction;
import edu.iastate.cs.utils.FileFilterEx;
import edu.iastate.cs.utils.Utility;

public class OntologyOpenAction
    implements ActionListener
{
    EditorWithAction parent;

    public OntologyOpenAction(EditorWithAction parent)
    {
        this.parent = parent;
    }

    public void actionPerformed(ActionEvent e)
    {
        // close current document
        OntologyCloseAction a = new OntologyCloseAction(parent);
        a.doClose();

        doOpen();
        parent.UpdateTitle(null);
    }

    void doOpen()
    {
        JFileChooser openDialog;

        openDialog = new JFileChooser();

        openDialog.setDialogType(JFileChooser.OPEN_DIALOG);
        openDialog.setDialogTitle("Open");

//Add a filter to display only OWL files
        FileFilterEx firstFilter = new FileFilterEx("owl", "OWL Documents");
        openDialog.addChoosableFileFilter(firstFilter);

//The "All Files" file filter is added to the dialog
//by default. Put it at the end of the list.
        FileFilter all = openDialog.getAcceptAllFileFilter();
        openDialog.removeChoosableFileFilter(all);
        openDialog.addChoosableFileFilter(all);
        openDialog.setFileFilter(firstFilter);

        int returnVal = openDialog.showOpenDialog(parent.mainFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            String newfile = openDialog.getSelectedFile().getPath();

            // if the name have no extenstion, append "owl"
            if (Utility.getFileExtenstion(newfile) == "")
            {
                newfile += ".owl";
            }
            parent.openOntology(new File(newfile));

            // create GUI
            parent.createPanels();
            // menu
            parent.mainMenu = parent.createMenuBar();
            parent.mainFrame.setJMenuBar(parent.mainMenu);
            parent.mainFrame.validate();
            parent.UpdateOWLPane();

        }

    }

}
