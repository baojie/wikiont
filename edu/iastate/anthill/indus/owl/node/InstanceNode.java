package edu.iastate.anthill.indus.owl.node;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import edu.iastate.anthill.indus.owl.OntologyWithPackage;
import edu.iastate.anthill.indus.owl.jena.render.TermRenderer;

/**
 * @author Jie Bao
 * @since 2004-04-29
 */

public class InstanceNode
    extends DefaultOntologyNode
{

    public InstanceNode(String inPackageName,
                        Individual pkg,
                        OntologyWithPackage model,
                        OntClass instanceOf
                        )
    {
        super(inPackageName, DefaultOntologyNode.INSTANCE, pkg, model,
              instanceOf);
    }

    public void propertySetup()
    {
        /**@todo Implement this edu.iastate.anthill.indus.owl.node.DefaultOntologyNode abstract method*/
    }

    public String getNodeInformation(boolean inHTML)
    {
        TermRenderer render = new TermRenderer(null);
        return render.renderInstance( (Individual) getRes());
    }
}
