package edu.iastate.cs.indus.owl.node;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntProperty;
import edu.iastate.cs.indus.owl.OntologyWithPackage;
import edu.iastate.cs.indus.owl.jena.render.TermRenderer;

/**
 * @author Jie Bao
 * @since 1.0
 */

public class PropertyNode
    extends DefaultOntologyNode
{

    public PropertyNode(String inPackageName,
                        Individual pkg,
                        OntologyWithPackage model)
    {
        super(inPackageName, DefaultOntologyNode.PROPERTY, pkg, model, null);
    }

    public void propertySetup()
    {
        /**@todo Implement this edu.iastate.cs.indus.owl.node.DefaultOntologyNode abstract method*/
    }

    public String getNodeInformation(boolean inHTML)
    {
        TermRenderer render = new TermRenderer();
        return render.renderProperty( (OntProperty) getRes());
    }
}
