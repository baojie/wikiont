package edu.isu.indus.owl.jena.tree;

import java.util.Enumeration;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.*;
import javax.swing.*;

/**
 * Tree Data Structure
 * @author Jie Bao
 * @version 1.0 2004-02-08
 */

public class Tree
{
    static String EDITOR = "instance.jsp?term=";

    DefaultNode top;

    public String toString()
    {
        return toString(false);
    }

    public String toString(boolean toHTML)
    {
        String blank = toHTML ? "&nbsp;&nbsp;&nbsp;&nbsp;" : "    ";
        String endline = "|---";
        String line = toHTML ? "|&nbsp;&nbsp;&nbsp;" : "|   ";
        String crlf = toHTML ? "<BR></BR>" : "\n";

        if (top == null)
        {
            return "";
        }
        String toPrint = "";

        Enumeration e = top.preorderEnumeration();
        while (e.hasMoreElements())
        {
            DefaultNode nn = (DefaultNode) e.nextElement();
            String leading = line;

            if (nn.getLevel() == 0)
            {
                leading = "";
            }
            else if (nn.getLevel() == 1)
            {
                leading = endline;
            }
            else
            {
                leading = endline;
                for (int i = 0; i < nn.getLevel() - 1; i++)
                {
                    leading = line + leading;
                }
            }
            toPrint += leading + toLink(nn) + crlf;
        }

        if (toHTML)
        {
            return toPrint;
        }
        else
        {
            return toPrint;
        }
    }

    /**
     * toLink
     *
     * @param nn DefaultNode
     * @return String
     */
    private String toLink(DefaultNode nn)
    {
        Resource res = (Resource) nn.getUserObject();
        String url = res.getURI();
        String str = "<a href=\"" + EDITOR +
            url.replaceAll("#", "!!!!") + "\">" +
            res.getLocalName() + "</a>";

        return str;
    }

    public void setTop(DefaultNode top)
    {
        this.top = top;
    }

    public DefaultNode getTop()
    {
        return top;
    }

    private void sampleTree()
    {
        Model model = ModelFactory.createDefaultModel();
        top = new DefaultNode("http://semanticWWW.com/indus.owl#USA");

        DefaultNode iowa = new DefaultNode(
            "http://semanticWWW.com/indus.owl#Iowa");
        top.add(iowa);
        iowa.add(new DefaultNode("http://semanticWWW.com/indus.owl#Ames"));
        iowa.add(new DefaultNode("http://semanticWWW.com/indus.owl#DesMoines"));

        DefaultNode va = new DefaultNode(
            "http://semanticWWW.com/indus.owl#Virginia");
        top.add(va);
        va.add(new DefaultNode("http://semanticWWW.com/indus.owl#Richmond"));
        va.add(new DefaultNode("http://semanticWWW.com/indus.owl#Petersberg"));

    }

// for test purpose
    public static void main(String[] args)
    {
        Tree t = new Tree();
        t.sampleTree();

        JFrame frame = new JFrame();
        frame.setSize(800, 600);

        JEditorPane pane = new JEditorPane();
        frame.getContentPane().add(new JScrollPane(pane));

//b.listTriple();
        pane.setContentType("text/html");
        pane.setText(t.toString(true));

        frame.setVisible(true);

       // System.out.print(t);
    }
}
