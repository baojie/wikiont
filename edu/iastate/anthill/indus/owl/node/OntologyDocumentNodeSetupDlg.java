package edu.iastate.anthill.indus.owl.node;

import java.awt.Frame;
import javax.swing.JTextField;

import edu.iastate.anthill.utils.Debug;
import edu.iastate.anthill.utils.gui.SetupDlg;

public class OntologyDocumentNodeSetupDlg
    extends DefaultOntologyNodeSetupDlg
{
    private JTextField uri = new JTextField();
    private JTextField prefix = new JTextField();

    public String getUri()
    {
        return uri.getText();
    }

    public String getPrefix()
    {
        return prefix.getText();
    }

    public OntologyDocumentNodeSetupDlg(Frame frame, String title,
                                        String uriStr,
                                        String prefixStr,
                                        String commentStr, boolean uriEditible)
    {
        super(frame, title, "", "", commentStr);

        try
        {
            this.inPackageName.setEditable(false);
            this.packageName.setEditable(false);

            addItem("Ontology URI", uri, uriStr);
            uri.setEditable(uriEditible);

            addItem("Name space", prefix, prefixStr);

            pack();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    // for test
    public static void main(String[] args)
    {

        OntologyDocumentNodeSetupDlg dlg = new OntologyDocumentNodeSetupDlg(
            null, "ConsoleExporter Setup",
            "http://foo.com/#", "Foo", "Nothing at all ", true);
        dlg.show();

        if (dlg.getAction() == SetupDlg.OK)
        {
            Debug.trace("URI = " + dlg.getUri() +
                        "\nName Space = " + dlg.getPrefix() +
                        "\nComment = " + dlg.getComment());
        }
    }

}
