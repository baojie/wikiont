package edu.iastate.anthill.wikiont;

import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.iastate.anthill.indus.owl.jena.JenaModel;
import edu.iastate.anthill.utils.io.FileUtils;

/**
 *  Data structure for wiki pages
 *
 * @author Jie Bao
 * @since 1.0 2004-07-10
 */
public class WikiPage
{
    Hashtable terms;
    Hashtable referenceTable;

    Map prefixeMapping;

    JenaModel model;
    String localPrefix;

    private static final String PREFIX_LIMITATOR = "..";
    private static final String DEFAULT_NAME = "AnonymousClass";

    String BREAKLINE = "\\\\\n";

    public WikiPage(JenaModel model, String localPrefix)
    {
        this.model = model;
        this.localPrefix = localPrefix;
        prefixeMapping = model.getModel().getNsPrefixMap();

        createPagesFromModel();
    }

    /**
     * createPagesFromModel
     *
     * @author Jie Bao
     * @version 2004-07-10
     */
    private void createPagesFromModel()
    {
        try
        {
            // get the model in N Triple
            String oldfmt = model.getWriteFormat();
            model.setWriteFormat(model.N_TRIPLE);
            String owlont = model.getOWLOntology();
            model.setWriteFormat(oldfmt);

            // parse it
            owlont = owlont.replaceAll("<", "[");
            owlont = owlont.replaceAll(">", "]");

            // rename all anonymous classes
            //System.out.println(owlont);
            owlont = nameAnonymousClasses(owlont);
            // prefix mapping
            owlont = prefixMapping(owlont);
            //System.out.println(owlont);

            Triple[] element = getTriples(owlont);

            // group triples according to their subjects
            terms = groupTriples(element);
            referenceTable = getPageGroups(terms);

            //tracePages(terms, referenceTable);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    /**
     * Generate triples from N-Triples
     *
     * @param owlont String
     * @return String[]
     */
    private Triple[] getTriples(String owlont)
    {
        // split
        String[] element = owlont.split("\n");
        Triple[] triples = new Triple[element.length];

        // sort the triples
        Arrays.sort(element);

        for (int i = 0; i < element.length; i++)
        {
            // remove the last "."
            String triple = element[i].substring(0, element[i].length() - 2);
            int s1 = triple.indexOf("[");
            int s2 = triple.indexOf("]", s1);
            String subject = triple.substring(s1 + 1, s2);
            int p1 = triple.indexOf("[", s2);
            int p2 = triple.indexOf("]", p1);
            String predicate = triple.substring(p1 + 1, p2);

            int o1 = triple.indexOf("[", p2);
            int o2 = triple.indexOf("]", o1);
            String object = "";
            boolean literalObject;
            if (o1 == -1) // must be "xxx" or "xxx"^^xsd..yyy
            {
                o1 = triple.indexOf("\"", s2);
                object = triple.substring(o1);
                literalObject = true;
            }
            else // must be "[ooo]"
            {
                object = triple.substring(o1 + 1, o2);
                literalObject = false;
            }
            triples[i] = new Triple(subject, predicate, object, literalObject);

        }
        return triples;
    }

    /**
     * rename all anonymous classes
     *
     * @param owlont String - the ontology model in N-Triple format
     * @return String
     * @author Jie Bao
     * @version 2004-07-07
     */
    private String nameAnonymousClasses(String owlont)
    {
        Hashtable table = new Hashtable();

        //  anonymous classes start from _:
        final String rexResultCount = "_:.*?\\s";
        Matcher M = Pattern.compile(rexResultCount).matcher(owlont);
        int i = 0;
        while (M.find())
        {
            String str = M.group(0);
            if (!table.containsKey(str))
            {
                i++;
                table.put(str, " [" + AnonymousPrefix() + i + "] ");
            }
        }

        // Iterate over the keys in the map
        Iterator it = table.keySet().iterator();
        while (it.hasNext())
        {
            // Get key
            Object key = it.next();
            owlont = owlont.replaceAll( (String) key, (String) table.get(key));
        }

        return owlont;
    }

    private String AnonymousPrefix()
    {
        return localPrefix + PREFIX_LIMITATOR + DEFAULT_NAME;
    }

    /**
     * prefixMapping
     *
     * @param owlont String
     * @return String
     */
    private String prefixMapping(String owlont)
    {
        Hashtable table = new Hashtable();

        final String rexResultCount = "\\[(http.*?)\\]";
        Matcher M = Pattern.compile(rexResultCount).matcher(owlont);

        while (M.find())
        {
            String term = M.group(1);
            term = model.getModel().usePrefix(term);
            // replace prefixe limitator ":" with ".."
            // ".." is the only naming form can be accepted by wiki
            if (!term.startsWith("http"))
            {
                term = term.replaceAll(":", PREFIX_LIMITATOR);
                //System.out.println(str);
            }
            table.put(M.group(1), term);
        }
        // Iterate over the keys in the map
        Iterator it = table.keySet().iterator();
        while (it.hasNext())
        {
            // Get key
            Object key = it.next();
            owlont = owlont.replaceAll( (String) key, (String) table.get(key));
        }
        return owlont;

    }

    /**
     * groupTriples
     *
     * @param element String[]
     * @return String[]
     * @author Jie Bao
     * @version 2004-07-07
     */
    private Hashtable groupTriples(Triple[] triples)
    {
        Hashtable table = new Hashtable();

        for (int i = 0; i < triples.length; i++)
        {
            // generate key, will be the name or wiki page
            String subject = triples[i].getSubject();
            subject = filterHttp(subject);

            if (table.containsKey(subject))
            {
                Vector vect = (Vector) table.get(subject);
                vect.add(triples[i]);
            }
            else
            {
                Vector vect = new Vector();
                vect.add(triples[i]);
                table.put(subject, vect);
            }
        }
        return table;
    }

    /**
     * filterHttp
     *
     * @param subject String
     * @return String
     */
    private String filterHttp(String subject)
    {
        subject = subject.replaceAll(":", "..");
        subject = subject.replaceAll("/", "_");
        subject = subject.replaceAll("#", "--");
        return subject;
    }

    /**
     * getPageGroups: determine to which page the anonymous term should belong
     *
     * @param element String[] triples
     * @return Hashtable
     */
    private Hashtable getPageGroups(Hashtable tripleGroups)
    {
        Hashtable referenceMap = new Hashtable();

        // get the direct referer
        Iterator it = tripleGroups.keySet().iterator();
        while (it.hasNext())
        {
            // Get key, the name of this wiki page
            String subject = (String) it.next();
            if (!subject.startsWith(this.AnonymousPrefix()))
            {
                referenceMap.put(subject, subject);
            }
            // scan its objects
            Vector vect = (Vector) tripleGroups.get(subject); // contains all triples of this page

            for (int i = 0; i < vect.size(); i++)
            {
                Triple triple = (Triple) vect.elementAt(i);
                // the object
                String object = triple.getObject();
                if (object.startsWith(this.AnonymousPrefix())) // anonymous class
                {
                    referenceMap.put(object, subject);
                }
            }
        }

        // get the final referer
        it = referenceMap.keySet().iterator();
        while (it.hasNext())
        {
            // if the referer is also anonymous, find the fianl non-anonymous referer
            String subject = (String) it.next();
            String referer = (String) referenceMap.get(subject);
            String finalRef = findFinalReferer(referer, referenceMap);
            referenceMap.put(subject, finalRef);
        }
        // print   System.out.println(referenceMap);
        return referenceMap;
    }

    /**
     * findFinalReferer
     *
     * @param referer String
     * @param finalReferer Hashtable
     * @return String
     */
    private String findFinalReferer(String referer, Hashtable referenceMap)
    {
        if (referer == null)
        {
            return null;
        }
        if (!referer.startsWith(this.AnonymousPrefix()))
        {
            return referer;
        }
        else
        {
            return findFinalReferer( (String) referenceMap.get(referer),
                                    referenceMap);
        }

    }

    /**
     * save wiki pages
     *
     * @param pages Hashtable
     * @author Jie Bao
     * @version 2004-07-08
     */
    public void savePages(String dir)
    {
        if (terms == null || referenceTable == null)
        {
            return;
        }
        String list = "* ";
        Hashtable toSave = new Hashtable();

        String anonNumber = AnonymousPrefix() + "([0-9,]+)";

        Iterator it = terms.keySet().iterator();
        while (it.hasNext())
        {
            // Get key and contents
            String pagename = (String) it.next();
            Vector triples = (Vector) terms.get(pagename);

            String contens;
            Matcher M = Pattern.compile(anonNumber).matcher(pagename);
            if (M.find())
            {
                String num = M.group(1);
                contens = "; [" + pagename + "|#" + num + "]" + BREAKLINE;
            }
            else
            {
                contens = "; " + pagename + BREAKLINE;
            }
            for (int i = 0; i < triples.size(); i++)
            {
                String ss = ( (Triple) triples.elementAt(i)).toString();
                // filter anaymous link

                M = Pattern.compile(anonNumber).matcher(ss);
                while (M.find())
                {
                    String anon = M.group(0);
                    String num = M.group(1);
                    ss = ss.replaceAll(anon, anon + "|" + num);
                }

                contens = contens + list + ss + BREAKLINE;
            }

            // group it to its final referer
            String referer = (String) referenceTable.get(pagename);
            if (referer == null)
            {
                referer = pagename;
            }
            if (toSave.containsKey(referer))
            {
                // append
                String oldcontents = (String) toSave.get(referer);
                toSave.put(referer, oldcontents + contens);
            }
            else
            {
                // create new item
                toSave.put(referer, contens);
            }
        }
        // save it
        it = toSave.keySet().iterator();
        while (it.hasNext())
        {
            String pagename = (String) it.next();
            try
            {
                String name = dir + (String) pagename + ".txt";
                FileUtils.writeFile(name, (String) toSave.get(pagename));
            }
            catch (IOException ex)
            {
            }
        }
    }

    private void tracePrefixMapping()
    {
        if (prefixeMapping != null)
        {
            System.out.println(prefixeMapping.toString().replaceAll(",", "\n"));
            System.out.println("The base prefix:" +
                               localPrefix
                               /*model.getNsPrefix()*/
                               );
        }
    }

    // print to sysout
    // 2004-07-07
    private void tracePages(Hashtable pages, Hashtable reference)
    {
        Iterator it = pages.keySet().iterator();
        while (it.hasNext())
        {
            // Get key
            String pagename = (String) it.next();
            Vector vect = (Vector) pages.get(pagename);
            String referer = (String) reference.get(pagename);
            if (referer == null)
            {
                referer = pagename;
            }
            System.out.println("==== " + pagename + "-> " + referer + " ====");
            for (int i = 0; i < vect.size(); i++)
            {
                System.out.println(vect.elementAt(i));
            }
        }
    }

}
