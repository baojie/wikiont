package edu.iastate.cs.indus.owl.node;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntResource;

import edu.iastate.cs.indus.owl.OntologyWithPackage;

/**
 * @author Jie Bao
 * @since 2004-04-29
 */
public class PackageNode
    extends DefaultOntologyNode
{

    public PackageNode(String inPackageName,
                       Individual pkg,
                       OntologyWithPackage model)
    {
        super(inPackageName, DefaultOntologyNode.PACKAGE, pkg, model, null);
    }

    public void propertySetup()
    {
        /**@todo Implement this edu.iastate.cs.indus.owl.node.DefaultOntologyNode abstract method*/
    }

    public String getNodeInformation(boolean inHTML)
    {
        return "This is a package";
    }

    public static Individual findParentNode(DefaultOntologyNode parent)
    {
        Individual father = OntologyWithPackage.SYSTEM_GLOBAL_PKG;

        OntResource rrr = parent.getRes();

        boolean flag = (parent.getType() == DefaultOntologyNode.PACKAGE);

        // if parent is package or root
        if ( (rrr instanceof Individual) && flag)
        {
            father = (Individual) parent.getRes();
        }
        return father;
    }
}
