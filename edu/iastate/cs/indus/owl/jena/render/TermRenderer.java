package edu.iastate.cs.indus.owl.jena.render;

import java.util.Iterator;

import com.hp.hpl.jena.ontology.BooleanClassDescription;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.EnumeratedClass;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.io.PrintStream;
import com.hp.hpl.jena.shared.PrefixMapping;
import java.util.Map;
import java.util.HashMap;

/**
 * render term level  information
 * @author Jie Bao
 * @version 1.0
 */
public class TermRenderer
    extends TermRelationRenderer
{
    public TermRenderer()
    {
        super();
    }

    public String retrieveInfoOfTerm(String termURI)
    {
        // display term information from model
        String ontURI = termURI.substring(0, termURI.lastIndexOf("#"));
        OntModel selOntologyModel = (OntModel) AllOntologies.get(ontURI);
        return displayTermInfo(selOntologyModel, termURI);
    }

    public String displayTermInfo(OntModel model, String termURI)
    {

        String desc = "";

        if (model.getOntClass(termURI) != null)
        {
            // term is a class
            OntClass cla = model.getOntClass(termURI);

            desc += "<b>Concept: " + cla.getLocalName() + "</b>" +
                renderClass(cla);
        }

        else if (model.getObjectProperty(termURI) != null)
        {
            // term is an objectproperty
            ObjectProperty oprop = model.getObjectProperty(termURI);

            desc += "<b>Object Property (Role): " + oprop.getLocalName() +
                "</b>" + renderProperty(oprop); ;
        }

        else if (model.getDatatypeProperty(termURI) != null)
        {
            // term is an datatypeproperty
            DatatypeProperty dprop = model.getDatatypeProperty(termURI);

            desc += "<b>Datatype Attribute: " + dprop.getLocalName() + "</b>" +
                renderProperty(dprop); ;
        }
        else if (model.getIndividual(termURI) != null)
        {
            // term is an instance
            Individual instance = model.getIndividual(termURI);

            desc += "<b>Instance: " + instance.getLocalName() + "</b>" +
                renderInstance(instance); ;
        }

        // finally add the 'desc' string to the term description pane
        desc = parseDesc(desc); // parse the description string to remove extraneous line breaks

        return desc;
        // System.out.println(desc+NEWLINE); // for some reason, when term is selected in termList, reaches this function twice
    }

    public String renderClass(OntClass cla)
    {

        String desc = "";

        // get class comment
        if (!cla.isAnon())
        {
            desc += BREAKLINE + " <b>Description</b>:"
                + BREAKLINE + "URI = " + cla.getURI()
                + BREAKLINE + "Local Name = " + cla.getLocalName();

            String comment = "";
            comment = cla.getComment(null);
            if (comment != null)
            {
                desc += BREAKLINE + " <b>Description</b>:" + comment +
                    BREAKLINE;
            }
        }

        // list equivalent classes
        if ( (!cla.isAnon()) && (cla.listEquivalentClasses() != null))
        {
            ExtendedIterator iter = cla.listEquivalentClasses();
            String equStr = "";
            while (iter.hasNext())
            {
                OntClass equCla = (OntClass) iter.next();

                if (equCla.isAnon())
                {
                    equStr += NEWLINE + renderClass(equCla);
                }
                else
                {
                    equStr += NEWLINE + term2Link(equCla);
                }
            }

            if (!equStr.equals(""))
            {
                desc += BREAKLINE + " <b>Synonyms:</b> " + equStr + BREAKLINE;
            }
        }

        // list disjoint classes
        if (cla.listDisjointWith() != null)
        {
            ExtendedIterator iter = cla.listDisjointWith();
            String disjStr = "";
            while (iter.hasNext())
            {
                OntClass disjCla = (OntClass) iter.next();

                if (disjCla.isAnon())
                {
                    disjStr += NEWLINE + renderClass(disjCla);
                }
                else
                {
                    disjStr += NEWLINE + term2Link(disjCla);
                }
            }

            if (!disjStr.equals(""))
            {
                desc += BREAKLINE + " <b>Different from (disjoint-with):</b> " +
                    disjStr + BREAKLINE;
            }
        }

        // definition: union
        if (cla.isUnionClass())
        {
            String unionStr = "";
            BooleanClassDescription boolClass = cla.asUnionClass();

            for (Iterator iter = boolClass.listOperands(); iter.hasNext(); )
            {
                OntClass unionCla = (OntClass) iter.next();

                if (unionCla.isAnon())
                {
                    unionStr += NEWLINE + renderClass(unionCla);
                }
                else
                {
                    unionStr += NEWLINE + term2Link(unionCla);
                }
            }
            if (!unionStr.equals(""))
            {
                desc += BREAKLINE + " <b>Union of:</b> " + unionStr + BREAKLINE;
            }
        }

        // definition: intersection
        if (cla.isIntersectionClass())
        {
            String intersectionStr = "";
            BooleanClassDescription boolClass = cla.asIntersectionClass();

            for (Iterator iter = boolClass.listOperands(); iter.hasNext(); )
            {
                OntClass intersectionCla = (OntClass) iter.next();

                if (intersectionCla.isAnon())
                {
                    intersectionStr += NEWLINE + renderClass(intersectionCla);
                }
                else
                {
                    intersectionStr += NEWLINE + term2Link(intersectionCla);
                }
            }
            if (!intersectionStr.equals(""))
            {
                desc += BREAKLINE + " <b>Intersection of:</b> " +
                    intersectionStr +
                    BREAKLINE;
            }
        }

        // definition: complement
        if (cla.isComplementClass())
        {
            String complementStr = "";
            BooleanClassDescription boolClass = cla.asComplementClass();

            for (Iterator iter = boolClass.listOperands(); iter.hasNext(); )
            {
                OntClass complementCla = (OntClass) iter.next();

                if (complementCla.isAnon())
                {
                    complementStr += NEWLINE + renderClass(complementCla);
                }
                else
                {
                    complementStr += NEWLINE + term2Link(complementCla);
                }
            }
            if (!complementStr.equals(""))
            {
                desc += BREAKLINE + " <b>Opposite of (complement):</b> " +
                    complementStr + BREAKLINE;
            }
        }

        // definition: one-of classes
        if (cla.isEnumeratedClass())
        {
            String enumeratedStr = "";
            EnumeratedClass enumClass = cla.asEnumeratedClass();

            for (Iterator iter = enumClass.listOneOf(); iter.hasNext(); )
            {
                Resource enumeratedInst = (Resource) iter.next();

                enumeratedStr += NEWLINE + "<a href=\"" +
                    term2Link(enumeratedInst);
            }
            if (!enumeratedStr.equals(""))
            {
                desc += BREAKLINE + " <b>one of:</b> " + enumeratedStr +
                    BREAKLINE;
            }
        }

        // list sub-classes
        if ( (!cla.isAnon()) && (cla.listSubClasses() != null))
        {
            ExtendedIterator iter = cla.listSubClasses(true);
            String subStr = "";
            while (iter.hasNext())
            {
                OntClass subCla = (OntClass) iter.next();

                // display immediate restrictions and subclasses
                if (subCla.isAnon())
                {
                    subStr += NEWLINE + renderClass(subCla);
                }
                else
                {
                    subStr += NEWLINE + term2Link(subCla);

                    // show sub-class hierarchy
                }
                if (subCla.listSubClasses() != null)
                {
                    subStr += getSubClassHierarchy(subCla, 1);
                }
            }

            if (!subStr.equals(""))
            {
                desc += NEWLINE + " <b>Hyponyms (more specific concepts):</b> " +
                    subStr + BREAKLINE;
            }
        }

        // list super-classes
        if ( (!cla.isAnon()) && (cla.listSuperClasses() != null))
        {
            ExtendedIterator iter = cla.listSuperClasses(true);
            String supStr = "";
            while (iter.hasNext())
            {
                OntClass supCla = (OntClass) iter.next();

                // display immediate restrictions and superclasses
                if (supCla.isAnon())
                {
                    supStr += NEWLINE + renderClass(supCla);
                }
                else
                {
                    supStr += NEWLINE + term2Link(supCla);

                    // show super-class hierarchy
                }
                if (supCla.listSuperClasses() != null)
                {
                    supStr += getSuperClassHierarchy(supCla, 1);
                }
            }

            if (!supStr.equals(""))
            {
                desc += NEWLINE + " <b>Hypernyms (more generic concepts):</b> " +
                    supStr + BREAKLINE;
            }
        }

        // list properties
        if ( (!cla.isAnon()) && (cla.listDeclaredProperties() != null))
        {
            ExtendedIterator iter = cla.listDeclaredProperties();
            String propStr = "";
            while (iter.hasNext())
            {
                OntProperty prop = (OntProperty) iter.next();
                propStr += NEWLINE + term2Link(prop);
            }

            if (!propStr.equals(""))
            {
                desc += NEWLINE + " <b>Attributes (properties):</b> " + propStr +
                    BREAKLINE;
            }
        }

        // list instances
        if ( (!cla.isAnon()) && (cla.listInstances() != null))
        {
            ExtendedIterator iter = cla.listInstances();
            String instStr = "";
            while (iter.hasNext())
            {
                Individual inst = (Individual) iter.next();
                instStr += NEWLINE + term2Link(inst);
            }

            if (!instStr.equals(""))
            {
                desc += NEWLINE + " <b>Example Instances:</b> " + instStr +
                    BREAKLINE;
            }
        }

        // definition: restrictions
        if ( (cla.isAnon()) && (cla.isRestriction()))
        {
            Restriction r = cla.asRestriction();
            desc += renderRestriction(r) + ")";
        }

        return desc;
    }

    public String renderProperty(OntProperty prop)
    {
        String desc = "";

        // get property comment
        String comment = "";
        comment = prop.getComment(null);
        if (comment != null)
        {
            desc += NEWLINE + " <b>Description</b>:" + comment + BREAKLINE;

            // get property domain
        }
        if (prop.getDomain() != null)
        {

            String domStr = "";
            for (ExtendedIterator iter = prop.listDomain(); iter.hasNext(); )
            {

                OntResource res = (OntResource) iter.next();

                if (res.canAs(OntClass.class))
                {
                    OntClass cla = (OntClass) res.as(OntClass.class);
                    if (cla.isAnon())
                    {
                        domStr += renderClass(cla);
                    }
                    else
                    {
                        domStr += NEWLINE + term2Link(cla);
                    }
                }
                else
                {
                    domStr += NEWLINE + term2Link(res);
                }
            }
            if (!domStr.equals(""))
            {
                desc += NEWLINE + " <b>Domain: </b>" + domStr;
            }
        }

        // get property range
        if (prop.getRange() != null)
        {

            String rangeStr = "";
            for (ExtendedIterator iter = prop.listRange(); iter.hasNext(); )
            {

                OntResource res = (OntResource) iter.next();

                if (res.canAs(OntClass.class))
                {
                    OntClass cla = (OntClass) res.as(OntClass.class);
                    if (cla.isAnon())
                    {
                        rangeStr += renderClass(cla);
                    }
                    else
                    {
                        rangeStr += NEWLINE + term2Link(cla);
                    }
                }
                else
                {
                    rangeStr += NEWLINE + term2Link(res);
                }
            }
            if (!rangeStr.equals(""))
            {
                desc += NEWLINE + " <b>Range: </b>" + rangeStr;
            }
        }

        // get attributes - functional/inverse-functional/transitive
        String attribStr = "";
        if (prop.isFunctionalProperty())
        {
            attribStr += " <a href=\"If a property, P, is tagged as Functional then for all x, y, and z: P(x,y) and P(x,z) implies y = z \">Functional</a>";
        }
        if (prop.isInverseFunctionalProperty())
        {
            attribStr += " <a href=\"If a property, P, is tagged as Inverse Functional then for all x, y, and z: P(y,x) and P(z,x) implies y = z \">Inverse-Functional</a>";
        }
        if (prop.isTransitiveProperty())
        {
            attribStr += " <a href=\"If a property, P, is tagged as Transitive then for all x, y, and z: P(x,y) and P(y,z) implies P(x,z) \">Transitive</a>";
        }
        if (!attribStr.equals(""))
        {
            desc += NEWLINE + "<b>Attributes:</b>" + attribStr;
        }

        // list equivalent properties
        if (prop.listEquivalentProperties() != null)
        {
            ExtendedIterator iter = prop.listEquivalentProperties();
            String equStr = "";
            while (iter.hasNext())
            {
                OntProperty equProp = (OntProperty) iter.next();
                // skip if same property appears
                if (equProp.getURI() == prop.getURI())
                {
                    continue;
                }

                equStr += NEWLINE + term2Link(equProp);
            }

            if (!equStr.equals(""))
            {
                desc += NEWLINE + " <b>Synonymous Roles:</b> " + equStr +
                    BREAKLINE;
            }
        }

        // list inverse properties
        String invStr = "";
        if (prop.listInverse() != null)
        {
            ExtendedIterator iter = prop.listInverse();
            while (iter.hasNext())
            {
                OntProperty invProp = (OntProperty) iter.next();
                // skip if same property appears
                if (invProp.getURI() == prop.getURI())
                {
                    continue;
                }

                invStr += NEWLINE + term2Link(invProp);
            }
        }
        // & inverse-of properties together
        if (prop.listInverseOf() != null)
        {
            ExtendedIterator iter = prop.listInverseOf();
            while (iter.hasNext())
            {
                OntProperty invProp = (OntProperty) iter.next();
                // skip if same property appears
                if (invProp.getURI() == prop.getURI())
                {
                    continue;
                }

                invStr += NEWLINE + term2Link(invProp);
            }
        }
        if (!invStr.equals(""))
        {
            desc += NEWLINE + " <b>Inverse Roles:</b> " + invStr + BREAKLINE;

            // list sub properties
        }
        if (prop.listSubProperties() != null)
        {

            String subStr = getSubPropertyHierarchy(prop, 0);
            if (!subStr.equals(""))
            {
                desc += NEWLINE + " <b>Hyponyms (more specific roles):</b> " +
                    subStr +
                    BREAKLINE;
            }
        }

        // list super properties
        if (prop.listSuperProperties() != null)
        {

            String supStr = getSuperPropertyHierarchy(prop, 0);
            if (!supStr.equals(""))
            {
                desc += NEWLINE + " <b>Hypernyms (more generic roles):</b> " +
                    supStr +
                    BREAKLINE;
            }
        }

        return desc;
    }

    public String renderInstance(Individual inst)
    {
        Resource cla = (Resource) inst.getRDFType();
        String desc = NEWLINE +
            " instance of class <B>" + term2Link(cla) + "</B>";

        return desc;
    }

    public String renderRestriction(Restriction r)
    {

        // handle restrictions specially since there are different types
        String resStr = "(";

        // get property of restriction
        OntProperty prop = r.getOnProperty();
        String propStr = term2Link(prop);

        // check cardinality restriction
        if (r.isCardinalityRestriction())
        {
            int n = r.asCardinalityRestriction().getCardinality();
            resStr +=
                " (<a href=\"Cardinality of property must be equal to..\">=</a>" +
                String.valueOf(n) + ") " + propStr;
        }

        // check mincardinality restriction
        if (r.isMinCardinalityRestriction())
        {
            int n = r.asMinCardinalityRestriction().getMinCardinality();
            resStr += " (" + GREATEQU + String.valueOf(n) + ") " + propStr;
        }

        // check maxcardinality restriction
        if (r.isMaxCardinalityRestriction())
        {
            int n = r.asMaxCardinalityRestriction().getMaxCardinality();
            resStr += " (" + LESSEQU + String.valueOf(n) + ") " + propStr;
        }

        // check someValuesFrom restriction
        if (r.isSomeValuesFromRestriction())
        {
            RDFNode some = r.asSomeValuesFromRestriction().getSomeValuesFrom();
            resStr += EXISTS + propStr + " " + MEMBEROF + " " +
                renderValue(some);
        }

        // check allValuesFrom restriction
        if (r.isAllValuesFromRestriction())
        {
            RDFNode all = r.asAllValuesFromRestriction().getAllValuesFrom();
            resStr += FORALL + propStr + " " + MEMBEROF + " " + renderValue(all);
        }

        // check hasValue restriction
        if (r.isHasValueRestriction())
        {
            RDFNode has = r.asHasValueRestriction().getHasValue();
            resStr += propStr + " = " + renderValue(has);
        }

        return resStr;
    }

    public String renderValue(RDFNode value)
    {

        String val = "";

        // render value of the restriction

        if (value.canAs(OntClass.class))
        {
            OntClass cla = (OntClass) value.as(OntClass.class);

            if (cla.isAnon())
            {
                // if value is another nested anonymous class, call renderclass again
                val = renderClass(cla);
            }
            else
            {
                // otherwise display class name/uri
                val = term2Link(cla);
            }
        }
        else if (value instanceof Literal)
        {
            // if value is a string literal
            val = ( (Literal) value).getLexicalForm();
        }
        else if (value instanceof Resource)
        {
            Resource r = (Resource) value;
            val = term2Link(r);
        }
        else
        {
            val = value.toString();
        }

        return val;
    }

    public String parseDesc(String desc)
    {

        // parse description string to remove extraneous line breaks
        // repeated triples could be due to imported ontologies' axioms being added previously

        // ***note: bug that causes repeated axioms
        // e.g. wine.owl imports food.owl and vice versa
        // if you save wine.owl to a local file, and read that file into a Jena OntModel,
        // it imports food.owl from the web and then recursively imports wine.owl again
        // from the web leading to double axioms of the wine ontology
        // ***

        String parse = "";
        int chk = 0;
        String[] tokens = desc.split("<br>");
        for (int i = 0; i < tokens.length; i++)
        {
            if (tokens[i].trim().length() > 0)
            {

                if ( (tokens[i].trim().startsWith("<b>Synonyms")) ||
                    (tokens[i].trim().startsWith("<b>Hypernyms")) ||
                    (tokens[i].trim().startsWith("<b>Hyponyms")) ||
                    (tokens[i].trim().startsWith("<b>Inverse")) ||
                    (tokens[i].trim().startsWith("<b>Attributes")) ||
                    (tokens[i].trim().startsWith("<b>Example")))
                {
                    parse += NEWLINE;
                }
                parse += tokens[i].trim() + NEWLINE;
            }
        }
        return parse;
    }

    /** Render a URI */
    public static void renderURI(PrintStream out, PrefixMapping prefixes,
                                 String uri)
    {
        out.print(prefixes.usePrefix(uri));
    }

    private static Map m_anonIDs = new HashMap();
    private static int m_anonCount = 0;

    /** Render an anonymous class or restriction */
    public static void renderAnonymous(PrintStream out, Resource anon,
                                       String name)
    {
        String anonID = (String) m_anonIDs.get(anon.getId());
        if (anonID == null)
        {
            anonID = "a-" + m_anonCount++;
            m_anonIDs.put(anon.getId(), anonID);
        }

        out.print("Anonymous ");
        out.print(name);
        out.print(" with ID ");
        out.print(anonID);
    }

    /**
     * <p>Handle the case of rendering a restriction.</p>
     * @param out The print stream to write to
     * @param r The restriction to render
     */
    protected static void renderRestriction(PrintStream out, Restriction r)
    {
        if (!r.isAnon())
        {
            out.print("Restriction ");
            renderURI(out, r.getModel(), r.getURI());
        }
        else
        {
            renderAnonymous(out, r, "restriction");
        }

        out.print(" on property ");
        renderURI(out, r.getModel(), r.getOnProperty().getURI());
    }

    /** Generate the indentation */
    protected static void indent(PrintStream out, int depth, boolean inHTML)
    {
        String BLANK = inHTML ? "&nbsp;" : " ";
        for (int i = 0; i < depth; i++)
        {
            out.print(BLANK);
        }
    }

    /**
     * <p>Render a description of the given class to the given output stream.</p>
     * @param out A print stream to write to
     * @param c The class to render
     */
    public static void renderClassDescription(PrintStream out, OntClass c, int depth,
                                       boolean inHTML)
    {
        indent(out, depth, inHTML);

        if (c.isRestriction())
        {
            renderRestriction(out, (Restriction) c.as(Restriction.class));
        }
        else
        {
            if (!c.isAnon())
            {
                out.print("Class ");
                TermRenderer.renderURI(out, c.getModel(), c.getURI());
                out.print(' ');
            }
            else
            {
                renderAnonymous(out, c, "class");
            }
        }
    }

}
