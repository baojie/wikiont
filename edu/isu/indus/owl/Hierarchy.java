package edu.isu.indus.owl;

import java.util.Enumeration;
import java.util.TreeSet;

import edu.isu.indus.owl.jena.tree.DefaultNode;
import edu.isu.indus.owl.jena.tree.Tree;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Hierarchy Ontology
 * @author Jie Bao
 * @version 1.0 2004-02-08
 */

public class Hierarchy
    extends Ontology
{
    private Tree tree = new Tree();
    TreeSet set = new TreeSet();
    static Model model = ModelFactory.createDefaultModel();

    public Hierarchy()
    {

        Resource res = model.createResource(
            "http://www.w3.org/2002/07/owl#Thing");
        tree.setTop(new DefaultNode(res));
    }

    public int getTreeSize()
    {
        return set.size();
    }

    public void clearList()
    {
        if (classList != null)
        {
            classList.removeAll(classList);
        }
        set.removeAll(set);
        Resource res = model.createResource(
            "http://www.w3.org/2002/07/owl#Thing");
        tree.setTop(new DefaultNode(res));
    }

    /**
     *
     * @param toFind String - the url of resource
     * @return DefaultNode
     */
    private DefaultNode findNodeInSet(String url)
    {
        if (url == null || url.length() == 0)
        {
            return null;
        }

        DefaultNode s;
        Name ss = new Name(url, null);
        if (!set.contains(ss))
        {
            // not in the tree yet
            // create the node
            Resource res = model.createResource(url);
            s = new DefaultNode(res);
            set.add(new Name(url, s));
            tree.getTop().add(s);
//            System.out.println("add " + toFind);
        }
        else
        {
            Name temp = (Name) set.tailSet(ss).first();
            s = temp.node;
//            System.out.println("Find "+s);
        }
        return s;
    }

    public void fromTriple()
    {
        String subclass = new String("rdfs:subClassOf");

        Enumeration e = classList.elements();
        while (e.hasMoreElements())
        {
            Triple t = (Triple) e.nextElement();
            if (t.predicate.equalsIgnoreCase(subclass))
            {
                DefaultNode subject = findNodeInSet(t.subject);
                DefaultNode object = findNodeInSet(t.object);
                if (object != null && subject != null)
                {
                    if (!object.isNodeAncestor(subject))
                    {
                        object.add(subject);
                    }
                }
            }
        }
    }

    public String print(boolean toHTML)
    {
        if (tree == null)
        {
            return "";
        }
        else
        {
            return tree.toString(toHTML);
        }
    }

    public void addTriple(String s, String p, String o)
    {
        classList.add(new Triple(s, p, o));
    }

    // for test purpose
    public static void main(String[] args)
    {
        Hierarchy h = new Hierarchy();
        h.addTriple("http://semanticWWW.com/indus.owl#Ames",
                    "rdfs:subClassOf",
                    "http://semanticWWW.com/indus.owl#Iowa");
        h.addTriple("http://semanticWWW.com/indus.owl#DesMoines",
                    "rdfs:subClassOf",
                    "http://semanticWWW.com/indus.owl#Iowa");
        h.addTriple("http://semanticWWW.com/indus.owl#Iowa",
                    "rdfs:subClassOf",
                    "http://semanticWWW.com/indus.owl#USA");
        h.addTriple("http://semanticWWW.com/indus.owl#Virginia",
                    "rdfs:subClassOf",
                    "http://semanticWWW.com/indus.owl#u USA");

        h.fromTriple();
        System.out.print(h.tree.toString(false));
    }

    public Tree getTree()
    {
        return tree;
    }
}
