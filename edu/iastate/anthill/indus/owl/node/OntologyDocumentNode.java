package edu.iastate.anthill.indus.owl.node;

import edu.iastate.anthill.indus.owl.OntologyWithPackage;
import edu.iastate.anthill.indus.owl.jena.render.OntologyRenderer;
import edu.iastate.anthill.utils.gui.SetupDlg;
import com.hp.hpl.jena.ontology.OntModel;
import edu.iastate.anthill.indus.owl.jena.JenaModel;

public class OntologyDocumentNode
    extends DefaultOntologyNode
{
    OntologyWithPackage ontModel;
    public OntologyDocumentNode(String baseURI, OntologyWithPackage ontModel)
    {
        super(baseURI, ROOT, null, ontModel, null);
        this.ontModel = ontModel;
    }

    public String getNodeInformation(boolean inHTML)
    {
        OntologyRenderer render = new OntologyRenderer(null);
//        assert ontModel != null;
        return render.displayOntologyDescription( (OntModel) getRes().getModel(),
                                                 getRes().getURI());
    }

    public void propertySetup()
    {
        // get old setting
        String uri = ontModel.getOntURI(); // non-editable
        String prefix = ontModel.getNsPrefix();
        String comment = this.getComment();

        // show the setup dlg
        OntologyDocumentNodeSetupDlg dlg = new OntologyDocumentNodeSetupDlg(
            null, "Ontology properties",
            uri, prefix, comment, false);
        dlg.show();

        // get new setting
        if (dlg.getAction() == SetupDlg.OK)
        {
            //ontModel.setOntURI(dlg.getUri());
            ontModel.setNsPrefix(dlg.getPrefix());
            this.setComment(dlg.getComment());
        }

    }
}
