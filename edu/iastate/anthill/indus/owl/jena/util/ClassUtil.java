package edu.iastate.anthill.indus.owl.jena.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import edu.iastate.anthill.indus.owl.jena.OWLMeta;

/**
 * A collection of static utility methods for ontology classes.
 *
 * @author Jie Bao
 * @since 2004-05-02
 */
public class ClassUtil
    extends JenaUtil
{
    public static OntClass asOntClass(Resource resource)
    {
        if (resource.canAs(OntClass.class))
        {
            return (OntClass) resource.as(OntClass.class);
        }
        else
        {
            OntModel ontModel = (OntModel) resource.getModel();
            OntClass ontClass = ontModel.getOntClass(resource.getURI());
            if (ontClass == null)
            {
                throw new IllegalArgumentException("Could not create OntClass");
            }
            return ontClass;
        }
    }

    public static boolean canAsOntClass(RDFNode resource)
    {
        return resource.canAs(OntClass.class) || canAs(resource, OWL.Class);
    }

    public static Model ensureOwlClassIsOntClass(OntModel ontModel)
    {
        Model newModel = ModelFactory.createDefaultModel();
        newModel.add(OWL.Thing, RDF.type, OWL.Class);
        newModel.add(OWL.Class, RDF.type, OWL.Class);
        newModel.add(OWL.DatatypeProperty, RDF.type, OWL.Class);
        newModel.add(OWL.ObjectProperty, RDF.type, OWL.Class);
        ontModel.addSubModel(newModel);
        return newModel;
    }

    public static boolean isSystemClass(OntClass ontClass)
    {
        return ontClass.equals(OWL.Class) ||
            ontClass.equals(OWL.ObjectProperty) ||
            ontClass.equals(OWL.DatatypeProperty) ||
            ontClass.equals(OWL.Nothing) ||
            ontClass.equals(OWL.Thing) ||
            ontClass.getNameSpace().equals(OWLMeta.NS);
    }

    /**
     * Answer an iterator over the classes we will use as the roots of the depicted
     * hierarchy.  We use named classes that either have Thing as a direct super-class,
     * or which have no declared super-classes.  The first condition is helpful if
     * using a reasoner, the second otherwise.
     * @param m A model
     * @return An iterator over the named class hierarchy roots in m
     */
    public static Iterator rootClasses(OntModel m)
    {
        List roots = new ArrayList();

        for (Iterator i = m.listClasses(); i.hasNext(); )
        {
            OntClass c = (OntClass) i.next();

            // too confusing to list all the restrictions as root classes
            if (c.isAnon())
            {
                continue;
            }

            if (c.hasSuperClass(m.getProfile().THING(), true))
            {
                // this class is directly descended from Thing
                roots.add(c);
            }
            else if (c.getCardinality(m.getProfile().SUB_CLASS_OF()) == 0)
            {
                // this class has no super-classes (can occur if we're not using the reasoner)
                roots.add(c);
            }
        }

        return roots.iterator();
    }
}
