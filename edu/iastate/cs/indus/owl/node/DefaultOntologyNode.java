package edu.iastate.cs.indus.owl.node;

import javax.swing.tree.DefaultMutableTreeNode;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntResource;
import edu.iastate.cs.indus.owl.OntologyWithPackage;
import edu.iastate.cs.indus.owl.ResourceWithPackage;

abstract public class DefaultOntologyNode
    extends DefaultMutableTreeNode
{
//    static String DEFAULT_LANGAUGE = "EN";

    // type of the node
    protected short type;

    // corresponding node in the jena model
    protected ResourceWithPackage resWithPackage;

    public short getType()
    {
        return type;
    }

    public Object getUserObject()
    {
        return userObject;
    }

    public void setUserObject(Object userObject)
    {
        this.userObject = userObject;
    }

    /**
     * Get the in-package name
     *
     * @return String
     * @since 2004-04-29
     */
    public String getInPackageName()
    {
        return resWithPackage.getInPackageName();
    }

    /**
     *
     * @return String
     * @since 2004-04-30
     */
    public String getPackageName()
    {
        return resWithPackage.getPackageName();
    }

    /**
     *
     * @return String
     * @since 2004-04-30
     */
    public String getJenaLocalName()
    {
        return resWithPackage.getJenaLocalName();
    }

    /**
     * Create a node with package attribute
     *
     * @param inPackageName String
     * @param type short
     * @param pkg Individual  - the package of this node. could be null for global nodes
     * @param model OntologyWithPackage
     * @param instanceOf OntClass
     *
     * @author Jie Bao
     * @since 2004-04-29
     */
    public DefaultOntologyNode(String inPackageName,
                               short type,
                               Individual pkg,
                               OntologyWithPackage model,
                               OntClass instanceOf)
    {
        super();
        this.type = type;
        this.resWithPackage = new ResourceWithPackage(inPackageName, type, pkg,
            model, instanceOf);
        this.setUserObject(resWithPackage);

    }

    /**
     * Create from a exiestent resource
     *
     * @param res OntResource
     *
     * @since 2004-05-01
     */
    public DefaultOntologyNode(OntResource res, short type,
                               OntologyWithPackage model)
    {
        super();
        resWithPackage = new ResourceWithPackage(res, type, model);
        type = resWithPackage.getType();
        setUserObject(resWithPackage);
    }



    /**
     * Return information about the node
     * @return String
     */
    abstract public String getNodeInformation(boolean inHTML);

    abstract public void propertySetup();

    public void setComment(String comment)
    {
        getRes().setComment(comment, null);
    }

    public String getComment()
    {
        return getRes().getComment(null);
    }

    public OntResource getRes()
    {
        return resWithPackage.getRes();
    }

    /**
     * rename the node and its ontology resource node
     *
     * @param newName String
     * @author Jie Bao
     * @since 2004-04-30
     */
    public void rename(String newInPackageName)
    {
        resWithPackage.rename(newInPackageName, (type == PACKAGE));
    }

    public final static short ROOT = 0;
    public final static short ALL_CLASSES = 1;
    public final static short ALL_PROPERTIES = 2;
    public final static short ALL_INSTANCES = 3;

    public final static short CLASS = 4;
    public final static short PROPERTY = 5;
    public final static short INSTANCE = 6;

    public final static short PACKAGE = 7;
    public final static short DOMAIN = 8;

}
