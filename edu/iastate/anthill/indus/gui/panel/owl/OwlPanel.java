package edu.iastate.anthill.indus.gui.panel.owl;

import java.awt.BorderLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.iastate.anthill.indus.gui.EditorGUI;

/**
 * The panel to hold owl text
 *
 * @author Jie Bao
 * @version 2004-04-22
 */
public class OwlPanel
    extends JPanel
{
    EditorGUI parent;
    JEditorPane previewWin = new JEditorPane("text/plain", "");
    // 2004-04-22
    public OwlPanel(EditorGUI parent)
    {
        this.parent = parent;
        try
        {
            jbInit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    void jbInit() throws Exception
    {
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(previewWin), BorderLayout.CENTER);
        previewWin.setEditable(false);
    }

    public void updatePreview(String text)
    {
        previewWin.setText(text);
    }
}
