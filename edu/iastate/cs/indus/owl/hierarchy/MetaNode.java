package edu.iastate.cs.indus.owl.hierarchy;

import edu.iastate.cs.indus.owl.ResourceWithPackage;
import edu.iastate.cs.indus.owl.node.DefaultOntologyNode;

/**
 * @author Jie Bao
 * @since 1.0 2004-05-01
 */

public class MetaNode
    extends DefaultOntologyNode
{
    public MetaNode( short type)
    {
        super(null,type,null);
        resWithPackage = new ResourceWithPackage(null, type, null);
        this.type = type;
        setUserObject(resWithPackage);
    }

    public void propertySetup()
    {
    }

    public String getNodeInformation(boolean inHTML)
    {
        return "This is a meta node";
    }
}
