package edu.iastate.anthill.indus.owl.hierarchy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import edu.iastate.anthill.indus.owl.OntologyWithPackage;
import edu.iastate.anthill.indus.owl.jena.util.ClassUtil;
import edu.iastate.anthill.indus.owl.node.ClassNode;
import edu.iastate.anthill.indus.owl.node.DefaultOntologyNode;
import java.io.PrintStream;
import edu.iastate.anthill.indus.owl.jena.render.TermRenderer;

/**
 * Hierarchy based on subClassOf
 *
 * @author Jie Bao
 * @since 2004-05-02
 */
public class SubClassHierarchy
    extends OntHierarchyImpl
    implements OntHierarchy
{
    static OntologyWithPackage jenaModel;

    public SubClassHierarchy(OntologyWithPackage jenaModel)
    {
        this.jenaModel = jenaModel;
    }

    /**
     * createFromModel
     *
     * @param jenaModel JenaModel
     * @return Tree
     */
    public Tree createFromModel(OntologyWithPackage jenaModel)
    {
        OntModel model = jenaModel.getModel();

        DefaultOntologyNode root = new MetaNode(DefaultOntologyNode.ALL_CLASSES);

        Tree t = new Tree(root);

        for (Iterator i = ClassUtil.rootClasses(model); i.hasNext(); )
        {
            addChild(root, (OntClass) i.next(), new ArrayList(), 0);
        }

        return t;
    }

    /** Present a class, then recurse down to the sub-classes.
     *  Use occurs check to prevent getting stuck in a loop
     */
    static protected void addChild(DefaultOntologyNode parent, OntClass cls,
                                   List occurs, int depth)
    {
        ClassNode thisClass = new ClassNode(cls, jenaModel);
        parent.add(thisClass);

        // recurse to the next level down
        if (cls.canAs(OntClass.class) && !occurs.contains(cls))
        {
            for (Iterator i = cls.listSubClasses(true); i.hasNext(); )
            {
                OntClass sub = (OntClass) i.next();

                // we push this expression on the occurs list before we recurse
                occurs.add(cls);
                addChild(thisClass, sub, occurs, depth + 1);
                occurs.remove(cls);
            }
        }
    }

    static String NEWLINE;
    /** Show the sub-class hierarchy encoded by the given model */
    public static void showHierarchy(PrintStream out, OntModel m, boolean inHTML)
    {
        NEWLINE = inHTML ? "<BR>\n" : "\n";

        for (Iterator i = ClassUtil.rootClasses(m); i.hasNext(); )
        {
            showClass(out, (OntClass) i.next(), new ArrayList(), 0);
        }
    }

    // Internal implementation methods
    //////////////////////////////////

    /** Present a class, then recurse down to the sub-classes.
     *  Use occurs check to prevent getting stuck in a loop
     */
    protected static void showClass(PrintStream out, OntClass cls, List occurs,
                             int depth)
    {
        TermRenderer.renderClassDescription(out, cls, depth, false);
        out.print(NEWLINE);

        // recurse to the next level down
        if (cls.canAs(OntClass.class) && !occurs.contains(cls))
        {
            for (Iterator i = cls.listSubClasses(true); i.hasNext(); )
            {
                OntClass sub = (OntClass) i.next();

                // we push this expression on the occurs list before we recurse
                occurs.add(cls);
                showClass(out, sub, occurs, depth + 1);
                occurs.remove(cls);
            }
        }
    }

    public static void main(String[] args)
    {
        OntologyWithPackage dd = new OntologyWithPackage(
            "http://www.mindswap.org/~aditkal/wine.owl#", "wine");

        OntModel m = dd.getModel();

        // we have a local copy of the wine ontology
        m.getDocumentManager().addAltEntry(
            "http://www.mindswap.org/~aditkal/wine.owl",
            "file:///F:\\0-Me\\8-Programming\\0-Java\\7-Ontology\\JSPWiki\\1.0\\localOnt\\wine.owl");

        m.read("http://www.mindswap.org/~aditkal/wine.owl");
        SubClassHierarchy s = new SubClassHierarchy(dd);
        System.out.println(s.createFromModel(dd));
        s.showHierarchy(System.out, m, false);
    }

}
