package edu.iastate.anthill.indus.owl.node;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntProperty;
import edu.iastate.anthill.indus.owl.OntologyWithPackage;
import edu.iastate.anthill.indus.owl.jena.render.TermRenderer;

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
        /**@todo Implement this edu.iastate.anthill.indus.owl.node.DefaultOntologyNode abstract method*/
    }

    public String getNodeInformation(boolean inHTML)
    {
        TermRenderer render = new TermRenderer(null);
        return render.renderProperty( (OntProperty) getRes());
    }
}
