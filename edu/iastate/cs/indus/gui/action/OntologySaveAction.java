package edu.iastate.cs.indus.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import edu.iastate.cs.indus.gui.EditorWithAction;
import edu.iastate.cs.utils.FileFilterEx;
import edu.iastate.cs.utils.Utility;

public class OntologySaveAction
    implements ActionListener
{
    EditorWithAction parent;

    public OntologySaveAction(EditorWithAction parent)
    {
        this.parent = parent;
    }

    public void actionPerformed(ActionEvent e)
    {
        doSave();
        parent.UpdateTitle(null);
    }

    /**
     * Save the Ontology
     *
     * @version 2004-04-26
     * @author Jie Bao
     */
    void doSave()
    {
        if (!parent.ifEverSaved()) // not yet saved
        {
            JFileChooser saveDialog;

            saveDialog = new JFileChooser();

            saveDialog.setDialogType(JFileChooser.SAVE_DIALOG);
            saveDialog.setDialogTitle("Save");

            //Add a filter to display only XML files
            FileFilterEx firstFilter = new FileFilterEx("owl", "OWL Documents");
            saveDialog.addChoosableFileFilter(firstFilter);

            //The "All Files" file filter is added to the dialog
            //by default. Put it at the end of the list.
            FileFilter all = saveDialog.getAcceptAllFileFilter();
            saveDialog.removeChoosableFileFilter(all);
            saveDialog.addChoosableFileFilter(all);
            saveDialog.setFileFilter(firstFilter);

            int returnVal = saveDialog.showSaveDialog(parent.mainFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                String newfile = saveDialog.getSelectedFile().getPath();

                // if the name have no extenstion, append "owl"
                if (Utility.getFileExtenstion(newfile) == "")
                {
                    newfile += ".owl";
                }
                parent.saveOntology(new File(newfile));
            }
        }
        else
        {
            parent.saveOntology();
        }
    }

}
