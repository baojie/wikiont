package edu.iastate.cs.indus.owl.jena.render;

import java.awt.HeadlessException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ListIterator;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import edu.iastate.cs.indus.owl.hierarchy.SubClassHierarchy;
import edu.isu.indus.owl.Hierarchy;

public class OntologyRenderer
    extends TermRenderer
{
    public OntologyRenderer() throws HeadlessException
    {
        super();
        ontListArray = new ArrayList();
        ontListMirrorURIArray = new ArrayList();
        termListArray = new ArrayList();
        termListMirrorURIArray = new ArrayList();
        AllOntologies = new Hashtable();
    }

    public ArrayList ontListArray, ontListMirrorURIArray;
    public ArrayList termListArray, termListMirrorURIArray;

    public void addTermToList(String termName, String termURI, String type)
    {
        // add termology name/uri to respective array lists
        // insert term in arrays
        if (type == "Class")
        {
            termListArray.add(termName + " [C]");
        }
        else if (type == "Property")
        {
            termListArray.add(termName + " [P]");
        }
        else if (type == "Instance")
        {
            termListArray.add(termName + " [I]");
        }

        termListMirrorURIArray.add(termURI);
    }

    /**
     *
     * @param model OntModel
     * @param ontURI String
     * @return String
     *
     * @since 2003-03-03
     */
    public String displayClassTree(OntModel model, String ontURI)
    {
        String desc = "";

        try
        {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            PrintStream stream = new PrintStream(buffer);
            SubClassHierarchy.showHierarchy(stream, model, true);
            stream.flush();
            buffer.flush();
            desc = buffer.toString();
        }
        catch (IOException ex)
        {
        }

        return desc;
    }

    /**
     *
     * @param model OntModel
     * @param ontURI String
     * @return String
     *
     * @since 2003-03-04
     */
    public String displayPropertyTree(OntModel model, String ontURI)
    {

        Hierarchy h = new Hierarchy();

        String desc = "";

        // display classes
        ExtendedIterator iter = (ExtendedIterator) model.listOntProperties();

        while (iter.hasNext())
        {
            OntProperty prop = (OntProperty) iter.next();

            String name = prop.getLocalName();
            String uri = prop.getURI();

            // add class to term list (alphabetically sorted)
            if ( (name != null) && (uri != null))
            {
                // skip those classes that don't belong to this ontology (i.e. from the imported ontology)
                //if (inDomain(prop))
                {
                    // get superclass
                    String hier = ""; // getSuperClassHierarchy(cla, 1);

                    ExtendedIterator iter1 = prop.listSuperProperties(true);
                    while (iter1.hasNext())
                    {

                        OntProperty sup = (OntProperty) iter1.next();

                        if ( /*sup.isAnon() ||*/!inDomain(sup))
                        {
                            continue; // if superclass has restriction, don't display
                        }

                        h.addTriple(uri, "rdfs:subClassOf", sup.getURI());
                        //hier += "..." + term2Link(supCla) + BREAKLINE;
                    }

                    //desc += term2Link(cla) + BREAKLINE + hier + BREAKLINE;
                }
            }
        }

        h.fromTriple();
        desc = h.getTree().toString(true) +
            "Number of properties: " + h.getTreeSize(); ;

        return desc;

    }

    public String displayOntologyTermList(OntModel model, String ontURI)
    {
        String desc = "";
        termListArray.removeAll(termListArray);
        termListMirrorURIArray.removeAll(termListMirrorURIArray);

        // display classes in Term List
        ExtendedIterator iter = (ExtendedIterator) model.listClasses();

        while (iter.hasNext())
        {
            OntClass cla = (OntClass) iter.next();

            String claName = cla.getLocalName();
            String claURI = cla.getURI();

            // add class to term list (alphabetically sorted)
            if ( (claName != null) && (claURI != null))
            {
                // skip those classes that don't belong to this ontology (i.e. from the imported ontology)
                if (inDomain(cla))
                {
                    addTermToList(claName, claURI, "Class");
                }
            }
        }

        int numClass = termListArray.size();

        // display properties in Term List
        iter = (ExtendedIterator) model.listOntProperties();
        while (iter.hasNext())
        {
            OntProperty prop = (OntProperty) iter.next();
            String propName = prop.getLocalName();
            String propURI = prop.getURI();

            // skip those properties that don't belong to this ontology (i.e. from the imported ontology)
            if (inDomain(prop))
            {
                addTermToList(propName, propURI, "Property");
            }

        }

        int numProp = termListArray.size() - numClass;

        // display instances in Term List
        iter = (ExtendedIterator) model.listIndividuals();
        while (iter.hasNext())
        {
            Individual inst = (Individual) iter.next();
            String propName = inst.getLocalName();
            String propURI = inst.getURI();

            // skip those properties that don't belong to this ontology (i.e. from the imported ontology)
            if (inDomain(inst))
            {
                addTermToList(propName, propURI, "Instance");
            }

        }

        int numInstance = termListArray.size() - numClass - numProp;

        desc += BREAKLINE + BREAKLINE +
            TermList2links(termListArray,
                           termListMirrorURIArray).replaceAll("#", "!!!!") +
            BREAKLINE + "Number of classes: " + numClass
            + ", Number of Properties: " + numProp
            + ", Number of Instances: " + numInstance
            + ", Total: " + (numClass + numProp + numInstance);

        return desc;
    }

    public String displayOntologyDescription(OntModel model, String ontURI)
    {

        String desc = "";

        // add information about current ontology to list/description panes
        Ontology currentOnt = model.getOntology(ontURI);
        String ontName = "";

        if (currentOnt != null)
        {
            ontName = currentOnt.getLocalName();
            String ontLbl = currentOnt.getLabel(null);
            String ontComment = currentOnt.getComment(null);
            desc += "<b>Ontology '" + ontName + "' Description: </b>";
            if (ontLbl != null)
            {
                desc += ontLbl;
            }
            if ( (ontLbl != null) && (ontComment != null))
            {
                desc += " - ";
            }
            if (ontComment != null)
            {
                desc += ontComment;
            }
            desc += NEWLINE;

            // imports ontology
            if (currentOnt.getImport() != null)
            {
                ExtendedIterator iter = currentOnt.listImports();
                String impStr = "";
                while (iter.hasNext())
                {
                    OntResource res = (OntResource) iter.next();
                    impStr += NEWLINE + term2Link(res);
                }
                if (!impStr.equals(""))
                {
                    desc += NEWLINE + " <b>Imports:</b> " + impStr + NEWLINE;
                }
            }

        }
        else
        {
            // no specific ontology element was defined in the doc
            ontName = ontURI.substring(ontURI.lastIndexOf("/") + 1,
                                       ontURI.length());
            desc += "<b>Ontology '" + ontName + "'</b>";
        }

        desc += BREAKLINE + BREAKLINE;

        return desc;
    }

    /**
     * Array2List
     *
     * @param termListArray ArrayList
     * @return String
     * @author Jie Bao
     * @version 2004-03-02
     */
    private String TermList2links(ArrayList termListArray,
                                  ArrayList termListMirrorURIArray)
    {
        if (termListMirrorURIArray.size() != termListArray.size())
        {
            return "";
        }
        StringBuffer str = new StringBuffer();

        ListIterator iter = (ListIterator) termListArray.
            listIterator();
        ListIterator iterMirror = (ListIterator) termListMirrorURIArray.
            listIterator();
        while (iter.hasNext())
        {
            String term = (String) iter.next();
            String termURI = (String) iterMirror.next();
            String item = NEWLINE + "<a href=\"" + EDITOR +
                termURI + "\">" + term + "</a>";
            str.append(item);
        }

        /*     Simple text representation
                String str = termListArray.toString();
                str = str.substring(1, str.length() - 1);
                str = str.replaceAll(",", NEWLINE);
         */
        String s = str.toString();
        //s = s.replaceAll("#", "!!!!");
        return s;
    }

    /**
     * Display all terms
     *
     * @return String
     */
    public String renderTerm(OntModel selOntologyModel, String term)
    {
        //System.out.println(termListMirrorURIArray);
        if (selOntologyModel == null)
        {
            return "";
        }
        StringBuffer buf = new StringBuffer();

        // display classes in Term List
        if (term == null)
        {
            ListIterator iter = (ListIterator) termListMirrorURIArray.
                listIterator();
            while (iter.hasNext())
            {
                String termURI = (String) iter.next();
                buf.append(displayTermInfo(selOntologyModel, termURI) + NEWLINE);
            }
        }
        else
        {
            term = term.replaceAll("!!!!", "#"); // See OntologyRenderer. "#" in link has been replaced by "!!!!"
            buf.append(displayTermInfo(selOntologyModel, term) + NEWLINE);
        }
        return buf.toString();

    }

    protected void removeOntology(String ontURI)
    {
        // remove ontmodel from hashtable
        AllOntologies.remove(ontURI);
    }

}
