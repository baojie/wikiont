package edu.iastate.anthill.indus.owl.jena;

import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;

/**
 * The namespaces and names from the Indus meta ontology.
 * This ontology is used to represent Indus-specific metadata such as
 * whether a class is abstract or not.
 *
 * adopted from protege owl plugin
 *
 * @author Jie Bao
 * @since 2004-04-30
 */
public class OWLMeta
{

    public final static String FILE = "http://boole.cs.iastate.edu/indus";

    public final static String NS = FILE + "#";

    public final static String ABSTRACT = "abstract";

    public final static String ALLOWED_PARENT = "allowedParent";

    public final static String CONSTRAINTS = "constraints";

    public final static String DIRECTED_BINARY_RELATION =
        "DIRECTED-BINARY-RELATION";

    public final static String DIRECT_TYPE = "directType";

    public final static String FROM = "from";

    public final static String PAL_CONSTRAINT = "PAL-Constraint";

    public final static String SUB_CLASS_OF = "subClassOf";

    public final static String TO = "to";

    public final static String SLOT_VALUES_COMPUTER = "valuesComputer";

    public static AnnotationProperty getAbstractProperty(OntModel ontModel)
    {
        return ontModel.getAnnotationProperty(NS + ABSTRACT);
    }

    public static AnnotationProperty getAllowedParentProperty(OntModel ontModel)
    {
        return ontModel.getAnnotationProperty(NS + ALLOWED_PARENT);
    }

    public static AnnotationProperty getDirectTypeProperty(OntModel ontModel)
    {
        return ontModel.getAnnotationProperty(NS + DIRECT_TYPE);
    }

    public static String getPALSlotName(Property property)
    {
        return ":" + property.getLocalName().toUpperCase();
    }

    public static String getSlotValuesComputerSlotName()
    {
        return "indus:" + SLOT_VALUES_COMPUTER;
    }
}
