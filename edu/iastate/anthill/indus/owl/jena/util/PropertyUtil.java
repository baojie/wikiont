package edu.iastate.anthill.indus.owl.jena.util;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * A collection of static utility methods for ontology properties.
 *
 * @author Jie Bao
 * @since 2004-05-02
 */
public class PropertyUtil
    extends JenaUtil
{
    public static ObjectProperty asObjectProperty(Resource resource)
    {
        OntModel ontModel = (OntModel) resource.getModel();
        return (ObjectProperty) ontModel.getResource(resource.getURI()).as(
            ObjectProperty.class);
    }

    public static DatatypeProperty asDatatypeProperty(Resource resource)
    {
        OntModel ontModel = (OntModel) resource.getModel();
        return (DatatypeProperty) ontModel.getResource(resource.getURI()).as(
            DatatypeProperty.class);
    }

    public static OntProperty asOntProperty(Resource resource)
    {
        OntModel ontModel = (OntModel) resource.getModel();
        return ontModel.getOntProperty(resource.getURI());
    }

    public static boolean canAsDatatypeProperty(RDFNode resource)
    {
        return canAs(resource, OWL.DatatypeProperty);
    }

    public static boolean canAsObjectProperty(RDFNode resource)
    {
        return canAs(resource, OWL.ObjectProperty);
    }

    public static boolean canAsOntProperty(RDFNode resource)
    {
        return canAs(resource, RDF.Property);
    }

    public static boolean canAsOWLProperty(RDFNode resource)
    {
        return canAsObjectProperty(resource) || canAsDatatypeProperty(resource);
    }

    public static Hashtable convertTypedAnnotationPropertiesIntoUntyped(
        OntModel ontModel)
    {
        Hashtable result = new Hashtable();
        Resource annotationPropertyClass = ontModel.getProfile().
            ANNOTATION_PROPERTY();
        for (Iterator it = ontModel.listDatatypeProperties(); it.hasNext(); )
        {
            DatatypeProperty property = (DatatypeProperty) it.next();
            if (property.canAs(AnnotationProperty.class))
            {
                addRDFTypes(result, property);
            }
        }
        for (Iterator it = ontModel.listObjectProperties(); it.hasNext(); )
        {
            ObjectProperty property = (ObjectProperty) it.next();
            if (property.canAs(AnnotationProperty.class))
            {
                addRDFTypes(result, property);
            }
        }
        for (Iterator it = result.keySet().iterator(); it.hasNext(); )
        {
            OntProperty property = (OntProperty) it.next();
            List types = (List) result.get(property);
            for (Iterator tit = types.iterator(); tit.hasNext(); )
            {
                Resource type = (Resource) tit.next();
                if (!type.equals(annotationPropertyClass))
                {
                    property.removeRDFType(type);
                    System.out.println("Temporarily removed type " + type +
                                       " from " + property);
                }
            }
        }
        return result;
    }

    public static void copyPropertyValues(Resource from, Resource to)
    {
        for (Iterator it = from.listProperties(); it.hasNext(); )
        {
            Property property = (Property) it.next();
            for (StmtIterator pit = from.listProperties(property); pit.hasNext(); )
            {
                Statement s = pit.nextStatement();
                to.addProperty(property, s.getObject());
            }
        }
    }

    /**
     * A convenience method that removes a property value with a given toString serialization
     * from an OntResource.  This method was introduced to overcome the lack of a similar
     * method in Jena.
     * @param resource  the OntResource to remove the property value from
     * @param property  the Property to remove a value of
     * @param value  the toString value to remove
     */
    public static void removePropertyValue(OntResource resource,
                                           Property property, String value)
    {
        for (StmtIterator it = resource.listProperties(property); it.hasNext(); )
        {
            Statement statement = it.nextStatement();
            RDFNode rdfNode = statement.getObject();
            if (rdfNode.toString().equals(value))
            {
                resource.removeProperty(property, rdfNode);
                return;
            }
        }
    }

    public static boolean isObjectProperty(OntModel ontModel,
                                           OntProperty ontProperty)
    {
        Resource objectPropertyClass = ontModel.getResource(com.hp.hpl.jena.
            vocabulary.OWL.ObjectProperty.getURI());
        return JenaUtil.canAs(ontProperty, objectPropertyClass);
    }

    public static boolean isDatatypeProperty(OntModel ontModel,
                                             OntProperty ontProperty)
    {
        Resource datatypePropertyClass = ontModel.getResource(com.hp.hpl.jena.
            vocabulary.OWL.DatatypeProperty.getURI());
        return JenaUtil.canAs(ontProperty, datatypePropertyClass);
    }

    public static boolean isSystemProperty(OntProperty property)
    {
        return isSystemResource(property) || property.hasDomain(OWL.Class);
    }
}
