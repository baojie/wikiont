package edu.iastate.cs.indus.owl.hierarchy;

import edu.iastate.cs.indus.owl.OntologyWithPackage;

/**
 * @author Jie Bao
 * @since 1.0
 */

public class InPackageHierarchy
    extends OntHierarchyImpl
    implements OntHierarchy
{
    OntologyWithPackage jenaModel;

    public InPackageHierarchy(OntologyWithPackage jenaModel)
    {
        this.jenaModel = jenaModel;
    }

    /**
     * createFromModel
     *
     * @param jenaModel OntologyWithPackage
     * @return Tree
     */
    public Tree createFromModel(OntologyWithPackage jenaModel)
    {
        return null;
    }

    /**
     * toString
     *
     * @return String
     */
    public String toString()
    {
        return "";
    }

}
