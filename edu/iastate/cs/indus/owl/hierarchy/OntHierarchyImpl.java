package edu.iastate.cs.indus.owl.hierarchy;

import java.util.Iterator;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author Jie Bao
 * @since 1.0
 */

abstract public class OntHierarchyImpl
    implements OntHierarchy
{
    /**
     * listChild
     *
     * @param res Resource
     * @return Iterator
     */
    public Iterator listChild(Resource res)
    {
        return null;
    }

}
