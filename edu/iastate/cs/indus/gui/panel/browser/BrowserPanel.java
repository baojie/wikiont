package edu.iastate.cs.indus.gui.panel.browser;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.iastate.cs.indus.gui.EditorGUI;

public class BrowserPanel
    extends JPanel
{
    EditorGUI parent;
    JEditorPane previewWin = new JEditorPane("text/html", "");

    // 2004-04-22
    public BrowserPanel(EditorGUI parent)
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
