package edu.iastate.cs.indus.owl.jena.render;

import java.util.Hashtable;

import com.hp.hpl.jena.rdf.model.Resource;

public class OntologyConstants
{
    public static String NEWLINE = "<BR>\n";
    public static String BREAKLINE = "<BR>\n";
    public static String RDFS = "http://www.w3.org/2000/01/rdf-schema#";
    public static String RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static String OWL = "http://www.w3.org/2002/07/owl#";

// DL operators
    public static String FORALL = "<font face=\"Symbol\" SIZE=3><a href=\"All values (if present) of this property..\">&#8704;</a></font><font face=\"Verdana\" SIZE=2>";
    public static String EXISTS = "<font face=\"Symbol\" SIZE=3><a href=\"Some values (atleast one) of this property..\">&#8707;</a></font><font face=\"Verdana\" SIZE=2>";
    public static String MEMBEROF = "<font face=\"Symbol\" SIZE=3><a href=\"..must belong to a member of class..\">&#8712;</a></font><font face=\"Verdana\" SIZE=2>";
    public static String GREATEQU = "<font face=\"Symbol\" SIZE=3><a href=\"Cardinality of property must be greater than or equal to..\">&#8805;</a></font><font face=\"Verdana\" SIZE=2>";
    public static String LESSEQU = "<font face=\"Symbol\" SIZE=3><a href=\"Cardinality of property must be lesser than or equal to..\">&#8804;</a></font><font face=\"Verdana\" SIZE=2>";
    public static String EQUIVALENT =
        "<font face=\"Symbol\" SIZE=3>&#8801;</font><font face=\"Verdana\" SIZE=2>";
    public static String INTERSECTION =
        "<font face=\"Symbol\" SIZE=3>&#8745;</font><font face=\"Verdana\" SIZE=2>";
    public static String UNION =
        "<font face=\"Symbol\" SIZE=3>&#8746;</font><font face=\"Verdana\" SIZE=2>";
    public static String COMPLEMENT =
        "<font face=\"Symbol\" SIZE=3>&#172;</font><font face=\"Verdana\" SIZE=2>";
    public static String SUBSET =
        "<font face=\"Symbol\" SIZE=3>&#8734;</font><font face=\"Verdana\" SIZE=2>";
    public static String SUPERSET =
        "<font face=\"Symbol\" SIZE=3>&#8735;</font><font face=\"Verdana\" SIZE=2>";

    public static String EDITOR = "instance.jsp?term=";

    public Hashtable AllOntologies;

    // supportive functions
    public static String term2Link(Resource res)
    {
        if (inDomain(res))
        {
            return "<a href=\"" + EDITOR +
                res.getURI().replaceAll("#", "!!!!") +
                "\">" + res.getLocalName() + "</a>";
        }
        else
        {
            return res.getLocalName();
        }
    }

    public static boolean inDomain(Resource res)
    {
        try
        {
            String uri = res.getURI();
            return! (uri.startsWith(RDFS) ||
                     uri.startsWith(RDF) ||
                     uri.startsWith(OWL));
        }
        catch (Exception ex)
        {
            return false;
        }
    }

}
