package edu.iastate.anthill.indus.owl.jena;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import edu.iastate.anthill.indus.owl.jena.util.JenaUtil;

/**
 * Agent to use jena ontology model
 *
 * @author Jie Bao
 * @since 1.0
 */
public class JenaModel
{
    public static String FULLXML = "RDF/XML";
    public static String SHORTXML = "RDF/XML-ABBREV";
    public static String N_TRIPLE = "N-TRIPLE";
    public static String N3 = "N3";
    protected String ontURI = "http://www.foo.com/foo#";
    protected String NsPrefix = "Foo";
    protected String writeFormat = FULLXML;

    protected boolean modified = true;

    // create an empty model
    protected static OntModel model = ModelFactory.createOntologyModel(
        OntModelSpec.OWL_MEM, null);

    // create a new model
    public JenaModel(String uri, String NsPrefix)
    {
        ontURI = uri;
        model.createOntology(uri);

        setNsPrefix(NsPrefix);
        modified = true;
    }

    // read a model from owl file
    public JenaModel(File file)
    {
        try
        {
            readOntology(new FileInputStream(file));

            // set uri and prefix
            Map m = model.getNsPrefixMap();
            if (m != null)
            {
                ontURI = (String) m.get(""); // the base uri
                if (ontURI != null)
                {
                    setNsPrefix(model.getNsURIPrefix(ontURI)); // the base prefix
                }
            }
            modified = false;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param className String
     * @return OntClass
     *
     * @author Jie Bao
     * @version 2003-03-05
     */
    public OntClass addClass(String localName)
    {
        String uri = ontURI + localName;
        OntClass res = model.createClass(uri);
        modified = true;
        return res;
    }

    public OntClass getClass(String localName)
    {
        String uri = ontURI + localName;
        OntClass res = model.getOntClass(uri);
        return res;
    }

    /**
     * Create a property
     *
     * @param propertyName String
     * @return Property
     *
     * @author Jie Bao
     * @version 2003-04-22
     */
    public OntProperty addProperty(String localName)
    {
        String uri = ontURI + localName;
        OntProperty res = model.createOntProperty(uri);
        modified = true;
        return res;
    }

    public Property getProperty(String propertyName)
    {
        String uri = ontURI + propertyName;
        Property res = model.getProperty(uri);
        return res;
    }

    /**
     * Create an Instance given a class
     *
     * @param instanceName String
     * @param className OntClass
     * @return Individual
     *
     * @author Jie Bao
     * @version 2003-04-22
     */
    public Individual addInstance(String localName, OntClass className)
    {
        String uri = ontURI + localName;
        Individual res = model.createIndividual(uri, className);
        modified = true;
        return res;
    }

    public Individual getInstance(String localName)
    {
        String uri = ontURI + localName;
        Individual res = model.getIndividual(uri);
        return res;
    }

    /**
     * List all triples in the model
     */
    public String listTriple(boolean thisDomainOnly)
    {
        StringBuffer buf = new StringBuffer();

        // list the statements in the graph
        StmtIterator iter = model.listStatements();

        // print out the predicate, subject and object of each statement
        while (iter.hasNext())
        {
            Statement stmt = iter.nextStatement(); // get next statement
            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object

            // if the trip is not in this domian, skip
            System.out.println(stmt);
            if (thisDomainOnly && subject != null)
            {
                if (subject.getNameSpace().compareToIgnoreCase(ontURI) !=
                    0)
                {
                    continue;
                }
            }

            //buf.append(subject.getLocalName());
            if (subject != null)
            {
                //buf.append ( model.usePrefix ( subject.getURI () ) ) ;
                buf.append(" " + subject.getLocalName() + " ");
            }
            if (predicate != null)
            {
                buf.append(" " + predicate.getLocalName() + " ");
                //buf.append ( " " + model.usePrefix ( predicate.getURI () ) + " " ) ;
            }
            if (object != null)
            {
                if (object instanceof Resource)
                {
                    buf.append( ( (Resource) object).getLocalName());
                    //buf.append ( model.usePrefix ( ( ( Resource ) object ).getURI () ) ) ;
                }
                else
                {
                    // object is a literal
                    buf.append(" \"" + object.toString() + "\"");
                }
            }

            buf.append(" \n");
        }
        return buf.toString();
    }

    /**
     * write the model in XML form to a file
     * writeFormat could be set afore
     *
     * @param out OutputStream
     */
    public void writeOntology(OutputStream out)
    {
        model.write(out, writeFormat);
        modified = false;
    }

    // 2004-04-26 Jie Bao
    public void writeOntology(File file)
    {
        try
        {
            model.write(new FileOutputStream(file), writeFormat);
            modified = false;
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * get the OWL text of the ontology
     * @return String
     */
    public String getOWLOntology()
    {
        try
        {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            writeOntology(buf);
            buf.flush();
            return buf.toString();
        }
        catch (IOException ex)
        {
            return null;
        }
    }

    public void readOntology(InputStream in)
    {
        // read the RDF/XML file
        model.read(new InputStreamReader(in), "");
        modified = true;
    }

    /**
     * rename a resource
     *
     * @param old Resource
     * @param newName String
     * @return Resource
     *
     * @version 2004-04-22
     */
    public OntResource renameResource(Resource old, String newName)
    {
        String uri = ontURI + newName;
        modified = true;
        return JenaUtil.renameResource(old, uri);
    }

    public OntResource renameResource(String localName, String newLocalName)
    {
        Resource old = model.getResource(ontURI + localName);
        modified = true;
        return renameResource(old, newLocalName);
    }

    /**
     * deleteResource
     *
     * @param object Object
     *
     * @author Jie Bao
     * @version 2004-04-23
     */
    public void deleteResource(String localName)
    {
        Resource old = model.getResource(ontURI + "#" + localName);

        List stmts = new ArrayList();

        // list the statements that mention old as a subject
        for (Iterator i = old.listProperties(); i.hasNext();
             stmts.add(i.next()))
        {
            ;
        }

        // list the statements that mention old an an object
        for (Iterator i = model.listStatements(null, null, old);
             i.hasNext();
             stmts.add(i.next()))
        {
            ;
        }

        // now move the statements to refer to res instead of old
        for (Iterator i = stmts.iterator(); i.hasNext(); )
        {
            Statement s = (Statement) i.next();
            s.remove();
        }
        modified = true;
    }

    public String getOntURI()
    {
        return ontURI;
    }

    public void setOntURI(String ontURI)
    {
        this.ontURI = ontURI;
        model.setNsPrefix(NsPrefix, ontURI);
        modified = true;
    }

    public void setNsPrefix(String NsPrefix)
    {
        this.NsPrefix = NsPrefix;
        model.setNsPrefix(NsPrefix, ontURI);
        modified = true;
    }

    public String getNsPrefix()
    {
        return NsPrefix;
    }

    public void setWriteFormat(String writeFormat)
    {
        this.writeFormat = writeFormat;
        modified = true;
    }

    public String getWriteFormat()
    {
        return writeFormat;
    }

    public boolean isModified()
    {
        return modified;
    }

    /**
     * createOntology
     *
     * @param inPackageName String
     * @return OntResource
     * @since 2004-05-01
     */
    public OntResource createOntology(String urlbase)
    {
        modified = true;
        return model.createOntology(urlbase);
    }

    public OntResource getResource(String uri)
    {
        return JenaUtil.asOntResource(model.getResource(uri));
    }

    public OntModel getModel()
    {
        return model;
    }

    public boolean inDomain(Resource res)
    {
        try
        {
            String uri = res.getURI();
            return uri.startsWith(ontURI);
        }
        catch (Exception ex)
        {
            return false;
        }
    }

}
