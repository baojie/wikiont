package edu.iastate.anthill.indus.owl.hierarchy;

import java.util.Iterator;

import com.hp.hpl.jena.rdf.model.Resource;
import edu.iastate.anthill.indus.owl.OntologyWithPackage;

/**
 * Builder hierarchy tree from jena model
 *
 * @author Jie Bao
 * @since 2004-05-01
 */

public interface OntHierarchy
{
    /**
     *  create a hierarchy tree from jena model
     *
     * @param jenaModel OntologyWithPackage
     * @return Tree
     *
     * @author Jie Bao
     * @since 2004-05-01
     */
    public Tree createFromModel(OntologyWithPackage jenaModel);

    /**
     * list all child of the resource
     * it provides alternative ways to build the tree another then subClassOf
     *
     * @param res Resource
     * @return Iterator - each item in the list is a resource
     *
     * @author Jie Bao
     * @since 2004-05-01
     */
    public Iterator listChild(Resource res);

}
