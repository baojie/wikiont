package edu.iastate.cs.indus.owl.jena.util;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.tidy.Checker;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.ModelLoader;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import edu.iastate.cs.indus.owl.jena.OWLMeta;
import edu.iastate.cs.indus.owl.jena.OntModelProvider;

/**
 * A collection of static utility methods for Jena.
 *
 * @author Holger Knublauch  <holger@smi.stanford.edu>
 * @ Jie Bao <baojie@cs.iastate.edu>
 *     fixed some version problem of jena 2004-04-30
 */
public class JenaUtil
{

    static void addRDFTypes(Hashtable table, OntProperty property)
    {
        List types = new ArrayList();
        for (Iterator it = property.listRDFTypes(true); it.hasNext(); )
        {
            Resource type = (Resource) it.next();
            types.add(type);
        }
        table.put(property, types);
    }

    /**
     *
     * @param resource Resource
     * @return OntResource
     *
     * @author Jie Bao
     * @since 2004-05-01
     */
    public static OntResource asOntResource(Resource resource)
    {
        if (resource.canAs(OntResource.class))
        {
            return (OntResource) resource.as(OntResource.class);
        }
        else
        {
            OntModel ontModel = (OntModel) resource.getModel();
            OntResource res = ontModel.createOntResource(
                OntResource.class, ontModel.getProfile().NIL(), resource.getURI());
            if (res == null)
            {
                throw new IllegalArgumentException(
                    "Could not create OntResource");
            }
            return res;
        }
    }

    /**
     * Checks whether a given OntResource has a certain rdf:type, or a subclass thereof.
     * This is a work-around for the lack of subsumption reasoning in Jena's default model.
     * @param resource
     * @param type
     * @return
     */
    public static boolean canAs(RDFNode resource, Resource type)
    {
        if (resource.canAs(OntResource.class))
        {
            final OntResource ontResource = (OntResource) resource.as(
                OntResource.class);
            if (ontResource.hasRDFType(type))
            {
                return true;
            }
            if (type.canAs(OntClass.class))
            {
                OntClass c = (OntClass) type.as(OntClass.class);
                for (Iterator it = c.listSubClasses(true); it.hasNext(); )
                {
                    Resource superClass = (Resource) it.next();
                    if (canAs(resource, (OntClass) superClass.as(OntClass.class)))
                    {
                        return true;
                    }
                }
            }
            else if (ontResource.getModel() instanceof OntModel)
            {
                OntModel ontModel = (OntModel) ontResource.getModel();
                return canAs(ontResource, ontModel.getResource(type.getURI()));
            }
        }
        return false;
    }



    public static OntModel cloneOntModel(OntModel oldModel)
    {
        OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);
        spec.setReasoner(null);
        return cloneOntModel(oldModel, spec);
    }

    public static OntModel cloneOntModel(OntModel oldModel, OntModelSpec spec)
    {
        String ns = oldModel.getNsPrefixURI("");

        StringWriter stringWriter = new StringWriter();
        RDFWriter writer = oldModel.getWriter(ModelLoader.langXMLAbbrev);
        writer.setProperty("blockRules", "propertyAttr");
        writer.setProperty("relativeURIs", "same-document");
        //Jena.dumpRDF(oldModel);
        writer.write(oldModel.getBaseModel(), stringWriter, ns);
        try
        {
            stringWriter.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        String buffer = stringWriter.toString();

        /*
                 try {
            File file = new File("c:\\test.owl");
            OutputStream outputStream = new FileOutputStream(file);
            PrintStream ps = new PrintStream(outputStream);
            ps.println(buffer);
            ps.close();
                 }
                 catch (Exception ex) {
            ex.printStackTrace();
                 } */

        OntModel newModel = ModelFactory.createOntologyModel(spec, null);
        InputStream inputStream = new StringBufferInputStream(buffer);
        newModel.read(inputStream, ns, ModelLoader.langXMLAbbrev);
        try
        {
            inputStream.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return newModel;
    }



    public static void dumpRDF(OntModel ontModel)
    {
        String language = ModelLoader.langXMLAbbrev;
        String namespace = ontModel.getNsPrefixURI("");
        RDFWriter writer = ontModel.getWriter(language);
        JenaUtil.prepareWriter(writer, language, namespace);
        writer.write(ontModel.getBaseModel(), System.out, namespace);
    }


    /**
     * Gets the Graph where the rdf:type statement of a certain resource is.
     * @param ontModel  the OntModel
     * @param resource  the Resource to look up
     * @return a sub graph of ontModel
     */
    public static Graph getHomeGraph(OntModel ontModel, OntResource resource)
    {
        Property predicate = RDF.type;
        Resource object = resource.getRDFType();
        for (Iterator it = ontModel.getSubGraphs().iterator(); it.hasNext(); )
        {
            Graph graph = (Graph) it.next();
            if (graph.contains(resource.getNode(), predicate.getNode(),
                               object.getNode()))
            {
                return graph;
            }
        }
        return ontModel.getGraph();
    }

    public static int getOWLSpecies(OntModel ontModel)
    {
        Checker checker = new Checker(false);
        checker.add(ontModel.getGraph());
        //checker.add(ontModel);
        String sublanguage = checker.getSubLanguage();
        if (sublanguage.equalsIgnoreCase("Full"))
        {
            return OntModelProvider.OWL_FULL;
        }
        else if (sublanguage.equalsIgnoreCase("DL"))
        {
            return OntModelProvider.OWL_DL;
        }
        else
        {
            return OntModelProvider.OWL_LITE;
        }
    }

    public static String getOWLSpeciesString(int x)
    {
        if (x == OntModelProvider.OWL_DL)
        {
            return "DL";
        }
        else if (x == OntModelProvider.OWL_LITE)
        {
            return "Lite";
        }
        else if (x == OntModelProvider.OWL_FULL)
        {
            return "Full";
        }
        throw new IllegalArgumentException(
            "Species constant must be on of OntModelProvider.OWL_xxx");
    }


    public static boolean isImportedResource(OntModel ontModel,
                                             OntResource ontResource)
    {
        Resource subject = ontResource;
        Property predicate = RDF.type;
        Resource object = ontResource.getRDFType();
        if (object != null)
        {
            if (!ontModel.getBaseModel().contains(subject, predicate, object))
            {
                return true;
            }
        }
        return false;
    }



    public static boolean isSystemResource(Resource ontResource)
    {
        String nameSpace = ontResource.getNameSpace();
        return (nameSpace.equals(OWLMeta.NS) &&
                !OWLMeta.SLOT_VALUES_COMPUTER.equals(ontResource.getLocalName())) ||
            nameSpace.equals(OWL.NAMESPACE) ||
            nameSpace.equals(RDFS.getURI()) ||
            nameSpace.equals(RDF.getURI());
    }


    public static void prepareWriter(RDFWriter writer, String language,
                                     String namespace)
    {
        if (ModelLoader.langXMLAbbrev.equals(language))
        {
            writer.setProperty("showXmlDeclaration", "true");
            writer.setProperty("blockRules", "propertyAttr");
            writer.setProperty("relativeURIs", "same-document");
            String xmlbase = namespace;
            if (xmlbase.endsWith("#"))
            {
                xmlbase = xmlbase.substring(0, xmlbase.length() - 1);
            }
            writer.setProperty("xmlbase", xmlbase);
        }
    }

    /**
     *
     * @param old Resource
     * @param uri String
     * @return OntResource
     *
     * @author Jie Bao
     * @since 2004-04-30
     */
    public static OntResource renameResource(Resource old, String uri)
    {

        OntModel homeModel = (OntModel) old.getModel();

        // Create a new resource to replace old

        OntResource res;

        Class cls = OntResource.class;
        //= (uri == null) ? homeModel.createResource() :            homeModel.createResource(uri);
        if (old instanceof OntClass)
        {
            System.out.print("Renaming a class ");
            cls = OntClass.class;
            res = homeModel.createClass(uri);
        }
        else if (old instanceof OntProperty)
        {
            System.out.print("Renaming a property ");
            cls = OntProperty.class;
            res = homeModel.createOntProperty(uri);
        }
        else if (old instanceof Individual)
        {
            cls = Individual.class;
            System.out.print("Renaming an instance ");
            res = homeModel.createIndividual(uri,
                                             ( (Individual) old).getRDFType());

        }
        else
        {
            System.out.print("Renaming a generic OntResource ");
            res = homeModel.createOntResource(cls, homeModel.getProfile().NIL(),
                                              uri);
        }
        System.out.println(old.getURI() + " -> " + uri);

        Model m = homeModel.getBaseModel();
        renameResourceInModel(m, old, res);

        return res;
    }

    public static Resource renameResource(Resource old, String uri,
                                          Model owlFullModel)
    {

        OntModel homeModel = (OntModel) old.getModel();

        // Create a new resource to replace old
        Resource res = (uri == null) ? homeModel.createResource() :
            homeModel.createResource(uri);

        Model m = homeModel.getBaseModel();
        renameResourceInModel(m, old, res);
        renameResourceInModel(owlFullModel, old, res);

        return res;
    }

    public static void renameResourceInModel(Model m, Resource old,
                                             Resource newResource)
    {
        List stmts = new ArrayList();

        // Add the statements that mention old as a subject
        for (Iterator i = m.listStatements(old, null, (RDFNode)null); i.hasNext(); )
        {
            stmts.add(i.next());
        }

        // Add the statements that mention old an an object
        for (Iterator i = m.listStatements(null, null, old); i.hasNext(); )
        {
            stmts.add(i.next());
        }

        // now move the statements to refer to newResource instead of old
        for (Iterator i = stmts.iterator(); i.hasNext(); )
        {
            Statement s = (Statement) i.next();
            s.remove();
            Resource subj = s.getSubject().equals(old) ? newResource :
                s.getSubject();
            RDFNode obj = s.getObject().equals(old) ? newResource : s.getObject();
            m.add(subj, s.getPredicate(), obj);
        }
    }

    /**
     * Converts an Iterator (e.g. those delivered by the Jena listXXX methods) into a List.
     * @param it  the Iterator to convert
     * @return a List with the same elements
     */
    public static Set set(Iterator it)
    {
        Set result = new HashSet();
        while (it.hasNext())
        {
            result.add(it.next());
        }
        return result;
    }

    /**
     * Converts an Iterator (e.g. those delivered by the Jena listXXX methods) into a List.
     * @param it  the Iterator to convert
     */
    public static void set(Set set, Iterator it)
    {
        while (it.hasNext())
        {
            set.add(it.next());
        }
    }



    // for test
    public static void main(String arg[])
    {
        OntModel model = ModelFactory.createOntologyModel();
        //       OntClass cls = model.createClass("http://nothing.com/indus#Old");
        //        renameResource(cls,"http://nothing.com/indus#New");
        model.createOntResource(OntClass.class, model.getProfile().CLASS(),
                                "http://nothing.com/indus#Old");
        model.write(System.out);

    }
}
