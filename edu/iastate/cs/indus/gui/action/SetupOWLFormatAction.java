package edu.iastate.cs.indus.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import edu.iastate.cs.indus.gui.EditorWithAction;
import edu.iastate.cs.indus.owl.jena.JenaModel;

public class SetupOWLFormatAction
    implements ActionListener
{
    EditorWithAction parent;

    public SetupOWLFormatAction(EditorWithAction parent)
    {
        this.parent = parent;
    }

    public void actionPerformed(ActionEvent e)
    {
        jMenuSetupOWL(e);
    }

    /**
     *
     * @param e ActionEvent
     *
     * @author Jie Bao
     * @version 2004-04-25
     */
    void jMenuSetupOWL(ActionEvent e)
    {
        JenaModel model = parent.getOntologyModel();
        // get old format
        String fmt = parent.getOntologyModel().getWriteFormat();

        // show a dlg to choose format
        Object[] possibleValues =
            {
			JenaModel.FULLXML, JenaModel.N_TRIPLE, JenaModel.N3,
			JenaModel.SHORTXML};
        Object selectedValue = JOptionPane.showInputDialog(null, "Choose one",
            "Input", JOptionPane.INFORMATION_MESSAGE, null, possibleValues,
            fmt);
        // set new format
        if (selectedValue != null)
        {
            model.setWriteFormat(selectedValue.toString());
            parent.UpdateOWLPane();
        }
    }

}
