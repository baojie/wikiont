package edu.iastate.cs.indus.owl.node;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;

import edu.iastate.cs.indus.owl.OntologyWithPackage;
import edu.iastate.cs.indus.owl.jena.render.TermRenderer;

/**
 * @author Jie Bao
 * @version 1.0 2004-04-29
 */
public class ClassNode
    extends DefaultOntologyNode
{
    public ClassNode(String inPackageName,
                     Individual pkg,
                     OntologyWithPackage model)
    {
        super(inPackageName, DefaultOntologyNode.CLASS, pkg, model, null);
    }

    public ClassNode(OntClass res,
                     OntologyWithPackage model)
    {
        super(res,DefaultOntologyNode.CLASS, model);
    }

    /**
     * propertySetup
     */
    public void propertySetup()
    {
        // show the setup dlg
        ClassNodeSetupDlg dlg = new ClassNodeSetupDlg(
            null, "Class properties",
            "nameStr", "packageStr", "commentStr");
        dlg.show();
    }

    /**
     * getNodeInformation
     *
     * @param inHTML boolean
     * @return String
     */
    public String getNodeInformation(boolean inHTML)
    {
        TermRenderer render = new TermRenderer();
        return render.renderClass( (OntClass) getRes());
    }

}
