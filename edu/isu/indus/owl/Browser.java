package edu.isu.indus.owl;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import edu.iastate.cs.indus.owl.jena.render.OntologyRenderer;

public class Browser
    extends OntologyRenderer
{
    OntModel selOntologyModel;
    String ontURI;

    public Browser()
    {}

    public Browser(String ontURI)
    {
        loadOntology(ontURI);
    }

    public void loadOntology(String ontURI)
    {
        this.ontURI = ontURI;

// create a Jena model
        try
        {
            selOntologyModel = ModelFactory.createOntologyModel(); // default is OWL
            selOntologyModel.read(ontURI);

            // add all ontologies in this model to the GlobalOntologyModel Hashtable
            // key: ontURI, value: Jena Ontology Model
            ExtendedIterator iter = selOntologyModel.listOntologies();
            if (iter.hasNext())
            {
                // if model has explicit ontology elements listed
                while (iter.hasNext())
                {
                    Ontology ont = (Ontology) iter.next();
                    ontURI = ont.getURI();

                    if (AllOntologies.containsKey(ontURI))
                    {
                        continue; // suppose newly added ontology imports one that is already present in our KB
                    }

                    AllOntologies.put(ontURI, selOntologyModel);

                    // add ontology name/uri to array lists
                    String ontName = ontURI.substring(ontURI.lastIndexOf("/") +
                        1, ontURI.length());
                    RegisterOntology(ontName, ontURI);
                }
            }
            else
            {
                // if there are no explicit ontology elements in the model
                // check if already existing in KB
                if (AllOntologies.containsKey(ontURI))
                {
                    return;
                }

                AllOntologies.put(ontURI, selOntologyModel);

                // add ontology name/uri to array lists
                String ontName = ontURI.substring(ontURI.lastIndexOf("/") + 1,
                                                  ontURI.length());
                RegisterOntology(ontName, ontURI);
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return;

    }

    /**
     *  List all triples in the model
     */
    public String listTriple()
    {
        if (selOntologyModel == null)
        {
            System.out.println("No ontology defined");
            return "";
        }

        StringBuffer buf = new StringBuffer();
        buf.append("<TABLE>");
        buf.append("<TR>" +
                   "<TD><B>Subject</B></TD>" +
                   "<TD><B>Predicate</B></TD>" +
                   "<TD><B>Object</B></TD>" +
                   "</TR>");

        // list the statements in the graph
        StmtIterator iter = selOntologyModel.listStatements();
        int count = 0;

        // print out the predicate, subject and object of each statement
        while (iter.hasNext())
        {
            count++;
            buf.append("<TR>");

            Statement stmt = iter.nextStatement(); // get next statement
            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object

            buf.append("<TD>" + term2Link(subject) + "</TD>");
            buf.append("<TD>" + term2Link(predicate) + "<TD>");
            if (object instanceof Resource)
            {
                buf.append("<TD>" + term2Link( (Resource) object) + "<TD>");
            }
            else
            {
                // object is a literal
                buf.append("<TD>\"" + object.toString() + "\"</TD>");
            }
            buf.append(NEWLINE);
            buf.append("</TR>");
        }
        buf.append("</TABLE>");

        buf.append("Number of triples: " + count + NEWLINE);

        return buf.toString();
    }

    protected void RegisterOntology(String ontName, String ontURI)
    {

        // add ontology name/uri to respective array lists and sort them alphabetically
        // and refresh UI display
        if (ontListArray.size() == 0)
        {
            ontListArray.add(ontName);
            ontListMirrorURIArray.add(ontURI);
        }
        else
        {
            // determine index at which to insert new ontName (sorted alphabetically)
            int insertIndex = ontListArray.size();
            for (int i = ontListArray.size() - 1; i >= 0; i--)
            {
                String chk = (String) ontListArray.get(i);
                if (chk.toLowerCase().compareTo(ontName.toLowerCase()) > 0)
                {
                    insertIndex--;
                }
                else
                {
                    break;
                }
            }
            ontListArray.add("temp");
            for (int i = ontListArray.size() - 1; i > insertIndex; i--)
            {
                ontListArray.set(i, ontListArray.get(i - 1));
            }
            ontListArray.set(insertIndex, ontName);
        }
    }

    public String RenderOntologyInfo()
    {
        if (selOntologyModel == null)
        {
            System.out.println("No ontology defined");
            return "";
        }

        String singleDesc = displayOntologyDescription(selOntologyModel, ontURI);

        //System.out.println(singleDesc);
        return singleDesc;
    }

    public String renderTerm(String term)
    {
        return renderTerm(selOntologyModel, term);
    }

    public String listTerms()
    {
        if (selOntologyModel == null)
        {
            return "";
        }
        return
            displayOntologyTermList(selOntologyModel, ontURI) + NEWLINE +
            displayClassTree(selOntologyModel, ontURI) + NEWLINE +
            displayPropertyTree(selOntologyModel, ontURI);

    }

    protected void saveOntologyKB()
    {
        // save Ontology KB
        try
        {
            // save global ontologies to a local directory
            // save keys in a separate text file
            String keyStr = "";
            Iterator iter = AllOntologies.keySet().iterator();
            Set ModelsSaved = new HashSet();
            while (iter.hasNext())
            {

                // for each key, get ontology model
                String ontURI = (String) iter.next();
                String ontName = ontURI.substring(ontURI.lastIndexOf("/") + 1,
                                                  ontURI.length());

                // write ontology model to local file
                OntModel model = (OntModel) AllOntologies.get(ontURI);

                // check for duplicate models being stored (via imports)
                if (ModelsSaved.contains(model))
                {
                    continue;
                }
                else
                {
                    ModelsSaved.add(model);

                }
                FileOutputStream fos = new FileOutputStream("localOnt/" +
                    ontName);
                model.write(fos);
                fos.close();

                // generate key string: key,local file (line pairs)
                keyStr += ontURI + NEWLINE; // + local file name
            }
            if (!keyStr.equals(""))
            {
                FileWriter out = new FileWriter("localOnt/OntologyKB.txt");
                out.write(keyStr);
                out.close();
            }

            System.out.println("Ontology KB Saved Successfully");
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        //Browser b = new Browser("file:///F:\\0-Me\\8-Programming\\0-Java\\7-Ontology\\JSPWiki\\1.0\\localOnt\\wine.owl");
        Browser b = new Browser("http://www.mindswap.org/~aditkal/wine.owl");

        JFrame frame = new JFrame();
        frame.setSize(800, 600);

        JEditorPane pane = new JEditorPane();
        frame.getContentPane().add(new JScrollPane(pane));

        //b.listTriple();
        pane.setContentType("text/plain");
        pane.setText(b.RenderOntologyInfo() +
                     b.listTerms());

        frame.setVisible(true);
    }

}
